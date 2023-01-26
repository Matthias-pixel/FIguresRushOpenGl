package de.ideaonic703.gd;

import static java.lang.System.nanoTime;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    //public static float timeStarted = nanoTime();
    public static float getTime() {
        //return (float)((nanoTime()-timeStarted) * 1E-9);
        return (float)glfwGetTime();
    }
}
