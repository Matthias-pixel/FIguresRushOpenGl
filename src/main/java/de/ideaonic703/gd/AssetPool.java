package de.ideaonic703.gd;

import de.ideaonic703.gd.components.ComplexSpritesheet;
import de.ideaonic703.gd.components.Spritesheet;
import de.ideaonic703.gd.engine.renderer.Shader;
import de.ideaonic703.gd.engine.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static boolean frozen = false;
    private final static Map<String, Shader> shaders = new HashMap<>();
    private final static Map<String, Texture> textures = new HashMap<>();
    private final static Map<String, Spritesheet> spritesheets = new HashMap<>();
    private final static Map<String, ComplexSpritesheet> complexSpritesheets = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if(shaders.containsKey(file.getAbsolutePath())) return shaders.get(file.getAbsolutePath());
        assert !frozen : "Shader '" + resourceName + "' loaded at runtime";
        Shader shader = new Shader(resourceName);
        shader.compile();
        AssetPool.shaders.put(file.getAbsolutePath(), shader);
        return shader;
    }
    public static Texture getTexture(String resourceName) {
        return getTexture(resourceName, true);
    }
    public static Texture getTexture(String resourceName, boolean flipped) {
        File file = new File(resourceName);
        if(textures.containsKey(file.getAbsolutePath()+flipped)) return textures.get(file.getAbsolutePath()+flipped);
        assert !frozen : "Texture '" + resourceName+flipped + "' loaded at runtime";
        Texture texture = new Texture(resourceName, flipped);
        AssetPool.textures.put(file.getAbsolutePath()+flipped, texture);
        return texture;
    }
    public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        assert !frozen : "Spritesheet '" + resourceName + "' loaded at runtime";
        File file = new File(resourceName);
        if(!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }
    public static void addComplexSpritesheet(String resourceName, ComplexSpritesheet spritesheet) {
        assert !frozen : "Complex Spritesheet '" + resourceName + "' loaded at runtime";
        File file = new File(resourceName+".plist");
        if(!AssetPool.complexSpritesheets.containsKey(file.getAbsolutePath())) {
            complexSpritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }
    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        assert AssetPool.spritesheets.containsKey(file.getAbsolutePath()) : "Error: Spritesheet '" + resourceName + "' accessed before adding.";
        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
    public static ComplexSpritesheet getComplexSpritesheet(String resourceName) {
        File file = new File(resourceName+".plist");
        assert AssetPool.complexSpritesheets.containsKey(file.getAbsolutePath()) : "Error: Complex Spritesheet '" + resourceName + "' accessed before adding.";
        return AssetPool.complexSpritesheets.getOrDefault(file.getAbsolutePath(), null);
    }

    public static void freeze() {
        AssetPool.frozen = true;
    }
}
