package de.ideaonic703.gd.engine;

import de.ideaonic703.gd.Time;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static java.lang.System.nanoTime;
import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20.GL_MAX_TEXTURE_IMAGE_UNITS;
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
    private Scene currentScene;

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0 -> {
                getInstance().currentScene = new LevelEditorScene();
                getInstance().currentScene.init();
                getInstance().currentScene.start();
            }
            case 1 -> {
                getInstance().currentScene = new LevelScene();
                getInstance().currentScene.init();
                getInstance().currentScene.start();
            }
            default -> {
                assert false : "Unknown Scene '" + newScene + "'";
            }
        }
    }

    public void run() {
        init();
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

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(0);
    }

    public void loop() {
        float startTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        while(!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt > 0)
                currentScene.update(dt);

            glfwSwapBuffers(glfwWindow);
            /*try {
                sleep(1000/60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            endTime = Time.getTime();
            dt = endTime - startTime;
            startTime = endTime;
        }
    }

    public static Scene getScene() {
        return getInstance().currentScene;
    }
}
