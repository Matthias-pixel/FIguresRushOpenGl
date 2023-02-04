package de.ideaonic703.gd.game.objects;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.ComplexSpritesheet;
import de.ideaonic703.gd.components.FontRenderer;
import de.ideaonic703.gd.components.Sprite;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.Scene;
import de.ideaonic703.gd.engine.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class BigProgressBar {
    private float xOffset = 0.0f, yOffset = 0.0f;
    private float completion;
    private GameObject levelCompletionBackground, levelCompletionFill, practiceCompletionBackground, practiceCompletionFill;
    private GameObject title, practiceTitle, completionText, practiceCompletionText;
    private int index;
    private float practiceCompletion;

    public BigProgressBar(int index, float completion, float practiceCompletion) {
        this.practiceCompletion = practiceCompletion;
        this.completion = completion;
        this.index = index;
        this.init();
    }
    private void init() {
        ComplexSpritesheet customSpritesheet = AssetPool.getComplexSpritesheet("assets/CustomSpritesheet");
        {
            Sprite completionBackgroundSprite = customSpritesheet.getSprite("level_completion_background.png");
            levelCompletionBackground = new GameObject(new Transform(new Vector2f(1920f/2f+1920f*this.index, 440f), new Vector2f(1142f, 67f)).withNormalizedOrigin(Transform.ORIGIN_CENTER), 0) {
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
            levelCompletionBackground.addComponent(new SpriteRenderer(completionBackgroundSprite, new Vector4f(0, 0, 0, 0.5f)));
        }
        {
            Sprite completionFillSprite = customSpritesheet.getSprite("level_completion_fill.png").copy();
            completionFillSprite.clipSize(new Vector2f(this.completion, 1f));
            levelCompletionFill = new GameObject(new Transform(new Vector2f(1920f/2f+1920f*this.index-1142f/2, 407f), new Vector2f(1142f*this.completion, 67f)), 0) {
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
            levelCompletionFill.addComponent(new SpriteRenderer(completionFillSprite, new Vector4f(0f, 1f, 0f, 1f)));
        }
        {
            Sprite completionBackgroundSprite = customSpritesheet.getSprite("level_completion_background.png");
            practiceCompletionBackground = new GameObject(new Transform(new Vector2f(1920f/2f+1920f*this.index, 440f-168f), new Vector2f(1142f, 67f)).withNormalizedOrigin(Transform.ORIGIN_CENTER), 0) {
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
            practiceCompletionBackground.addComponent(new SpriteRenderer(completionBackgroundSprite, new Vector4f(0, 0, 0, 0.5f)));
        }
        {
            Sprite completionFillSprite = customSpritesheet.getSprite("level_completion_fill.png").copy();
            completionFillSprite.clipSize(new Vector2f(this.practiceCompletion, 1f));
            practiceCompletionFill = new GameObject(new Transform(new Vector2f(1920f/2f+1920f*this.index-1142f/2, 407f-168f), new Vector2f(1142f*this.practiceCompletion, 67f)), 0) {
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
            practiceCompletionFill.addComponent(new SpriteRenderer(completionFillSprite, new Vector4f(0f, 1f, 1f, 1f)));
        }
        {
            this.title = new GameObject(new Transform(new Vector2f(795f+1920f*this.index, 485f)), 0) {
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
            this.title.addComponent(new FontRenderer("Normal Mode", AssetPool.getFont("assets/gdresources/bigFont-uhd")));
            this.title.getComponent(FontRenderer.class).setScale(0.4f);
        }
        {
            this.practiceTitle = new GameObject(new Transform(new Vector2f(768f+1920f*this.index, 485f-168f)), 0) {
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
            this.practiceTitle.addComponent(new FontRenderer("Practice Mode", AssetPool.getFont("assets/gdresources/bigFont-uhd")));
            this.practiceTitle.getComponent(FontRenderer.class).setScale(0.4f);
        }
        {
            this.completionText = new GameObject(new Transform(new Vector2f(795f+1920f*this.index+112f, 414f)), 0) {
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
            this.completionText.addComponent(new FontRenderer((int)(this.completion*100)+"%", AssetPool.getFont("assets/gdresources/bigFont-uhd")));
            this.completionText.getComponent(FontRenderer.class).setScale(0.45f);
        }
        {
            this.practiceCompletionText = new GameObject(new Transform(new Vector2f(795f+1920f*this.index+112f, 414f-168f)), 0) {
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
            this.practiceCompletionText.addComponent(new FontRenderer((int)(this.practiceCompletion*100)+"%", AssetPool.getFont("assets/gdresources/bigFont-uhd")));
            this.practiceCompletionText.getComponent(FontRenderer.class).setScale(0.45f);
        }
    }

    public void setXOffset(float xOffset) {
        this.xOffset = xOffset;
    }
    public void addToScene(Scene scene) {
        scene.addGameObjectToScene(this.levelCompletionBackground);
        scene.addGameObjectToScene(this.levelCompletionFill);
        scene.addGameObjectToScene(this.practiceCompletionBackground);
        scene.addGameObjectToScene(this.practiceCompletionFill);
        scene.addGameObjectToScene(this.title);
        scene.addGameObjectToScene(this.practiceTitle);
        scene.addGameObjectToScene(this.completionText);
        scene.addGameObjectToScene(this.practiceCompletionText);
    }
    public void removeFromScene(Scene scene) {
        scene.removeGameObjectFromScene(this.levelCompletionBackground);
        scene.removeGameObjectFromScene(this.levelCompletionFill);
        scene.removeGameObjectFromScene(this.practiceCompletionBackground);
        scene.removeGameObjectFromScene(this.practiceCompletionFill);
        scene.removeGameObjectFromScene(this.title);
        scene.removeGameObjectFromScene(this.practiceTitle);
        scene.removeGameObjectFromScene(this.completionText);
        scene.removeGameObjectFromScene(this.practiceCompletionText);
    }
    public void setYOffset(float yOffset) {
        this.yOffset = yOffset;
    }

}
