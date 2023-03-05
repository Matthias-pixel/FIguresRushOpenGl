package de.ideaonic703.gd.game.scenes;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.ComplexSpritesheet;
import de.ideaonic703.gd.components.Sprite;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.Window;
import de.ideaonic703.gd.engine.*;
import de.ideaonic703.gd.engine.renderer.RenderBatch;
import de.ideaonic703.gd.game.Level;
import de.ideaonic703.gd.game.objects.Background;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.List;

import static java.lang.Math.PI;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Playing extends Scene {
    public final static float NDEG = (float)PI/2f;
    public final static float FDEG = (float)PI/4f;
    private Background background;
    private Level level;
    private int playerType = 5;
    private Vector4f playerColorForeground = new Vector4f(20f/255f, 50f/255f, 150f/255f, 1f);
    private Vector4f playerColorBackground = new Vector4f(20f/255f, 150f/255f, 50f/255f, 1f);
    private GameObject playerForeground, playerBackground;
    ComplexSpritesheet playerSpritesheet;
    Sprite backgroundSprite;
    private final float scaleFactor = 107f;
    private final float originalScaleFactor = 120f;
    private final float scaledWith = 1920f/scaleFactor;
    private final float scaledHeight = 1080f/scaleFactor;
    private final float scrollSpeed = 1000f/scaleFactor;
    private final float secondsPerBlock = scaleFactor/1000f;
    private float levelTime = 0f;
    private float yPos = 0;
    private float lastUpdate = 1.0f;
    private final float PLAYER_X = 200f;
    private boolean onGround = true;
    private float ySpeed = 0f;
    private float yAcceleration = -1.2f;
    private float airTime = 0;
    private float rotation = 0f, rotationOffset = 0f;

    public Playing(Level level) {
        this.level = level;
    }
    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.camera.adjustProjection();
        this.camera2 = new Camera(new Vector2f(0, -2.81f));
        this.camera2.adjustProjection(scaledWith, scaledHeight);
        background = new Background(14, 12);
        background.setColor(new Color(180, 80, 230));
        background.addTo(this);
        playerSpritesheet = AssetPool.getComplexSpritesheet("assets/gdresources/GJ_GameSheet02-uhd");
        if(playerType <= 9) {
            backgroundSprite = playerSpritesheet.getSprite("player_0"+playerType+"_2_001.png");
            backgroundSprite.setDebug(true);
            Vector2f size = backgroundSprite.scale(scaleFactor/originalScaleFactor);
            playerBackground = new GameObject(new Transform(new Vector2f(PLAYER_X+scaleFactor/2f+backgroundSprite.getOffset().x, 304f+scaleFactor/2f+backgroundSprite.getOffset().y), size).withNormalizedOrigin(Transform.ORIGIN_CENTER), 0);
            playerBackground.addComponent(new SpriteRenderer(backgroundSprite, playerColorBackground));
            this.addGameObjectToScene(playerBackground);
            playerForeground = new GameObject(new Transform(new Vector2f(PLAYER_X, 304f), new Vector2f(scaleFactor, scaleFactor)), 0);
            playerForeground.addComponent(new SpriteRenderer(playerSpritesheet.getSprite("player_0"+playerType+"_001.png"), playerColorForeground));
            this.addGameObjectToScene(playerForeground);
        } else {
            backgroundSprite = playerSpritesheet.getSprite("player_"+playerType+"_2_001.png");
            backgroundSprite.setDebug(true);
            Vector2f size = backgroundSprite.scale(scaleFactor/originalScaleFactor);
            playerBackground = new GameObject(new Transform(new Vector2f(PLAYER_X+scaleFactor/2f+backgroundSprite.getOffset().x, 304f+scaleFactor/2f+backgroundSprite.getOffset().y), size).withNormalizedOrigin(Transform.ORIGIN_CENTER), 0);
            playerBackground.addComponent(new SpriteRenderer(backgroundSprite, playerColorBackground));
            this.addGameObjectToScene(playerBackground);
            playerForeground = new GameObject(new Transform(new Vector2f(PLAYER_X, 304f), new Vector2f(scaleFactor, scaleFactor)), 0);
            playerForeground.addComponent(new SpriteRenderer(playerSpritesheet.getSprite("player_"+playerType+"_001.png"), playerColorForeground));
            this.addGameObjectToScene(playerForeground);
        }
        playerBackground.transform.setRotationSlot(1);
        playerForeground.transform.setRotationSlot(1);
        List<GameObject> gameObjects = this.level.getObjectsAt(0, (int)scaledWith+1);
        loadNewGameObjects(gameObjects);
    }
    private void loadNewGameObjects(List<GameObject> gameObjects) {
        for(GameObject go : gameObjects) {
            if(!this.gameObjects.contains(go)) {
                go.init();
                this.addGameObjectToScene(go);
            }
        }
    }
    private void freeGameObjects(List<GameObject> gameObjects) {
        for(int i = 0; i < this.gameObjects.size(); i++) {
            GameObject go = this.gameObjects.get(i);
            if (!gameObjects.contains(go) && !this.background.hasGameObject(go)) {
                this.removeGameObjectFromScene(go);
                i--;
            }
        }
    }

    @Override
    public void update(float dt) {
        levelTime += dt;
        lastUpdate += dt;
        float x = levelTime*scrollSpeed;
        float y = -2.81f+yPos;
        if(lastUpdate >= secondsPerBlock) {
            List<GameObject> gameObjects = this.level.getObjectsAt((int)x, (int) Math.ceil(scaledWith+1));
            freeGameObjects(gameObjects);
            loadNewGameObjects(gameObjects);
            lastUpdate = 0f;
        }
        List<GameObject> objects = this.level.getObjectsAt((int)(x), 2);
        boolean xCollision = false;
        for(GameObject object : objects) {
            if(object.transform.interjects(new Vector2f(x+0.2f, yPos+0.2f), new Vector2f(0.6f, 0.6f))) {
                xCollision = true;
                break;
            }
        }
        if(xCollision) {
            Window.changeScene(new Playing(this.level));
        }
        ySpeed += yAcceleration*dt;
        boolean yCollision = false;
        GameObject collisionWith = null;
        if(yPos+ySpeed < 0) yCollision = true;
        else {
            for(GameObject object : objects) {
                if(object.transform.interjects(new Vector2f(x+0.2f, (yPos+ySpeed)), new Vector2f(0.6f, 1f))) {
                    yCollision = true;
                    collisionWith = object;
                    break;
                }
            }
        }
        boolean touchedGroundNow = !onGround && yCollision;
        onGround = yCollision;
        if(!yCollision) {
            yPos += ySpeed;
            airTime += dt;
            rotation = 2f*(float)PI-2f*(float)PI*airTime*0.9f;
        } else {
            airTime = 0f;
            ySpeed = 0;
            if(collisionWith != null) {
                yPos = collisionWith.transform.getPrecisePositionNoOrigin().y+collisionWith.transform.getScale().y;
            } else {
                yPos = 0;
            }
            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                ySpeed = 0.33f;
            }
            float rotationModulo = rotationOffset%NDEG;
            System.out.println(rotationModulo);
            if(rotationModulo > 0.1f && rotationModulo < NDEG-0.1f) {
                if (rotationModulo < FDEG) {
                    rotationOffset -= 10f * dt;
                    if (rotationOffset % NDEG < 11f * dt) {
                        rotationOffset -= rotationModulo;
                    }
                } else {
                    rotationOffset += 10f * dt;
                    if (NDEG - (rotationOffset % NDEG) < 11f * dt) {
                        rotationOffset += NDEG - rotationModulo;
                    }
                }
            }
            rotationModulo = rotationOffset%NDEG;
            if (rotationModulo < FDEG) {
                if (rotationOffset % NDEG < 11f * dt) {
                    rotationOffset -= rotationModulo;
                }
            } else {
                if (NDEG - (rotationOffset % NDEG) < 11f * dt) {
                    rotationOffset += NDEG - rotationModulo;
                }
            }

        }
        if(touchedGroundNow) {
            rotationOffset += rotation;
            rotationOffset %= 2*(float)PI;
            rotation = 0f;
        }
        playerForeground.transform.setPrecisePosition(new Vector2f(PLAYER_X, 304f+yPos*scaleFactor));
        playerBackground.transform.setPrecisePosition(new Vector2f(PLAYER_X+scaleFactor/2f+backgroundSprite.getOffset().x, 304f+yPos*scaleFactor+scaleFactor/2+backgroundSprite.getOffset().y));
        RenderBatch.ROTATION_SLOTS_ANGLE[1] = rotationOffset+rotation;
        RenderBatch.ROTATION_SLOTS_CENTER[1] = playerForeground.transform.getPrecisePosition().add(playerForeground.transform.getScale().mul(0.5f));
        playerForeground.getComponent(SpriteRenderer.class).makeDirty();
        playerBackground.getComponent(SpriteRenderer.class).makeDirty();
        this.camera2.position.set(new Vector2f(x-PLAYER_X/scaleFactor, -2.81f));
        //this.background.setGroundTileOffset(-yPos*scaleFactor);
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }
}
