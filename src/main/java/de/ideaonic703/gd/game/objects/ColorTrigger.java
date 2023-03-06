package de.ideaonic703.gd.game.objects;

import de.ideaonic703.gd.Serializer;
import de.ideaonic703.gd.engine.Transform;
import de.ideaonic703.gd.game.Level;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ColorTrigger extends Trigger {
    private final static boolean[] FLAGS = new boolean[]{
            /*Solid*/ false,
            /*Dangerous*/ false,
    };
    private final static Transform HITBOX = new Transform(new Vector2f(0f, 0f), new Vector2f(0f, 0f));
    private final Vector4f difference;
    private Vector4f startColor, endColor;
    private float transitionTime;

    public ColorTrigger(Vector2f position, Vector4f startColor, Vector4f endColor, float transitionTime) {
        this.transform = new Transform(position, new Vector2f(transitionTime, Level.MAX_HEIGHT));
        this.startColor = startColor;
        this.endColor = endColor;
        this.transitionTime = transitionTime;
        this.difference = new Vector4f(endColor).sub(startColor);
    }
    public float getTransitionTime() {
        return this.transitionTime;
    }
    public Vector4f getStartColor() {
        return startColor;
    }
    public Vector4f getEndColor() {
        return endColor;
    }
    public Vector4f getColorForTime(float countdown) {
        return new Vector4f(startColor).add(new Vector4f(difference).mul(1-countdown/transitionTime));
    }
    public boolean setColorForTime(Vector4f color, float xPos) {
        float countdown = xPos - this.transform.getPrecisePosition().x;
        System.out.println("Countdown: " + countdown);
        if(countdown < 0f) {
            return true;
        } else if(countdown > transitionTime) {
            color.set(this.endColor);
            return false;
        }
        color.set(new Vector4f(startColor).add(new Vector4f(difference).mul(countdown/transitionTime)));
        return true;
    }

    @Override
    public boolean[] flags() {
        return FLAGS;
    }

    @Override
    protected Transform hitbox() {
        return HITBOX;
    }
    public void save(ObjectOutputStream stream) throws IOException {
        stream.writeInt(Serializer.TYPES.COLOR_TRIGGER.ordinal());
        Serializer.saveVector(stream, this.startColor);
        Serializer.saveVector(stream, this.endColor);
        stream.writeFloat(transitionTime);
        Serializer.saveVector(stream, transform.getPrecisePosition());
    }
    public static ColorTrigger load(ObjectInputStream stream) throws IOException {
        assert stream.readInt() == Serializer.TYPES.VECTOR4F.ordinal();
        Vector4f startColor = Serializer.loadVector4f(stream);
        assert stream.readInt() == Serializer.TYPES.VECTOR4F.ordinal();
        Vector4f endColor = Serializer.loadVector4f(stream);
        float transitionTime = stream.readFloat();
        assert stream.readInt() == Serializer.TYPES.VECTOR2F.ordinal();
        Vector2f position = Serializer.loadVector2f(stream);
        return new ColorTrigger(position, startColor, endColor, transitionTime);
    }
}
