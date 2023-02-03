package de.ideaonic703.gd.game.scenes;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.engine.Camera;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.Scene;
import de.ideaonic703.gd.game.Level;
import de.ideaonic703.gd.game.objects.Background;
import de.ideaonic703.gd.game.objects.LevelSelector;
import org.joml.Vector2f;

public class LevelSelectScreen extends Scene {
    private float initTransition = 0.0f;
    private Background background;
    private Level[] levels;
    private LevelSelector[] levelSelectors;

    public LevelSelectScreen(Background background) {
        super();
        this.background = background;
    }

    @Override
    public void init() {
        super.init();
        this.initTransition = 100.0f;
        this.camera = new Camera(new Vector2f(0, 0));
        this.camera.adjustProjection();
        for(GameObject groundTile : background.groundTiles) {
            groundTile.setzIndex(2);
        }
        for(GameObject groundTile : background.groundTiles2) {
            groundTile.setzIndex(3);
        }
        background.addTo(this);
        levels = AssetPool.getLevels();
        levelSelectors = new LevelSelector[levels.length];
        for (int i = 0; i < levelSelectors.length; i++) {
            levelSelectors[i] = new LevelSelector(i, levels[i]);
            levelSelectors[i].addToScene(this);
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(float dt) {
        if(this.initTransition > 0.0f) {
            for(int i = 0; i < this.levelSelectors.length; i++) {
                this.levelSelectors[i].setYOffset((float) (-0.0007*Math.pow(this.initTransition, 3)));
            }

            this.initTransition -= Math.min(dt, 0.1f)*200;
        } else if(this.initTransition < 0.0f) {
            this.initTransition = 0.0f;
        }
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }
}
