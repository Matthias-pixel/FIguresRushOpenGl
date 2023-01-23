package de.ideaonic703.gd.engine;

import de.ideaonic703.gd.engine.renderer.Renderer;

import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    protected boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public void start() {
        for(GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }
    public void addGameObjectToScene(GameObject go) {
        gameObjects.add(go);
        if(isRunning) {
            go.start();
            this.renderer.add(go);
        }
    }
    public abstract void update(float dt);
    public void init() {}
    public Camera getCamera() {
        return this.camera;
    }
}
