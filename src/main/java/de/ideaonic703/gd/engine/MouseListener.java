package de.ideaonic703.gd.engine;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener INSTANCE;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY, dragX, dragY;
    private final boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener getInstance() {
        if(MouseListener.INSTANCE == null) MouseListener.INSTANCE = new MouseListener();
        return INSTANCE;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        xPos = xPos * 1920 / Window.getWidth();
        yPos = yPos * 1080 / Window.getHeight();
        yPos = 1080-yPos;
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().xPos = xPos;
        getInstance().yPos = yPos;
        boolean isDragging = getInstance().mouseButtonPressed[0] || getInstance().mouseButtonPressed[1] || getInstance().mouseButtonPressed[2];
        if(isDragging && !getInstance().isDragging) {
            getInstance().dragX = xPos;
            getInstance().dragY = yPos;
        }
        if(!isDragging) {
            getInstance().dragX = -1;
            getInstance().dragY = -1;
        }
        getInstance().isDragging = isDragging;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if(button < 0 || button >= getInstance().mouseButtonPressed.length) return;
        if(action == GLFW_PRESS)
            getInstance().mouseButtonPressed[button] = true;
        else if(action == GLFW_RELEASE) {
            getInstance().mouseButtonPressed[button] = false;
            getInstance().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }

    public static void endFrame() {
        getInstance().scrollX = 0;
        getInstance().scrollY = 0;
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
    }

    public static float getX() {
        return (float)getInstance().xPos;
    }

    public static float getY() {
        return (float)getInstance().yPos;
    }
    public static Vector2f getPos() {
        return new Vector2f((float) getInstance().xPos, (float) getInstance().yPos);
    }

    public static float getDx() {
        return (float)(getInstance().xPos-getInstance().lastX);
    }

    public static float getDy() {
        return (float)(getInstance().yPos-getInstance().lastY);
    }

    public static float getScrollX() {
        return (float)getInstance().scrollX;
    }

    public static float getScrollY() {
        return (float)getInstance().scrollY;
    }

    public static boolean isDragging() {
        return getInstance().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if(button < 0 || button >= getInstance().mouseButtonPressed.length) return false;
        return getInstance().mouseButtonPressed[button];
    }
}
