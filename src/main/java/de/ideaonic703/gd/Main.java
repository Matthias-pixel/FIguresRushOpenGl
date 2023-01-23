package de.ideaonic703.gd;

import de.ideaonic703.gd.engine.Window;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL20.GL_MAX_TEXTURE_IMAGE_UNITS;

public class Main {
    public static void main(String[] args) {
        Window window = Window.getInstance("Geometry Dash", 2560, 1440);
        window.run();
    }
}
