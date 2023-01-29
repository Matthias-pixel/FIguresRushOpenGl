package de.ideaonic703.gd.components;

import de.ideaonic703.gd.engine.renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    private List<Sprite> sprites = new ArrayList<>();
    protected Spritesheet(List<Sprite> sprites) {
        this.sprites = sprites;
    }
    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites) {
        int currentX = 0;
        int currentY = texture.getHeight()-spriteHeight;
        for(int i = 0; i < numSprites; i++) {
            float topY = (currentY+spriteHeight)/(float)texture.getHeight();
            float rightX = (currentX+spriteWidth)/(float)texture.getWidth();
            float leftX = currentX/(float)texture.getWidth();
            float bottomY = currentY/(float)texture.getHeight();

            Vector2f[] texCoords = new Vector2f[]{
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite(texture, texCoords);
            this.sprites.add(sprite);
            currentX += spriteWidth;
            if(currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight;
            }
        }
    }
    public Sprite getSprite(int i) {
        return sprites.get(i);
    }
}
