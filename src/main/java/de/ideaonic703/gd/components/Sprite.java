package de.ideaonic703.gd.components;

import de.ideaonic703.gd.engine.renderer.Texture;
import org.joml.Vector2f;

public class Sprite {
    private boolean rotate = false;
    private boolean debug = false;
    private Vector2f offset;
    private Texture texture;
    private Vector2f[] texCoords;
    private Vector2f clipSize = new Vector2f(1, 1);
    private Vector2f size;
    private float scale;

    public Sprite(Texture texture) {
        this.texture = texture;
        this.texCoords = new Vector2f[]{
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
        };
    }
    public Sprite(Texture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
    }
    public Sprite(Texture texture, Vector2f[] texCoords, Vector2f size) {
        this.texture = texture;
        this.texCoords = texCoords;
        this.size = size;
    }
    public Sprite(Texture texture, Vector2f[] texCoords, Vector2f size, Vector2f offset, boolean rotate) {
        this.texture = texture;
        this.texCoords = texCoords;
        this.size = size;
        this.offset = offset;
        this.rotate = rotate;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    public Texture getTexture() {
        return this.texture;
    }
    public Vector2f[] getTexCoords() {
        float width = texCoords[0].x-texCoords[2].x;
        float height = texCoords[0].y-texCoords[1].y;
        width *= clipSize.x;
        height *= clipSize.y;
        if(rotate) {
            return new Vector2f[] {
                    new Vector2f(texCoords[3].x+width, texCoords[1].y),
                    new Vector2f(texCoords[2].x         , texCoords[2].y),
                    new Vector2f(texCoords[3].x         , texCoords[2].y+height),
                    new Vector2f(texCoords[3].x+width, texCoords[1].y+height)
            };
        } else {
            return new Vector2f[] {
                    new Vector2f(texCoords[3].x+width, texCoords[1].y+height),
                    new Vector2f(texCoords[3].x+width, texCoords[1].y),
                    new Vector2f(texCoords[2].x         , texCoords[2].y),
                    new Vector2f(texCoords[3].x         , texCoords[2].y+height)
            };
        }
    }
    public void clipSize(Vector2f normalizedClipArea) {
        this.clipSize = new Vector2f(normalizedClipArea);
    }

    public Sprite copy() {
        return new Sprite(this.texture, this.texCoords);
    }
    public void setSize(Vector2f size) {
        this.size = size;
    }
    public Vector2f getSize() {
        return this.size == null ? new Vector2f(this.texture.getWidth(), this.texture.getHeight()).mul(scale) : new Vector2f(this.size).mul(scale);
    }
    public void setOffset(Vector2f offset) {
        this.offset = offset;
    }
    public Vector2f getOffset() {
        return this.offset == null ? new Vector2f(0f, 0f) : new Vector2f(this.offset).mul(scale);
    }

    public Vector2f scale(float scale) {
        this.scale = scale;
        return this.getSize();
    }
}
