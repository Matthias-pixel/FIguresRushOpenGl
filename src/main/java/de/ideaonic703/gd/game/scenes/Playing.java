package de.ideaonic703.gd.game.scenes;

import de.ideaonic703.gd.engine.Camera;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.Scene;
import de.ideaonic703.gd.game.Level;
import de.ideaonic703.gd.game.objects.Background;
import org.joml.Vector2f;

import java.awt.*;
import java.util.List;

public class Playing extends Scene {
    private Background background;
    private Level level;
    private final float scaleFactor = 107f;
    private final float scaledWith = 1920f/scaleFactor;
    private final float scaledHeight = 1080f/scaleFactor;

    public Playing(Level level) {
        this.level = level;
    }
    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.camera.adjustProjection();
        this.camera2 = new Camera(new Vector2f(0, -2.81f));
        this.camera2.adjustProjection(scaledWith, scaledHeight);
        background = new Background(14, 12);
        background.setColor(new Color(180, 80, 230));
        background.addTo(this);
        loadNewGameObjects();
    }
    private void loadNewGameObjects() {
        GameObject[] gameObjects = this.level.getObjectsAt(0, 0, (int)scaledWith, (int)scaledHeight);
        for(GameObject go : gameObjects) {
            if(!this.gameObjects.contains(go)) {
                go.init();
                this.addGameObjectToScene(go);
            }
        }
    }
    private void freeGameObjects() {
        List<GameObject> gameObjects = List.of(this.level.getObjectsAt(0, 0, (int)scaledWith, (int)scaledHeight));
        for(GameObject go : this.gameObjects) {
            if(!gameObjects.contains(go) && !this.background.hasGameObject(go)) {
                this.removeGameObjectFromScene(go);
            }
        }
    }

    @Override
    public void update(float dt) {
        //System.out.printf("FPS: %f%n", 1f/dt);
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }
}
