package de.ideaonic703.gd.engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener INSTANCE;
    private boolean[] keyPressed = new boolean[350];
    private KeyListener() {}
    public static KeyListener getInstance() {
        if(INSTANCE == null) INSTANCE = new KeyListener();
        return INSTANCE;
    }
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if(key < 0 || key > 350) return;
        if(action == GLFW_PRESS)
            getInstance().keyPressed[key] = true;
        else if (action == GLFW_RELEASE)
            getInstance().keyPressed[key] = false;
    }
    public static boolean isKeyPressed(int keyCode) {
        return getInstance().keyPressed[keyCode];
    }
}
