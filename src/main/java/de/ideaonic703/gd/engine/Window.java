package de.ideaonic703.gd.engine;

import de.ideaonic703.gd.Time;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static java.lang.System.nanoTime;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window INSTANCE = null;
    private Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }
    public static Window getInstance(String title, int width, int height) {
        if(INSTANCE == null) INSTANCE = new Window(title, width, height);
        return INSTANCE;
    }
    public static Window getInstance() {
        if(INSTANCE == null) INSTANCE = new Window("Title", 800, 600);
        return INSTANCE;
    }

    private int width, height;
    private String title;
    private long glfwWindow;
    public float r = 1, g = 1, b = 1, a = 1;
    private boolean fadeToBlack = false;
    private float lastNanoTime = 0;
    private Scene currentScene;

    public static void changeScene(int newScene) {
        switch(newScene) {
            case 0:
                getInstance().currentScene = new LevelEditorScene();
                getInstance().currentScene.init();
                break;
            case 1:
                getInstance().currentScene = new LevelScene();
                getInstance().currentScene.init();
                break;
            default:
                assert false : "Unknown Scene '" + newScene + "'";
                break;
        }
    }

    public void run() {
        init();
        currentScene = new LevelEditorScene();
        currentScene.init();
        loop();
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()) throw new RuntimeException("Could not initialise GLFW!");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if(glfwWindow == NULL) throw new RuntimeException("Could not create GLFW Window!");

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();
    }

    public void loop() {
        float startTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        while(!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt > 0)
                currentScene.update(dt);

            glfwSwapBuffers(glfwWindow);
            endTime = Time.getTime();
            dt = endTime - startTime;
            startTime = endTime;
        }
    }
}
