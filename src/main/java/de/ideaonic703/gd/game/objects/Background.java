package de.ideaonic703.gd.game.objects;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.ComplexSpritesheet;
import de.ideaonic703.gd.components.Sprite;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.Scene;
import de.ideaonic703.gd.engine.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.*;

public class Background {
    private int bgType, groundType;
    private String bgTypeString, groundTypeString;
    private ComplexSpritesheet sprites;
    public GameObject background, background2, floorLine;
    public GameObject[] groundTiles, groundTiles2;
    private float groundTilesStartPos, groundTiles2StartPos, floorLineStartPos;
    private Color colorOverride = null;

    public Background(int bgType, int groundType) {
        if(bgType < 1 || bgType > 20) bgType = 1;
        this.bgType = bgType;
        if(bgType > 9) this.bgTypeString = String.valueOf(bgType);
        else this.bgTypeString = "0"+bgType;
        if(groundType < 1 || groundType > 20) groundType = 1;
        this.groundType = groundType;
        if(groundType > 9) this.groundTypeString = String.valueOf(groundType);
        else this.groundTypeString = "0"+groundType;
        sprites = AssetPool.getComplexSpritesheet("assets/gdresources/GJ_GameSheet-uhd");

        {
            this.background = new GameObject(new Transform(new Vector2f(0, 0), new Vector2f(2048f, 2048f)), -3) {
                private float time = 0;

                @Override
                public void update(float dt) {
                    super.update(dt);
                    this.transform.setPrecisePosition(new Vector2f((-time * 100) % this.transform.getScale().x, 0f));
                    time += dt;
                    if(colorOverride == null) {
                        Color col = Color.getHSBColor((time % 40f) / 40f, 0.89f, 0.9f);
                        this.getComponent(SpriteRenderer.class).setColor(new Vector4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 1.0f));
                    }
                }
            };
            background.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/gdresources/game_bg_"+ this.bgTypeString +"_001-uhd.png")), new Vector4f(1.0f, 0.0f, 1.0f, 1.0f)));
        }
        {
            this.background2 = new GameObject(new Transform(new Vector2f(2048f, 0), new Vector2f(2048f, 2048f)), -3) {
                private float time = 0;

                @Override
                public void update(float dt) {
                    super.update(dt);
                    this.transform.setPrecisePosition(new Vector2f(2048f - ((time * 100) % this.transform.getScale().x), 0f));
                    time += dt;
                    if(colorOverride == null) {
                        Color col = Color.getHSBColor((time % 40f) / 40f, 0.89f, 0.9f);
                        this.getComponent(SpriteRenderer.class).setColor(new Vector4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 1.0f));
                    }
                }
            };
            background2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/gdresources/game_bg_"+ this.bgTypeString +"_001-uhd.png")), new Vector4f(1.0f, 0.0f, 1.0f, 1.0f)));
        }
        {
            int textureHeight = AssetPool.getTexture("assets/gdresources/groundSquare_"+ this.groundTypeString +"_001-uhd.png").getHeight();
            int textureWidth = AssetPool.getTexture("assets/gdresources/groundSquare_"+ this.groundTypeString +"_001-uhd.png").getWidth();
            this.groundTiles = new GameObject[1920/textureWidth+2];
            for (int i = 0; i < groundTiles.length; i++) {
                final int finalI = i;
                groundTiles[i] = new GameObject(new Transform(new Vector2f(finalI * textureWidth, 304f-textureHeight), new Vector2f(textureWidth, textureHeight)), -2) {
                    private float time = 0;

                    @Override
                    public void update(float dt) {
                        super.update(dt);
                        this.transform.setPrecisePosition(new Vector2f((finalI * textureWidth) - (time * 1000) % this.transform.getScale().x, this.transform.getPrecisePositionNoOrigin().y));
                        time += dt;
                        if(colorOverride == null) {
                            Color col = Color.getHSBColor((time % 40f) / 40f, 0.89f, 0.9f);
                            this.getComponent(SpriteRenderer.class).setColor(new Vector4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 1.0f));
                        }
                    }
                };
                groundTiles[i].addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/gdresources/groundSquare_"+ this.groundTypeString +"_001-uhd.png")), new Vector4f(1.0f, 0.0f, 1.0f, 1.0f)));
                groundTilesStartPos = groundTiles[i].transform.getPrecisePositionNoOrigin().y;
            }
        }
        if(this.groundType >= 8) {
            int textureHeight = AssetPool.getTexture("assets/gdresources/groundSquare_"+ this.groundTypeString +"_2_001-uhd.png").getHeight();
            int textureWidth = AssetPool.getTexture("assets/gdresources/groundSquare_"+ this.groundTypeString +"_2_001-uhd.png").getWidth();
            this.groundTiles2 = new GameObject[1920/textureWidth+2];
            for (int i = 0; i < groundTiles2.length; i++) {
                final int finalI = i;
                groundTiles2[i] = new GameObject(new Transform(new Vector2f(finalI * textureWidth, 304f - textureHeight), new Vector2f(textureWidth, textureHeight)), -1) {
                    private float time = 0;

                    @Override
                    public void update(float dt) {
                        super.update(dt);
                        this.transform.setPrecisePosition(new Vector2f((finalI * textureWidth) - (time * 1000) % this.transform.getScale().x, this.transform.getPrecisePositionNoOrigin().y));
                        time += dt;
                        if(colorOverride == null) {
                            Color col = Color.getHSBColor((time % 40f) / 40f, 0.89f, 0.9f);
                            this.getComponent(SpriteRenderer.class).setColor(new Vector4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 1.0f));
                        }
                    }
                };
                groundTiles2[i].addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/gdresources/groundSquare_"+ this.groundTypeString +"_2_001-uhd.png")), new Vector4f(1.0f, 0.0f, 1.0f, 1.0f)));
                groundTiles2StartPos = groundTiles2[i].transform.getPrecisePositionNoOrigin().y;
            }
        }
        {
            this.floorLine = new GameObject(new Transform(new Vector2f(0, 300f), new Vector2f(1776f, 3f)), 0);
            floorLine.addComponent(new SpriteRenderer(sprites.getSprite("floorLine_001.png")));
            this.floorLineStartPos = this.floorLine.transform.getPrecisePositionNoOrigin().y;
        }
    }
    public void setColor(Color color) {
        this.colorOverride = color;
        this.background.getComponent(SpriteRenderer.class).setColor(new Vector4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f));
        this.background2.getComponent(SpriteRenderer.class).setColor(new Vector4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f));
        for(GameObject groundTile : this.groundTiles) {
            groundTile.getComponent(SpriteRenderer.class).setColor(new Vector4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f));
        }
        if(groundType >= 8) {
            for(GameObject groundTile : this.groundTiles2) {
                groundTile.getComponent(SpriteRenderer.class).setColor(new Vector4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f));
            }
        }
    }
    public void addTo(Scene scene) {
        scene.addGameObjectToScene(this.background);
        scene.addGameObjectToScene(this.background2);
        scene.addGameObjectToScene(this.floorLine);
        for(GameObject tile : this.groundTiles) {
            scene.addGameObjectToScene(tile);
        }
        if(groundType >= 8) {
            for(GameObject tile : this.groundTiles2) {
                scene.addGameObjectToScene(tile);
            }
        }
    }
    public void removeFrom(Scene scene) {
        scene.removeGameObjectFromScene(this.background);
        scene.removeGameObjectFromScene(this.background2);
        scene.removeGameObjectFromScene(this.floorLine);
        for(GameObject tile : this.groundTiles) {
            scene.removeGameObjectFromScene(tile);
        }
        if(groundType >= 8) {
            for(GameObject tile : this.groundTiles2) {
                scene.removeGameObjectFromScene(tile);
            }
        }
    }
    public void setGroundTileOffset(float yOffset) {
        for(GameObject tile : this.groundTiles) {
            tile.transform.setPrecisePosition(new Vector2f(tile.transform.getPrecisePosition().x, this.groundTilesStartPos+yOffset));
        }
        if(groundType >= 8) {
            for(GameObject tile : this.groundTiles2) {
                tile.transform.setPrecisePosition(new Vector2f(tile.transform.getPrecisePosition().x, this.groundTiles2StartPos+yOffset));
            }
        }
        this.floorLine.transform.setPrecisePosition(new Vector2f(this.floorLine.transform.getPrecisePosition().x, this.floorLineStartPos+yOffset));
    }
    public boolean hasGameObject(GameObject go) {
        if(this.background == go) return true;
        if(this.background2 == go) return true;
        for(GameObject tile : this.groundTiles) {
            if(tile == go) return true;
        }
        if(groundType >= 8) {
            for(GameObject tile : this.groundTiles2) {
                if(tile == go) return true;
            }
        }
        return false;
    }
}
