package de.ideaonic703.gd.engine;

public abstract class Component {
    public GameObject gameObject = null;

    public abstract void update(float dt);
    public void start() {}
}
