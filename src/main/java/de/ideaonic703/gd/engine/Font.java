package de.ideaonic703.gd.engine;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.Sprite;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Font {
    private Map<Integer, Letter> letters;
    private Map<Integer, List<Kerning>> kernings;
    private int lineHeight;
    private Texture texture;

    private Font(Texture texture, int lineHeight, Map<Integer, Letter> letters, Map<Integer, List<Kerning>> kernings) {
        this.texture = texture;
        this.lineHeight = lineHeight;
        this.letters = letters;
        this.kernings = kernings;
    }
    public static Font loadFromFile(String filename) throws IOException {
        Texture texture = AssetPool.getTexture(filename+".png", false);
        final File file = new File(filename+".fnt");
        final FileInputStream fis = new FileInputStream(file);
        final byte[] bytes = fis.readAllBytes();
        final String fileData = new String(bytes);
        final String[] lines = fileData.split("\n");
        int lineHeight = -1;
        Map<Integer, Letter> letters = new HashMap<>();
        Map<Integer, List<Kerning>> kernings = new HashMap<>();
        for(int i = 0; i < lines.length; i++) {
            final List<String> items = new java.util.ArrayList<>(List.of(lines[i].split(" ")));
            while(items.remove(""));
            String type = items.get(0);
            items.remove(0);
            if(type.equals("char")) {
                Letter letter = new Letter();
                for(String item : items) {
                    final String[] pair = item.split("=");
                    String key = pair[0];
                    String value = pair[1];
                    switch(key) {
                        case "id": letter.id = Integer.parseInt(value); break;
                        case "x": letter.x = Integer.parseInt(value); break;
                        case "y": letter.y = Integer.parseInt(value); break;
                        case "width": letter.width = Integer.parseInt(value); break;
                        case "height": letter.height = Integer.parseInt(value); break;
                        case "xoffset": letter.xoffset = Integer.parseInt(value); break;
                        case "yoffset": letter.yoffset = Integer.parseInt(value); break;
                        case "xadvance": letter.xadvance = Integer.parseInt(value); break;
                        case "letter": {
                            if(value.equals("\"space\"")) letter.letter = ' ';
                            else if(value.length() == 3) letter.letter = value.charAt(1);
                            else letter.letter = '\0';
                        } break;
                        default: break;
                    }
                    letters.put(letter.id, letter);
                }
            } else if(type.equals("common")) {
                for(String item : items) {
                    final String[] pair = item.split("=");
                    String key = pair[0];
                    String value = pair[1];
                    switch(key) {
                        case "lineHeight": lineHeight = Integer.parseInt(value); break;
                        default: break;
                    }
                }
            } else if(type.equals("kerning")) {
                Kerning kerning = new Kerning();
                for(String item : items) {
                    final String[] pair = item.split("=");
                    String key = pair[0];
                    String value = pair[1];
                    switch(key) {
                        case "first": kerning.first = Integer.parseInt(value); break;
                        case "second": kerning.second = Integer.parseInt(value); break;
                        case "amount": kerning.amount = Integer.parseInt(value); break;
                        default: break;
                    }
                }
                kernings.computeIfAbsent(kerning.second, k -> new ArrayList<>());
                kernings.get(kerning.second).add(kerning);
            }
        }
        fis.close();
        return new Font(texture, lineHeight, letters, kernings);
    }
    public Texture getTexture() {return texture;}
    public Sprite getSprite(int chr) {
        Letter letter = this.letters.get(chr);
        if(letter == null) letter = this.letters.get((int)'?');
        float bottomY = (letter.getY()+letter.getHeight())/(float)texture.getHeight();
        float rightX = (letter.getX()+ letter.getWidth())/(float)texture.getWidth();
        float leftX = letter.getX()/(float)texture.getWidth();
        float topY = letter.getY()/(float)texture.getHeight();
        Vector2f[] texCoords = new Vector2f[]{
                new Vector2f(rightX, topY),
                new Vector2f(rightX, bottomY),
                new Vector2f(leftX, bottomY),
                new Vector2f(leftX, topY)
        };
        return new Sprite(this.texture, texCoords);
    }
    public Vector2f getOffset(int prv, int thz) {
        List<Kerning> kernings = this.kernings.get(thz);
        Kerning foundKerning = null;
        if(kernings != null) {
            for (Kerning kerning : kernings) {
                if (kerning.first == prv) {
                    foundKerning = kerning;
                    break;
                }
            }
        }
        Vector2f fixedOffset = new Vector2f(-this.letters.get(thz).xoffset, -this.letters.get(thz).yoffset);
        Vector2f dynamicOffset = new Vector2f();
        if(foundKerning != null) dynamicOffset = new Vector2f(foundKerning.amount, 0);
        return fixedOffset.add(dynamicOffset);
    }
    public Vector2f getScale(int chr) {
        Letter letter = this.letters.get(chr);
        if(letter == null) letter = this.letters.get((int)'?');
        return new Vector2f(letter.getWidth(), letter.getHeight());
    }
    public SpriteRenderer getSpriteRenderer(int prv, int thz, Vector4f color) {
        SpriteRenderer s = new SpriteRenderer(this.getSprite(thz), color);
        s.setOffset(this.getOffset(prv, thz));
        s.setScaleOverride(this.getScale(thz));
        return s;
    }
    public int getXAdvance(int chr) {
        return this.letters.get(chr).xadvance;
    }

    private final static class Letter {
        int id;
        int x;
        int y;
        int width;
        int height;
        int xoffset;
        int yoffset;
        int xadvance;
        char letter;
        public int getY() {return y;}
        public int getX() {return x;}
        public int getWidth() {return width;}
        public int getHeight() {return height;}
    }
    private final static class Kerning {
        int first, second, amount;
    }
}
