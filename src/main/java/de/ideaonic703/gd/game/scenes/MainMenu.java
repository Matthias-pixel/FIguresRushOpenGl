package de.ideaonic703.gd.game.scenes;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.Sprite;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.*;
import de.ideaonic703.gd.game.objects.Background;
import de.ideaonic703.gd.game.objects.MenuButton;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MainMenu extends Scene {
    private float playSceneTransition = 0.0f;
    private boolean shouldTransition = false;
    private GameObject menuBackground;
    private MenuButton playButton, editorButton;
    private Background background;
    private Vector2f menuBackgroundStartPos, playButtonStartPos, editorButtonStartPos;

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.camera.adjustProjection();
        background = new Background(14, 12);
        background.addTo(this);
        {
            Sprite menuBackgroundSprite = AssetPool.getComplexSpritesheet("assets/CustomSpritesheet").getSprite("main_menu_background.png");
            Vector2f menuBackgroundSize = new Vector2f(1020, 470);
            menuBackground = new GameObject(new Transform(
                    new Vector2f(1920/2f, 1080/2f + 100),
                    menuBackgroundSize).withNormalizedOrigin(Transform.ORIGIN_CENTER), 0);
            menuBackground.addComponent(new SpriteRenderer(menuBackgroundSprite));
            this.addGameObjectToScene(menuBackground);
            this.menuBackgroundStartPos = this.menuBackground.transform.getPrecisePositionNoOrigin();
        }
        {
            Sprite playButtonSprite = AssetPool.getComplexSpritesheet("assets/CustomSpritesheet").getSprite("play_button.png");
            Vector2f playButtonSize = new Vector2f(300, 300);
            playButton = new MenuButton(new Transform(new Vector2f(1920 / 2f, 1080 / 2f + 100), playButtonSize), playButtonSprite) {
                @Override
                public void onClick() {
                    super.onClick();
                    playSceneTransition = 100.0f;
                }
            };
            this.addGameObjectToScene(playButton);
            this.playButtonStartPos = this.playButton.transform.getPrecisePositionNoOrigin();
        }
        {
            Sprite editorButtonSprite = AssetPool.getComplexSpritesheet("assets/CustomSpritesheet").getSprite("edit_button.png");
            Vector2f editorButtonSize = new Vector2f(250f*3f/4f, 250f*3f/4f);
            editorButton = new MenuButton(new Transform(new Vector2f(1920 / 2f + 320, 1080 / 2f+ 100), editorButtonSize), editorButtonSprite);
            this.addGameObjectToScene(editorButton);
            this.editorButtonStartPos = this.editorButton.transform.getPrecisePositionNoOrigin();
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(float dt) {
        //System.out.printf("FPS: %f%n", 1f/dt);
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        if(this.playSceneTransition > 0.0f) {
            this.shouldTransition = true;
            float originalTransition = this.playSceneTransition;
            float inverseTransition = 100f-this.playSceneTransition;
            float squareTransition = (float)Math.pow(inverseTransition*0.1f, 2f);
            float cubeTransition = (float)Math.pow(inverseTransition*0.1f, 3f)/10f;
            float inverseSquareTransition = (float)Math.pow(originalTransition*0.1f, 2f);
            float inverseCubeTransition = (float)Math.pow(originalTransition*0.1f, 3f)/10f;

            this.menuBackground.transform.setPrecisePosition(this.menuBackgroundStartPos.add(0, squareTransition*3.5f));
            this.menuBackground.transform.offsetScale(inverseSquareTransition/100f);
            this.menuBackground.getComponent(SpriteRenderer.class).setColor(new Vector4f(1.0f, 1.0f, 1.0f, inverseCubeTransition/100f));
            this.editorButton.transform.setPrecisePosition(this.editorButtonStartPos.add(-squareTransition*5f, squareTransition*8));
            this.editorButton.transform.offsetScale(originalTransition/100f);
            this.editorButton.getComponent(SpriteRenderer.class).setColor(new Vector4f(1.0f, 1.0f, 1.0f, inverseCubeTransition/100f));
            this.playButton.transform.setPrecisePosition(this.playButtonStartPos.add(0, squareTransition*12));
            this.playButton.transform.offsetScale(originalTransition/100f);
            this.playButton.getComponent(SpriteRenderer.class).setColor(new Vector4f(1.0f, 1.0f, 1.0f, inverseCubeTransition/100f));
            this.background.setGroundTileOffset(-cubeTransition*1.7f);
            this.background.floorLine.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, originalTransition/100f));

            this.playSceneTransition -= dt*100;
        } else if(this.playSceneTransition < 30.0f && this.shouldTransition) {
            this.playSceneTransition = 30.0f;
        }
        if(this.playSceneTransition == 30.0f && this.shouldTransition) {
            LevelSelectScreen lss = new LevelSelectScreen(this.background);
            background.addTo(lss);
            this.background.removeFrom(this);
            this.removeGameObjectFromScene(this.menuBackground);
            this.removeGameObjectFromScene(this.playButton);
            this.removeGameObjectFromScene(this.editorButton);
            this.removeGameObjectFromScene(this.menuBackground);
            Window.changeScene(lss);
        }
        this.renderer.render();
    }
}
