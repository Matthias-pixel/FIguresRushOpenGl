package de.ideaonic703.gd;

import de.ideaonic703.gd.engine.Window;

public class Main {
    public static void main(String[] args) {
        Window window = Window.getInstance("Geometry Dash", 600, 400);
        window.run();
    }
}
