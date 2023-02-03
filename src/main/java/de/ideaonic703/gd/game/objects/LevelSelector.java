package de.ideaonic703.gd.game.objects;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.ComplexSpritesheet;
import de.ideaonic703.gd.components.FontRenderer;
import de.ideaonic703.gd.components.Sprite;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.Font;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.Scene;
import de.ideaonic703.gd.engine.Transform;
import de.ideaonic703.gd.game.Level;
import org.joml.Vector2f;

public class LevelSelector {
    private int index;
    private Level level;
    private float xOffset = 0;
    private float yOffset = 0.0f;
    private GameObject background, difficultyIcon, title;

    public LevelSelector(int index, Level level) {
        this.index = index;
        this.level = level;
        this.init();
    }
    private void init() {
        ComplexSpritesheet customSpritesheet = AssetPool.getComplexSpritesheet("assets/CustomSpritesheet");
        ComplexSpritesheet spritesheet3 = AssetPool.getComplexSpritesheet("assets/gdresources/GJ_GameSheet03-uhd");
        {
            Sprite backgroundSprite = customSpritesheet.getSprite("level_selection_background.png");
            background = new GameObject(new Transform(new Vector2f(1920f/2f+1920f*this.index, 1080f/2f+190), new Vector2f(1144f, 320f)).withNormalizedOrigin(Transform.ORIGIN_CENTER), 0) {
                private float pxOffset = 0, pyOffset = 0;
                private boolean firstUpdate = true;
                private Vector2f precisePosition;

                @Override
                public void update(float dt) {
                    super.update(dt);
                    if(firstUpdate) {
                        firstUpdate = false;
                        precisePosition = this.transform.getPrecisePositionNoOrigin();
                    }
                    if(xOffset != this.pxOffset || yOffset != this.pyOffset) {
                        this.pxOffset = xOffset;
                        this.pyOffset = yOffset;
                        this.transform.setPrecisePosition(new Vector2f(precisePosition.x+xOffset, precisePosition.y+yOffset));
                    }
                }
            };
            background.addComponent(new SpriteRenderer(backgroundSprite));
        }
        {
            Sprite difficultySprite = spritesheet3.getSprite("diffIcon_0"+this.level.getDifficulty()+"_btn_001.png");
            difficultyIcon = new GameObject(new Transform(new Vector2f(1920f/2f+1920f*this.index-(1144f/2f)+120, 1080f/2f+190), new Vector2f(120f, 120f)).withNormalizedOrigin(Transform.ORIGIN_CENTER), 0) {
                private float pxOffset = 0, pyOffset = 0;
                private boolean firstUpdate = true;
                private Vector2f precisePosition;

                @Override
                public void update(float dt) {
                    super.update(dt);
                    if(firstUpdate) {
                        firstUpdate = false;
                        precisePosition = this.transform.getPrecisePositionNoOrigin();
                    }
                    if(xOffset != this.pxOffset || yOffset != this.pyOffset) {
                        this.pxOffset = xOffset;
                        this.pyOffset = yOffset;
                        this.transform.setPrecisePosition(new Vector2f(precisePosition.x+xOffset, precisePosition.y+yOffset));
                    }
                }
            };
            difficultyIcon.addComponent(new SpriteRenderer(difficultySprite));
        }
        {
            Font titleFont = AssetPool.getFont("assets/gdresources/bigFont-uhd");
            this.title = new GameObject(new Transform(new Vector2f(1920f/2f+1920f*this.index-(1144f/2f)+200, 1080f/2f+170)), 1) {
                private float pxOffset = 0, pyOffset = 0;
                private boolean firstUpdate = true;
                private Vector2f precisePosition;

                @Override
                public void update(float dt) {
                    super.update(dt);
                    if(firstUpdate) {
                        firstUpdate = false;
                        precisePosition = this.transform.getPrecisePositionNoOrigin();
                    }
                    if(xOffset != this.pxOffset || yOffset != this.pyOffset) {
                        this.pxOffset = xOffset;
                        this.pyOffset = yOffset;
                        this.transform.setPrecisePosition(new Vector2f(precisePosition.x+xOffset, precisePosition.y+yOffset));
                    }
                }
            };
            this.title.addComponent(new FontRenderer(this.level.getName(), titleFont));
            this.title.getComponent(FontRenderer.class).setScale(0.83f);
        }
        //diffIcon_01_btn_001.png
    }
    public void setXOffset(float xOffset) {
        this.xOffset = xOffset;
    }
    public void addToScene(Scene scene) {
        scene.addGameObjectToScene(this.background);
        scene.addGameObjectToScene(this.difficultyIcon);
        scene.addGameObjectToScene(this.title);
    }
    public void removeFromScene(Scene scene) {
        scene.removeGameObjectFromScene(this.background);
        scene.removeGameObjectFromScene(this.difficultyIcon);
        scene.removeGameObjectFromScene(this.title);
    }

    public void setYOffset(float yOffset) {
        this.yOffset = yOffset;
    }
}
