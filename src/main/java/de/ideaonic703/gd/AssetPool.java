package de.ideaonic703.gd;

import de.ideaonic703.gd.engine.renderer.Shader;
import de.ideaonic703.gd.engine.renderer.Texture;
import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static boolean frozen = false;
    private final static Map<String, Shader> shaders = new HashMap<>();
    private final static Map<String, Texture> textures = new HashMap<>();
    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if(shaders.containsKey(file.getAbsolutePath())) return shaders.get(file.getAbsolutePath());
        assert !frozen : "Texture loaded at runtime";
        Shader shader = new Shader(resourceName);
        shader.compile();
        AssetPool.shaders.put(file.getAbsolutePath(), shader);
        return shader;
    }
    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if(shaders.containsKey(file.getAbsolutePath())) return textures.get(file.getAbsolutePath());
        assert !frozen : "Texture loaded at runtime";
        Texture texture = new Texture(resourceName);
        AssetPool.textures.put(file.getAbsolutePath(), texture);
        return texture;
    }

    public static void freeze() {
        AssetPool.frozen = true;
    }
}
