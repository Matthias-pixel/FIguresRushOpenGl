package de.ideaonic703.gd.game.scenes;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.Sprite;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.Camera;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.Scene;
import de.ideaonic703.gd.engine.Transform;
import de.ideaonic703.gd.game.Level;
import de.ideaonic703.gd.game.objects.Background;
import de.ideaonic703.gd.game.objects.LevelSelector;
import de.ideaonic703.gd.game.objects.MenuButton;
import org.joml.Vector2f;

public class LevelSelectScreen extends Scene {
    private float initTransition = 0.0f, levelTransition = 0.0f;
    private int currentLevel = 0;
    private boolean advanceLevel;
    private Background background;
    private Level[] levels;
    private LevelSelector[] levelSelectors;
    private MenuButton[] navigationArrows = new MenuButton[2];

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
        levels = AssetPool.getLevels();
        levelSelectors = new LevelSelector[levels.length];
        for (int i = 0; i < levelSelectors.length; i++) {
            levelSelectors[i] = new LevelSelector(i, levels[i]);
            levelSelectors[i].addToScene(this);
        }
        //navArrowBtn_001.png
        {
            Sprite navButtonSprite = AssetPool.getComplexSpritesheet("assets/gdresources/GJ_GameSheet03-uhd").getSprite("navArrowBtn_001.png");
            this.navigationArrows[0] = new MenuButton(
                    new Transform(new Vector2f(100f, 1080f/2f), new Vector2f(90, 200)).withNormalizedOrigin(Transform.ORIGIN_CENTER),
                    navButtonSprite ){
                @Override
                public void onClick() {
                    super.onClick();
                    if(levelTransition % 1.0f == 0) {
                        currentLevel -= 1;
                        currentLevel %= levels.length+1;
                        levelTransition = 1.0f;
                        advanceLevel = false;
                    }
                }
            };
            this.navigationArrows[1] = new MenuButton(
                    new Transform(new Vector2f(1920f-100f, 1080f/2f), new Vector2f(90, 200)).withNormalizedOrigin(Transform.ORIGIN_CENTER),
                    navButtonSprite){
                @Override
                public void onClick() {
                    super.onClick();
                    if(levelTransition % 1.0f == 0) {
                        currentLevel += 1;
                        currentLevel %= levels.length+1;
                        levelTransition = 1.0f;
                        advanceLevel = true;
                    }
                }
            };
            this.navigationArrows[0].getComponent(SpriteRenderer.class).flipX(true);
            this.addGameObjectToScene(this.navigationArrows[0]);
            this.addGameObjectToScene(this.navigationArrows[1]);
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(float dt) {
        if(this.levelTransition > 0) {
            this.levelTransition -= dt*2;
            if(this.advanceLevel) {
                for(int i = 0; i < this.levelSelectors.length; i++) {
                    this.levelSelectors[i].setXOffset(-this.currentLevel * 1920f+(float)Math.pow(this.levelTransition, 3)*1920f);
                }
            } else {
                for(int i = 0; i < this.levelSelectors.length; i++) {
                    this.levelSelectors[i].setXOffset(-this.currentLevel * 1920f-(float)Math.pow(this.levelTransition, 3)*1920f);
                }
            }
            if(this.levelTransition < 0) this.levelTransition = 0.0f;
        }
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
