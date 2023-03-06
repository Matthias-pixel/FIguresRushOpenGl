package de.ideaonic703.gd.engine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    protected List<Component> components;
    private int zIndex;
    public Transform transform;

    public GameObject(Transform transform, int zIndex) {
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
    }
    public GameObject() {
        this(new Transform(), 0);
    }
    public GameObject(Transform transform) {
        this(transform, 0);
    }
    public GameObject(int zIndex) {
        this(new Transform(), zIndex);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for(Component c : components) {
            if(componentClass.isAssignableFrom(c.getClass())) {
                return componentClass.cast(c);
            }
        }
        return null;
    }
    public <T extends Component> T[] getComponents(Class<T> componentClass) {
        List<T> result = new ArrayList<>();
        for(Component c : components) {
            if(componentClass.isAssignableFrom(c.getClass())) {
                result.add(componentClass.cast(c));
            }
        }
        return result.toArray((T[])Array.newInstance(componentClass, 0));
    }
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }
    public void remove() {
        while(components.size() > 0) {
            components.remove(0);
        }
    }
    public void addComponent(Component c) {
        components.add(c);
        c.gameObject = this;
    }
    public boolean update(float dt) {
        for(Component c : components) {
            c.update(dt);
        }
        return false;
    }
    public void init() {}
    public void start() {
        for(Component c : components) {
            c.start();
        }
    }
    public int getzIndex() {
        return this.zIndex;
    }
    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }
    public void imgui() {
        for(Component c : components) {
            c.imgui();
        }
    }

    @Override
    public String toString() {
        if(this.transform == null) return "GameObject without Transform";
        return "gameObject at " + this.transform.toString();
    }
}
