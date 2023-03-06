package de.ideaonic703.gd.engine;

import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.renderer.Renderer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera, camera2 = null;
    protected boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;

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
    public void removeGameObjectFromScene(GameObject go) {
        this.gameObjects.remove(go);
        if(go.getComponent(SpriteRenderer.class) != null) {
            this.renderer.remove(go);
        }
    }
    public abstract void update(float dt);
    public void init() {}
    public Camera getCamera() {
        return this.camera;
    }
    public void sceneImgui() {
        if(activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }
        imgui();
    }
    public void imgui() {}

    public Camera getCamera2() {
        return camera2 == null ? camera : camera2;
    }

    public boolean hasSecondaryCamera() {
        return camera2 != null;
    }
}
