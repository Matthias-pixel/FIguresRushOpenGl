package de.ideaonic703.gd.game.scenes;

import de.ideaonic703.gd.engine.Camera;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.Scene;
import de.ideaonic703.gd.game.objects.Background;
import org.joml.Vector2f;

public class MainMenu extends Scene {

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.camera.adjustProjection();
        Background background = new Background(14, 12);
        background.addTo(this);
    }

    @Override
    public void update(float dt) {
        System.out.printf("FPS: %f%n", 1f/dt);
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imgui() {

    }
}
