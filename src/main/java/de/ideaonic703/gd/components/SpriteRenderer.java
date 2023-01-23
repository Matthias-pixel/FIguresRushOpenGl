package de.ideaonic703.gd.components;

import de.ideaonic703.gd.engine.Component;
import org.joml.Vector4f;

import java.util.Vector;

public class SpriteRenderer extends Component {
    Vector4f color;
    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(float dt) {

    }
    public Vector4f getColor() {
        return color;
    }
}
