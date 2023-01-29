package de.ideaonic703.gd;

import org.joml.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class Serializer {
    public static void saveVector(ObjectOutputStream stream, Vector2f vec) throws IOException {
        stream.writeInt(TYPES.VECTOR2F.ordinal());
        stream.writeFloat(vec.x);
        stream.writeFloat(vec.y);
    }
    public static Vector2f loadVector2f(ObjectInputStream stream) throws IOException {
        float x = stream.readFloat();
        float y = stream.readFloat();
        return new Vector2f(x, y);
    }
    public enum TYPES {
        SOLID_BLOCK,
        VECTOR2F
    }
}
