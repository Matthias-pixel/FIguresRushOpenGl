package de.ideaonic703.gd;

import de.ideaonic703.gd.components.ComplexSpritesheet;
import de.ideaonic703.gd.engine.Window;
import de.ideaonic703.gd.game.Level;
import de.ideaonic703.gd.game.SaveData;
import de.ideaonic703.gd.game.objects.SolidBlock;
import de.ideaonic703.gd.game.scenes.MainMenu;
import org.joml.Vector2f;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Main.generateDummyLevels();
        Main.generateDummySaveData();
        //Window window = Window.getInstance("Geometry Dash", 2560, 1440, new MainMenu());
        Window window = Window.getInstance("Geometry Dash", 1920, 1080, new MainMenu());
        window.init();
        loadResources();
        window.run();
    }
    public static void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addComplexSpritesheet("assets/gdresources/GJ_GameSheet-uhd", ComplexSpritesheet.loadFromFile("assets/gdresources/GJ_GameSheet-uhd"));
        AssetPool.addComplexSpritesheet("assets/CustomSpritesheet", ComplexSpritesheet.loadFromFile("assets/CustomSpritesheet"));
        AssetPool.addComplexSpritesheet("assets/gdresources/GJ_GameSheetGlow-uhd", ComplexSpritesheet.loadFromFile("assets/gdresources/GJ_GameSheetGlow-uhd"));
        for(int gameSheet = 2; gameSheet <= 4; gameSheet++) {
            AssetPool.addComplexSpritesheet("assets/gdresources/GJ_GameSheet0"+ gameSheet +"-uhd", ComplexSpritesheet.loadFromFile("assets/gdresources/GJ_GameSheet0"+ gameSheet +"-uhd"));
        }
        for(int bgType = 1; bgType <= 20; bgType++) {
            if(bgType < 10) {
                AssetPool.getTexture("assets/gdresources/game_bg_0" + bgType + "_001-uhd.png");
                AssetPool.getTexture("assets/gdresources/groundSquare_0" + bgType + "_001-uhd.png");
                if(bgType >= 8) AssetPool.getTexture("assets/gdresources/groundSquare_0" + bgType + "_2_001-uhd.png");
            } else {
                AssetPool.getTexture("assets/gdresources/game_bg_" + bgType + "_001-uhd.png");
                if(bgType <= 17) AssetPool.getTexture("assets/gdresources/groundSquare_" + bgType + "_001-uhd.png");
                if(bgType >= 8 && bgType < 17) AssetPool.getTexture("assets/gdresources/groundSquare_" + bgType + "_2_001-uhd.png");
            }
        }
        for(int i = 1; i <= 9; i++) {
            AssetPool.getFont("assets/gdresources/gjFont0" + i + "-uhd");
        }
        for(int i = 10; i <= 11; i++) {
            AssetPool.getFont("assets/gdresources/gjFont" + i + "-uhd");
        }
        AssetPool.getFont("assets/gdresources/goldFont-uhd");
        AssetPool.getFont("assets/gdresources/bigFont-uhd");
        AssetPool.loadLevels();
        AssetPool.freeze();
    }
    public static void generateDummyLevels() {
        Level stereoMadness = new Level("Stereo Madness", 1000, 14, 12, 1, 0);
        stereoMadness.addObject(new SolidBlock(new Vector2f(30f, 0f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(31f, 0f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(31f, 1f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(32f, 0f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(32f, 1f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(33f, 0f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(33f, 1f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(33f, 2f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(34f, 0f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(34f, 1f), SolidBlock.BLOCK_DEFAULT));
        stereoMadness.addObject(new SolidBlock(new Vector2f(34f, 2f), SolidBlock.BLOCK_DEFAULT));
        Level backOnTrack = new Level("Back On Track", 1000, 14, 12, 1, 1);
        Level polargeist = new Level("Polargeist", 1000, 14, 12, 2, 2);
        Level dryOut = new Level("Dry Out", 1000, 14, 12, 2, 3);
        try {
            stereoMadness.save();
            backOnTrack.save();
            polargeist.save();
            dryOut.save();
        } catch (IOException e) {
            assert false;
        }
    }
    private static void generateDummySaveData() {
        SaveData saveData = new SaveData(new SaveData.LevelSaveData[]{
                new SaveData.LevelSaveData(1.0f, 0.0f, new boolean[]{false, false, true}),
                new SaveData.LevelSaveData(0.5f, 0.5f, new boolean[]{false, true, false}),
                new SaveData.LevelSaveData(0.1f, 0.9f, new boolean[]{false, true, true}),
                new SaveData.LevelSaveData(0.9f, 0.1f, new boolean[]{true, false, false})
        });
        try {
            saveData.saveToFile("gdSave.bin");
        } catch (IOException e) {
            assert false;
        }
    }
}
