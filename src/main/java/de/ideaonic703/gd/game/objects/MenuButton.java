package de.ideaonic703.gd.game.objects;

import de.ideaonic703.gd.components.Sprite;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.MouseListener;
import de.ideaonic703.gd.engine.Transform;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MenuButton extends GameObject {
    public MenuButton(Transform transform, Sprite sprite) {
        super(transform.withNormalizedOrigin(Transform.ORIGIN_CENTER));
        this.addComponent(new SpriteRenderer(sprite));
    }

    private float hoverScale = 1.0f;
    private final static float fFactor = (float)Math.sqrt(1.2)/1.2f;
    private boolean isClicked = false;
    @Override
    public void update(float dt) {
        super.update(dt);
        if(this.transform.interjects(MouseListener.getPos())) {
            //click
            boolean isClicked = MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT);
            if(!isClicked && this.isClicked) {
                this.onClick();
            }
            this.isClicked = isClicked;
            if(isClicked) {
                this.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.5f, 0.5f, 0.5f, 1f));
            } else {
                this.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1f));
            }
            //hover
            if(hoverScale < 1.5f) {
                hoverScale += Math.min(dt, 0.1f)*2f;
                this.transform.offsetScale(g(hoverScale));
            } else if(this.hoverScale != 1.5f) {
                this.hoverScale = 1.5f;
                this.transform.offsetScale(g(hoverScale));
            }
        } else if(hoverScale > 1.0f) {
            hoverScale -= Math.min(dt, 0.1f)*2f;
            this.transform.offsetScale(g(hoverScale));
        } else if(hoverScale != 1.0f) {
            this.hoverScale = 1.0f;
            this.transform.offsetScale(g(hoverScale));
        }
    }
    private static float f(float x) {
        // f(x) = (x*sqrt(1.2)/1.2)^2
        return (float)Math.pow(x*fFactor, 2);
    }
    private static float g(float x) {
        // g(x)=-3.7 (0.9 (x - 0.7))^(3) + 1 (0.9 (x - 0.7))^(2) + 2.4 * 0.9 (x - 0.7) + 0.3
        return (float)(-3.7 * Math.pow(0.9*(x - 0.7), 3) + 1*Math.pow(0.9*(x - 0.7), 2) + 2.4 * 0.9*(x - 0.7) + 0.3);
    }
    public void onClick() {
        //System.out.println("onClick");
    }
}
