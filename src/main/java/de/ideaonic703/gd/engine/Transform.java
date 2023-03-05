package de.ideaonic703.gd.engine;

import org.joml.Vector2f;
import org.joml.Vector2i;

public class Transform {
    public final static Vector2f ORIGIN_CENTER = new Vector2f(0.5f, 0.5f);
    public final static Vector2f ORIGIN_BOTTOM_LEFT = new Vector2f(0f, 0f);
    public final static Vector2f ORIGIN_TOP_LEFT = new Vector2f(0f, 1f);
    public final static Vector2f ORIGIN_BOTTOM_RIGHT = new Vector2f(1f, 0f);
    public final static Vector2f ORIGIN_TOP_RIGHT = new Vector2f(1f, 1f);

    private Vector2i position;
    private Vector2f offset;
    private Vector2f scale;
    private Vector2f origin = ORIGIN_BOTTOM_LEFT;
    private float scaleOffset = 1.0f;
    private int rotationSlot = 0;

    public Transform(Vector2i position, Vector2f offset, Vector2f scale) {
        this.position = position;
        this.offset = offset;
        this.scale = scale;
    }
    public Transform(Vector2i position, Vector2f scale) {
        this.position = position;
        this.offset = new Vector2f();
        this.scale = scale;
    }
    public Transform(Vector2i position) {
        this.position = position;
        this.offset = new Vector2f();
        this.scale = new Vector2f(1.0f, 1.0f);
    }
    public Transform(Vector2f position, Vector2f scale) {
        this.position = new Vector2i((int)position.x, (int)position.y);
        this.offset = new Vector2f(position.x-this.position.x, position.y-this.position.y);
        this.scale = scale;
    }
    public Transform(Vector2f position) {
        this(position, new Vector2f());
    }
    public Transform() {
        this(new Vector2f(), new Vector2f());
    }
    public Vector2i getPosition() {
        Vector2f scaledOrigin = new Vector2f(this.origin).mul(this.getScale());
        return new Vector2i(this.position).sub(new Vector2i((int) scaledOrigin.x, (int) scaledOrigin.y));
    }
    public void setPosition(Vector2i position) {
        this.position = new Vector2i(position);
    }
    public Vector2f getOffset() {
        //Vector2f scaledOffset = new Vector2f(this.offset).sub(new Vector2f(this.origin).mul(this.getScale()));
        return new Vector2f(this.offset);
    }
    public void setOffset(Vector2f offset) {
        assert offset.absolute().x < 1 && offset.absolute().y < 1 : "Offset should only be used for fine positioning. For values larger or equal than 1.0f use setPercisePosition()";
        this.offset = new Vector2f(offset);
    }
    public Vector2f getPrecisePosition() {
        Vector2i position = getPosition();
        Vector2f offset = getOffset();
        return new Vector2f(position.x+offset.x, position.y+offset.y);
    }
    public Vector2f getPrecisePositionNoOrigin() {
        return new Vector2f(position.x+offset.x, position.y+offset.y);
    }
    public void setPrecisePosition(Vector2f position) {
        this.position = new Vector2i((int)position.x, (int)position.y);
        this.offset = new Vector2f(position.x-this.position.x, position.y-this.position.y);
    }
    public void setScale(Vector2f scale) {
        this.scale = new Vector2f(scale);
    }
    public Vector2f getScale() {
        return new Vector2f(this.scale).mul(this.scaleOffset);
    }
    public Transform copy() {Transform n = new Transform(); this.copy(n); return n;}
    public void copy(Transform to) {to.position = new Vector2i(this.position); to.offset = new Vector2f(this.offset); to.scale = new Vector2f(this.scale); to.origin = new Vector2f(this.origin); to.scaleOffset = this.scaleOffset;}
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof Transform t)) return false;
        return t.position.equals(this.position) && t.scale.equals(this.scale) && t.origin.equals(this.origin) && (t.scaleOffset == this.scaleOffset);
    }
    public Transform withNormalizedOrigin(Vector2f normalizedOrigin) {
        this.origin = new Vector2f(normalizedOrigin);
        return this;
    }
    public boolean interjects(Vector2f pos) {
        Vector2f my = getPrecisePosition();
        Vector2f mySize = getScale();
        boolean interjects = pos.x > my.x && pos.x < (my.x+mySize.x);
        interjects = interjects && (pos.y > my.y && pos.y < (my.y+mySize.y));
        return interjects;
    }
    public boolean interjects(Vector2f pos, Vector2f size) {
        Vector2f my = getPrecisePosition();
        Vector2f mySize = getScale();
        boolean interjects = pos.x+size.x > my.x && pos.x < (my.x+mySize.x);
        interjects = interjects && (pos.y+size.y > my.y && pos.y < (my.y+mySize.y));
        return interjects;
    }
    public void offsetScale(float scaleOffset) {
        this.scaleOffset = scaleOffset;
    }

    @Override
    public String toString() {
        Vector2f position = getPrecisePosition();
        return String.format("(x: %f, y: %f, width: %f, height: %f)", position.x, position.y, scale.x, scale.y);
    }

    public void setPrecisePositionNoOrigin(Vector2f position) {
        Vector2f scaledOrigin = new Vector2f(this.origin).mul(this.getScale());
        this.setPrecisePosition(new Vector2f(position).add(scaledOrigin));
    }
    public void setRotationSlot(int slot) {
        this.rotationSlot = slot;
    }
    public int getRotationSlot() {
        return this.rotationSlot;
    }
}
