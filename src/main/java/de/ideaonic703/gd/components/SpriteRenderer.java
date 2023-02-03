package de.ideaonic703.gd.components;

import de.ideaonic703.gd.engine.Component;
import de.ideaonic703.gd.engine.Transform;
import de.ideaonic703.gd.engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Objects;

public class SpriteRenderer extends Component {
    private Vector4f color;
    private Sprite sprite;
    private Transform lastTransform;
    private Vector2f offset = new Vector2f(), scaleOverride;
    private boolean dirty;

    public SpriteRenderer(Sprite sprite, Vector4f color) {
        this.dirty = true;
        this.sprite = sprite;
        this.color = color;
    }
    public SpriteRenderer(Vector4f color) {
        this(new Sprite(null), color);
    }
    public SpriteRenderer(Sprite sprite) {
        this(sprite, new Vector4f(1, 1, 1, 1));
    }

    @Override
    public void start() {
        super.start();
        this.lastTransform = gameObject.transform.copy();
    }
    @Override
    public void update(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            this.dirty = true;
        }
    }
    public Vector4f getColor() {
        return color;
    }
    public Texture getTexture() {
        return sprite.getTexture();
    }
    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }
    public void setSprite(Sprite sprite) {
        //if(Objects.equals(sprite, this.sprite)) return;
        this.dirty = true;
        this.sprite = sprite;
    }
    public void setColor(Vector4f color) {
        if(Objects.equals(color, this.color)) return;
        this.dirty = true;
        this.color.set(color);
    }
    public void setOffset(Vector2f offset) {this.offset = new Vector2f(offset);}
    public Vector2f getOffset() {return new Vector2f(this.offset);}
    public void setScaleOverride(Vector2f scaleOverride) {this.scaleOverride = new Vector2f(scaleOverride);}
    public Vector2f getScaleOverride() {return new Vector2f(this.scaleOverride);}
    public Transform getTransform() {
        Transform transform = this.gameObject.transform.copy();
        Vector2f pos = transform.getPrecisePositionNoOrigin();
        pos.add(this.offset);
        transform.setPrecisePosition(pos);
        if(this.scaleOverride != null) transform.setScale(this.scaleOverride);
        return transform;
    }
    public boolean isDirty() {return this.dirty;}
    public void makeDirty() {this.dirty = true;}
    public void clean() {this.dirty = false;}
}
