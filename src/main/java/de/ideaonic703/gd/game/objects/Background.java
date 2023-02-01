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
    private GameObject background, background2, floorLine;
    private GameObject[] groundTiles, groundTiles2;
    private float groundTilesStartPos, groundTiles2StartPos, floorLineStartPos;

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
                    Color col = Color.getHSBColor((time % 40f) / 40f, 0.89f, 0.9f);
                    this.getComponent(SpriteRenderer.class).setColor(new Vector4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 1.0f));
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
                    Color col = Color.getHSBColor((time % 40f) / 40f, 0.89f, 0.9f);
                    this.getComponent(SpriteRenderer.class).setColor(new Vector4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 1.0f));
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
                        Color col = Color.getHSBColor((time % 40f) / 40f, 0.89f, 0.9f);
                        this.getComponent(SpriteRenderer.class).setColor(new Vector4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 1.0f));
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
                        Color col = Color.getHSBColor((time % 40f) / 40f, 0.89f, 0.9f);
                        this.getComponent(SpriteRenderer.class).setColor(new Vector4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 1.0f));
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
}
