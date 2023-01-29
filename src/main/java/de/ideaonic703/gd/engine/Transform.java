package de.ideaonic703.gd.engine;

import org.joml.Vector2f;
import org.joml.Vector2i;

public class Transform {
    public Vector2i position;
    public Vector2f offset;
    public Vector2f scale;
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

    public Vector2f getPercisePosition() {
        return new Vector2f(position.x+offset.x, position.y+offset.y);
    }
    public Transform copy() {return new Transform(new Vector2i(this.position), new Vector2f(this.offset), new Vector2f(this.scale));}
    public void copy(Transform to) {to.position.set(this.position); to.offset.set(this.offset); to.scale.set(this.scale);}
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof Transform t)) return false;
        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }
    public void setPosition(Vector2f position) {
        this.position = new Vector2i((int)position.x, (int)position.y);
        this.offset = new Vector2f(position.x-this.position.x, position.y-this.position.y);
    }

    @Override
    public String toString() {
        return String.format("(x: %f, y: %f, width: %f, height: %f)", position.x, position.y, scale.x, scale.y);
    }
}
