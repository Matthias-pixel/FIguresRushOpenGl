package de.ideaonic703.gd.engine;

import java.nio.charset.CoderMalfunctionError;

public abstract class Scene {
    protected Camera camera;
    public abstract void update(float dt);
    public void init() {}
}
