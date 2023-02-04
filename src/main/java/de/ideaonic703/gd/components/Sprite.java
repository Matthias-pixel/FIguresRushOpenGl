package de.ideaonic703.gd.components;

import de.ideaonic703.gd.engine.renderer.Texture;
import org.joml.Vector2f;

public class Sprite {
    private Texture texture;
    private Vector2f[] texCoords;
    private Vector2f clipSize = new Vector2f(1, 1);

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
    public Texture getTexture() {
        return this.texture;
    }
    public Vector2f[] getTexCoords() {
        float width = texCoords[0].x-texCoords[2].x;
        float height = texCoords[0].y-texCoords[1].y;
        width *= clipSize.x;
        height *= clipSize.y;
        return new Vector2f[] {
                new Vector2f(texCoords[3].x+width, texCoords[1].y+height),
                new Vector2f(texCoords[3].x+width, texCoords[1].y),
                new Vector2f(texCoords[2].x         , texCoords[2].y),
                new Vector2f(texCoords[3].x         , texCoords[2].y+height)
        };
    }
    public void clipSize(Vector2f normalizedClipArea) {
        this.clipSize = new Vector2f(normalizedClipArea);
    }

    public Sprite copy() {
        return new Sprite(this.texture, this.texCoords);
    }
}
