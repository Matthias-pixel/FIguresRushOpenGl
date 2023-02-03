package de.ideaonic703.gd.game;

import de.ideaonic703.gd.Serializer;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.game.objects.SolidBlock;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Level {
    private static final int LEVEL_VERSION = 3;
    private List<List<GameObject>> data;
    private String name;
    private int backgroundType, floorType, difficulty, levelID;

    public Level(String name, int width, int backgroundType, int floorType, int difficulty, int levelID) {
        this.levelID = levelID;
        this.name = name;
        this.backgroundType = backgroundType;
        this.floorType = floorType;
        this.difficulty = difficulty;
        data = new ArrayList<>();
        setWidth(width);
    }
    private Level(String name, List<List<GameObject>> data, int backgroundType, int floorType, int difficulty, int levelID) {
        this.levelID = levelID;
        this.name = name;
        this.data = data;
        this.backgroundType = backgroundType;
        this.floorType = floorType;
        this.difficulty = difficulty;
    }
    public void setWidth(int newWidth) {
        newWidth++;
        if(newWidth < 0) throw new IndexOutOfBoundsException();
        while(data.size() > newWidth) {
            data.remove(newWidth);
        }
        for(int i = 0; i < newWidth; i++) {
            if(i >= data.size()) data.add(new ArrayList<>());
        }
    }
    public void addObject(GameObject gameObject) {
        List<GameObject> column = this.data.get(gameObject.transform.getPosition().x);
        column.add(gameObject);
    }
    public GameObject[] getObjectsAt(int x, int y) {
        List<GameObject> result = new ArrayList<>();
        List<GameObject> columnLeft = this.data.get(x-1);
        List<GameObject> column = this.data.get(x);
        for(GameObject go : columnLeft) {
            if(go.transform.getOffset().x > 0 && (go.transform.getPosition().y == y || (go.transform.getPosition().y == y-1 && go.transform.getOffset().y > 0))) {
                result.add(go);
            }
        }
        for(GameObject go : column) {
            if(go.transform.getPosition().y == y || (go.transform.getPosition().y == y-1 && go.transform.getOffset().y > 0)) {
                result.add(go);
            }
        }
        return result.toArray(new GameObject[0]);
    }
    public int getDifficulty() {
        return difficulty;
    }
    public int getBackgroundType() {
        return backgroundType;
    }
    public int getFloorType() {
        return floorType;
    }
    public int getLevelID() {return this.levelID;}
    public String getName() {
        return this.name;
    }
    public void save() throws IOException {
        save(new File("levels/"+name.toLowerCase(Locale.ROOT).replace("\\s+", "").replace(" ", "")));
    }
    public void save(File levelFile) throws IOException {
        if(!levelFile.exists()) levelFile.createNewFile();
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(levelFile));
        stream.writeInt(LEVEL_VERSION);
        stream.writeInt(this.floorType);
        stream.writeInt(this.backgroundType);
        stream.writeInt(this.difficulty);
        stream.writeInt(this.levelID);
        stream.writeObject(name);
        stream.writeInt(data.size());
        for (List<GameObject> column : data) {
            stream.writeInt(column.size());
            for (GameObject gameObject : column) {
                if (gameObject instanceof SolidBlock casted) casted.save(stream);
                else
                    assert false : gameObject + " of type '" + gameObject.getClass().getSimpleName() + "' can not be serialized.";
            }
        }
        stream.close();
    }
    public static Level load(File levelFile) throws IOException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(levelFile));
        int version = stream.readInt();
        if(version != LEVEL_VERSION) {
            return Level.loadAndSaveLegacy(levelFile);
        }
        int floorType = stream.readInt();
        int backgroundType = stream.readInt();
        int difficulty = stream.readInt();
        int levelID = stream.readInt();
        String name;
        try {
            name = (String) stream.readObject();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            assert false : "Deserialization failure: Expected String, got unknown class. See Stack trace for more details.";
            throw new IOException();
        }
        int dataSize = stream.readInt();
        List<List<GameObject>> data = new ArrayList<>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            int columnSize = stream.readInt();
            List<GameObject> column = new ArrayList<>(columnSize);
            for (int j = 0; j < columnSize; j++) {
                int objectId = stream.readInt();
                if (objectId == Serializer.TYPES.SOLID_BLOCK.ordinal()) column.add(i, SolidBlock.load(stream));
                else
                    assert false : "Object of type '" + objectId + "' can not be deserialized.";
            }
            data.add(i, column);
        }
        Level nLevel = new Level(name, data, backgroundType, floorType, difficulty, levelID);
        //nLevel.save();
        return nLevel;
    }
    private static Level loadAndSaveLegacy(File levelFile) throws IOException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(levelFile));
        int version = stream.readInt();
        Level level = null;
        switch(version) {
            case 1: {
                level = loadLegacy1(stream); break;
            }
            case 2: {
                level = loadLegacy2(stream); break;
            }
            case 3: {
                level = loadLegacy3(stream); break;
            }
        }
        if(level == null) {
            assert false : "Unknown legacy version number of level '" + levelFile.getAbsolutePath() + "'.";
            throw new IOException();
        }
        level.save(levelFile);
        return level;
    }
    private static Level loadLegacy1(ObjectInputStream stream) throws IOException {
        String name;
        try {
            name = (String) stream.readObject();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            assert false : "Deserialization failure: Expected String, got unknown class. See Stack trace for more details.";
            throw new IOException();
        }
        int dataSize = stream.readInt();
        List<List<GameObject>> data = new ArrayList<>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            int columnSize = stream.readInt();
            List<GameObject> column = new ArrayList<>(columnSize);
            for (int j = 0; j < columnSize; j++) {
                int objectId = stream.readInt();
                if (objectId == Serializer.TYPES.SOLID_BLOCK.ordinal()) column.add(i, SolidBlock.load(stream));
                else
                    assert false : "Object of type '" + objectId + "' can not be deserialized.";
            }
            data.add(i, column);
        }
        int levelID = 0;
        if(name.equalsIgnoreCase("Stereo Madness")) levelID = 0;
        if(name.equalsIgnoreCase("Back On Track")) levelID = 1;
        if(name.equalsIgnoreCase("Polargeist")) levelID = 2;
        if(name.equalsIgnoreCase("Dry Out")) levelID = 3;
        return new Level(name, data, 1, 1, 1, levelID);
    }
    public static Level loadLegacy2(ObjectInputStream stream) throws IOException {
        int floorType = stream.readInt();
        int backgroundType = stream.readInt();
        int difficulty = stream.readInt();
        String name;
        try {
            name = (String) stream.readObject();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            assert false : "Deserialization failure: Expected String, got unknown class. See Stack trace for more details.";
            throw new IOException();
        }
        int dataSize = stream.readInt();
        List<List<GameObject>> data = new ArrayList<>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            int columnSize = stream.readInt();
            List<GameObject> column = new ArrayList<>(columnSize);
            for (int j = 0; j < columnSize; j++) {
                int objectId = stream.readInt();
                if (objectId == Serializer.TYPES.SOLID_BLOCK.ordinal()) column.add(i, SolidBlock.load(stream));
                else
                    assert false : "Object of type '" + objectId + "' can not be deserialized.";
            }
            data.add(i, column);
        }
        int levelID = 0;
        if(name.equalsIgnoreCase("Stereo Madness")) levelID = 0;
        if(name.equalsIgnoreCase("Back On Track")) levelID = 1;
        if(name.equalsIgnoreCase("Polargeist")) levelID = 2;
        if(name.equalsIgnoreCase("Dry Out")) levelID = 3;
        return new Level(name, data, backgroundType, floorType, difficulty, levelID);
    }
    public static Level loadLegacy3(ObjectInputStream stream) throws IOException {
        int floorType = stream.readInt();
        int backgroundType = stream.readInt();
        int difficulty = stream.readInt();
        int levelID = stream.readInt();
        String name;
        try {
            name = (String) stream.readObject();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            assert false : "Deserialization failure: Expected String, got unknown class. See Stack trace for more details.";
            throw new IOException();
        }
        int dataSize = stream.readInt();
        List<List<GameObject>> data = new ArrayList<>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            int columnSize = stream.readInt();
            List<GameObject> column = new ArrayList<>(columnSize);
            for (int j = 0; j < columnSize; j++) {
                int objectId = stream.readInt();
                if (objectId == Serializer.TYPES.SOLID_BLOCK.ordinal()) column.add(i, SolidBlock.load(stream));
                else
                    assert false : "Object of type '" + objectId + "' can not be deserialized.";
            }
            data.add(i, column);
        }
        Level nLevel = new Level(name, data, backgroundType, floorType, difficulty, levelID);
        //nLevel.save();
        return nLevel;
    }
}
