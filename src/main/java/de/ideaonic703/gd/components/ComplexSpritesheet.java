package de.ideaonic703.gd.components;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.engine.renderer.Texture;
import org.apache.commons.configuration2.plist.XMLPropertyListConfiguration;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ComplexSpritesheet {
    private Map<String, Sprite> data;
    private String pixelFormat;
    private boolean premultiplyAlpha;
    private String realTextureFileName;
    private Vector2i size;
    private String textureFileName;

    private ComplexSpritesheet(Map<String, Sprite> data) {
        this.data = data;
    }
    public static ComplexSpritesheet loadFromFile(String filename) {
        for(int tries = 10; tries >= 0; tries--) {
            try {
                ComplexSpritesheet spriteSheet = new ComplexSpritesheet(new HashMap<>());
                Map<String, SpriteMeta> spriteMap = new HashMap<>();
                Texture texture = AssetPool.getTexture(filename + ".png", false);

                XMLPropertyListConfiguration plist = new XMLPropertyListConfiguration();
                plist.read(new FileReader(filename + ".plist"));
                Iterator<String> iterator = plist.getKeys();
                while(iterator.hasNext()) {
                    String key = iterator.next();
                    if(key.startsWith("metadata.")) {
                        String metadataName = key.split("metadata.")[1];
                        switch(metadataName) {
                            case "pixelFormat": spriteSheet.pixelFormat = plist.get(String.class, key); break;
                            case "premultiplyAlpha": spriteSheet.premultiplyAlpha = plist.get(Boolean.class, key); break;
                            case "realTextureFileName": spriteSheet.realTextureFileName = plist.get(String.class, key); break;
                            case "size": spriteSheet.size = vec2iFromString(plist.get(String.class, key)); break;
                            case "textureFileName": spriteSheet.textureFileName = plist.get(String.class, key); break;
                        }
                    } else if (key.startsWith("frames.")) {
                        String[] spriteData = key.split("frames.")[1].split("..png.");
                        String spriteName = spriteData[0]+".png";
                        String metaKey = spriteData[1];
                        if(!spriteMap.containsKey(spriteName)) spriteMap.put(spriteName, new SpriteMeta());
                        SpriteMeta sm = spriteMap.get(spriteName);
                        switch (metaKey) {
                            case "spriteOffset": sm.spriteOffset = vec2fFromString(plist.get(String.class, key)); break;
                            case "spriteSize": sm.spriteSize = vec2iFromString(plist.get(String.class, key)); break;
                            case "spriteSourceSize": sm.spriteSourceSize = vec2iFromString(plist.get(String.class, key)); break;
                            case "textureRect": sm.textureRect = vec2iArray2FromString(plist.get(String.class, key)); break;
                            case "textureRotated": sm.textureRotated = plist.get(Boolean.class, key); break;
                        }
                    }
                }
                for(String name : spriteMap.keySet()) {
                    SpriteMeta sprite = spriteMap.get(name);
                    if(sprite.textureRotated) {
                        int xTemp = sprite.textureRect[1].x;
                        sprite.textureRect[1].x = sprite.textureRect[1].y;
                        sprite.textureRect[1].y = xTemp;
                    }
                    float bottomY = (sprite.getY()+sprite.getHeight())/(float)texture.getHeight();
                    float rightX = (sprite.getX()+ sprite.getWidth())/(float)texture.getWidth();
                    float leftX = sprite.getX()/(float)texture.getWidth();
                    float topY = sprite.getY()/(float)texture.getHeight();
                    Vector2f[] texCoords;
                    texCoords = new Vector2f[]{
                            new Vector2f(rightX, topY),
                            new Vector2f(rightX, bottomY),
                            new Vector2f(leftX, bottomY),
                            new Vector2f(leftX, topY)
                    };
                    spriteSheet.data.put(name, new Sprite(texture, texCoords, new Vector2f(sprite.getHeight(), sprite.getWidth()), sprite.spriteOffset, sprite.textureRotated));
                }
                return spriteSheet;
            } catch (Exception e) {
                continue;
            }
        }
        assert false : "Could not load Complex Spritesheet '" + filename + "'.";
        return new ComplexSpritesheet(new HashMap<>());
    }
    public static Vector2i vec2iFromString(String str) {
        String withoutBrackets = str.replace("{", "").replace("}", "");
        String[] coordinates = withoutBrackets.split(",");
        return new Vector2i(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
    }
    public static Vector2f vec2fFromString(String str) {
        String withoutBrackets = str.replace("{", "").replace("}", "");
        String[] coordinates = withoutBrackets.split(",");
        return new Vector2f(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1]));
    }
    public static Vector2i[] vec2iArray2FromString(String str) {
        String[] vectors = str.split("},\\{");
        String vec1 = vectors[0].replace("{", "").replace("}", "");
        String vec2 = vectors[1].replace("{", "").replace("}", "");
        return new Vector2i[] {vec2iFromString(vec1), vec2iFromString(vec2)};
    }
    public static class SpriteMeta {
        public Vector2f spriteOffset;
        public Vector2i spriteSize, spriteSourceSize;
        public Vector2i[] textureRect;
        public boolean textureRotated;
        public float getWidth() {return textureRect[1].x;}
        public float getHeight() {return textureRect[1].y;}
        public float getX() {return textureRect[0].x;}
        public float getY() {return textureRect[0].y;}

    }
    public Sprite getSprite(String name) {
        return data.get(name);
    }
}
