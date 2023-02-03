package de.ideaonic703.gd.engine;

import de.ideaonic703.gd.Time;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window INSTANCE = null;
    private Window(String title, int width, int height, Scene startScene) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.currentScene = startScene;
    }
    public static Window getInstance(String title, int width, int height, Scene startScene) {
        if(INSTANCE == null) INSTANCE = new Window(title, width, height, startScene);
        return INSTANCE;
    }
    public static Window getInstance() {
        if(INSTANCE == null) INSTANCE = new Window("Title", -1, -1, new Scene() {
            @Override
            public void update(float dt) {

            }
        });
        return INSTANCE;
    }

    private int width, height;
    private String title;
    private long glfwWindow;
    private Scene currentScene;
    //private ImGuiLayer imGuiLayer;

    public static void changeScene(Scene newScene) {
        getInstance().currentScene = newScene;
        getInstance().currentScene.init();
        getInstance().currentScene.start();
    }

    public static int getWidth() {
        return getInstance().width;
    }
    public static int getHeight() {
        return getInstance().height;
    }
    public void run() {
        Window.changeScene(this.currentScene);
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
        //glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);
        if(this.width <= 0 || this.height <= 0) {
            long primaryMonitor = glfwGetPrimaryMonitor();
            GLFWVidMode vidMode = glfwGetVideoMode(primaryMonitor);
            if(vidMode == null) {
                assert false : "could not get video mode for primary monitor.";
                this.width = 1920;
                this.height = 1080;
            } else {
                this.width = vidMode.width();
                this.height = vidMode.height();
            }
        }
        //glfwWindow = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), NULL);
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if(glfwWindow == NULL) throw new RuntimeException("Could not create GLFW Window!");

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.getInstance().width = newWidth;
            Window.getInstance().height = newHeight;
        });

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glEnable(GL_MULTISAMPLE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //this.imGuiLayer = new ImGuiLayer(glfwWindow);
        //this.imGuiLayer.initImGui();
    }

    public void loop() {
        float startTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        while(!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(0.2f, 0.2f, 0.2f, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt > 0)
                currentScene.update(dt);
            System.out.printf("FPS: %f%n", 1/dt);

            //this.imGuiLayer.update(dt, currentScene);
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
