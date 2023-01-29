package de.ideaonic703.gd.game;

import de.ideaonic703.gd.Serializer;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.game.objects.SolidBlock;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Level {
    private List<List<GameObject>> data;
    private String name;

    public Level(String name, int width) {
        this.name = name;
        data = new ArrayList<>();
        setWidth(width);
    }
    private Level(String name, List<List<GameObject>> data) {
        this.name = name;
        this.data = data;
    }
    public void setWidth(int newWidth) {
        newWidth++;
        if(newWidth < 0) throw new IndexOutOfBoundsException();
        while(data.size() > newWidth) {
            data.remove(newWidth);
        }
        for(int i = 0; i < newWidth; i++) {
            if(data.get(i) == null) data.add(new ArrayList<>());
        }
    }
    public void addObject(GameObject gameObject) {
        List<GameObject> column = this.data.get(gameObject.transform.position.x);
        column.add(gameObject);
    }
    public GameObject[] getObjectsAt(int x, int y) {
        List<GameObject> result = new ArrayList<>();
        List<GameObject> columnLeft = this.data.get(x-1);
        List<GameObject> column = this.data.get(x);
        for(GameObject go : columnLeft) {
            if(go.transform.offset.x > 0 && (go.transform.position.y == y || (go.transform.position.y == y-1 && go.transform.offset.y > 0))) {
                result.add(go);
            }
        }
        for(GameObject go : column) {
            if(go.transform.position.y == y || (go.transform.position.y == y-1 && go.transform.offset.y > 0)) {
                result.add(go);
            }
        }
        return result.toArray(new GameObject[0]);
    }
    public void save() throws IOException {
        save(name.toLowerCase(Locale.ROOT).replace("\\s+", ""));
    }
    public void save(String filename) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("levels/"+name+".lvl"));
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
    }
    public static Level load(String filename) throws IOException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream("levels/"+filename+".lvl"));
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
        return new Level(name, data);
    }
}
