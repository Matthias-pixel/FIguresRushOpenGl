package de.ideaonic703.gd.game;

import java.io.*;

public class SaveData {
    private LevelSaveData[] levelSaves;
    public SaveData(LevelSaveData[] levelSaves) {
        this.levelSaves = levelSaves;
    }
    public LevelSaveData getLevelSave(int levelId) {
        if(levelId < 0 || levelId >= this.levelSaves.length) return new LevelSaveData(0, 0, new boolean[]{false, false, false});
        else return this.levelSaves[levelId];
    }
    public static SaveData loadFromFile(String filename) throws IOException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filename));
        int levelCount = stream.readInt();
        LevelSaveData[] levels = new LevelSaveData[levelCount];
        for(int i = 0; i < levelCount; i++) {
            float completion = stream.readFloat();
            float practiceCompletion = stream.readFloat();
            boolean[] coins = new boolean[] {
                    stream.readBoolean(),
                    stream.readBoolean(),
                    stream.readBoolean()
            };
            levels[i] = new LevelSaveData(completion, practiceCompletion, coins);
        }
        stream.close();
        return new SaveData(levels);
    }
    public void saveToFile(String filename) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filename));
        stream.writeInt(this.levelSaves.length);
        for(int i = 0; i < this.levelSaves.length; i++) {
            stream.writeFloat(this.levelSaves[i].completion);
            stream.writeFloat(this.levelSaves[i].practiceCompletion);
            stream.writeBoolean(this.levelSaves[i].coins[0]);
            stream.writeBoolean(this.levelSaves[i].coins[1]);
            stream.writeBoolean(this.levelSaves[i].coins[2]);
        }
        stream.flush();
        stream.close();
    }
    public record LevelSaveData(float completion, float practiceCompletion, boolean[] coins) {}
}
