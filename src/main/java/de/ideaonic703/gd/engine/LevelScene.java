package de.ideaonic703.gd.engine;

public class LevelScene extends Scene {
    public LevelScene() {
    }
    @Override
    public void update(float dt) {
        Window.getInstance().r = 1;
        Window.getInstance().g = 1;
        Window.getInstance().b = 1;
    }

    @Override
    public void init() {
        super.init();
        System.out.println("Message from LevelScene!");
    }
}
