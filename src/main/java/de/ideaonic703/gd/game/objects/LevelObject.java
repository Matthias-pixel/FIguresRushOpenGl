package de.ideaonic703.gd.game.objects;

import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.Transform;
import org.joml.Vector2f;

public abstract class LevelObject extends GameObject {
    public abstract boolean[] flags();
    protected abstract Transform hitbox();
    public LevelObject(Transform transform, int zIndex) {
        super(transform, zIndex);
    }
    public LevelObject() {
        super(new Transform(), 0);
    }
    public LevelObject(Transform transform) {
        super(transform, 0);
    }
    public LevelObject(int zIndex) {
        super(new Transform(), zIndex);
    }
    public boolean interjectsWithHitbox(Vector2f position, Vector2f size) {
        return hitbox().interjects(new Vector2f(position).sub(this.transform.getPrecisePosition()), size);
    }
}
