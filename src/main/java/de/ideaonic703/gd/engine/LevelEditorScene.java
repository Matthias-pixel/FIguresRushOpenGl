package de.ideaonic703.gd.engine;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.Sprite;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.components.Spritesheet;
import de.ideaonic703.gd.engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {
    private GameObject obj1, obj2;
    Spritesheet sprites;

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(0, 0));
        this.camera.adjustProjection();

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 2);
        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/blendImage1.png"))));
        obj2 = new GameObject("Object 2", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 1);
        obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/blendImage2.png"))));
        this.addGameObjectToScene(obj1);
        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/ExampleSpritesheet.png", new Spritesheet(AssetPool.getTexture("assets/images/ExampleSpritesheet.png"), 16, 16, 26));
        AssetPool.getTexture("assets/images/blendImage1.png");
        AssetPool.getTexture("assets/images/blendImage2.png");
        AssetPool.freeze();
        sprites = AssetPool.getSpritesheet("assets/images/ExampleSpritesheet.png");
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS: " + (1.0f / dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
