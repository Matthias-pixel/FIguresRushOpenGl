package de.ideaonic703.gd;

import de.ideaonic703.gd.components.ComplexSpritesheet;
import de.ideaonic703.gd.engine.Window;
import de.ideaonic703.gd.game.scenes.MainMenu;

public class Main {
    public static void main(String[] args) {
        //Window window = Window.getInstance("Geometry Dash", 2560, 1440, new MainMenu());
        Window window = Window.getInstance("Geometry Dash", -1, -1, new MainMenu());
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
        AssetPool.freeze();
    }
}
