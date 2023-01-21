package de.ideaonic703.gd.engine;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;
    public LevelEditorScene() {

    }
    @Override
    public void init() {

    }
    @Override
    public void update(float dt) {
        System.out.println("fps: " + 1/dt);
        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
            changingScene = true;
        if(changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.getInstance().r -= dt*0.5f;
            Window.getInstance().g -= dt*0.5f;
            Window.getInstance().b -= dt*0.5f;
        } else if(changingScene && timeToChangeScene <= 0) {
            Window.changeScene(1);
        }
    }
}
