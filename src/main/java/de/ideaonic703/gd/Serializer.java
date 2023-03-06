package de.ideaonic703.gd;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class Serializer {
    public static void saveVector(ObjectOutputStream stream, Vector2f vec) throws IOException {
        stream.writeInt(TYPES.VECTOR2F.ordinal());
        stream.writeFloat(vec.x);
        stream.writeFloat(vec.y);
    }
    public static void saveVector(ObjectOutputStream stream, Vector4f vec) throws IOException {
        stream.writeInt(TYPES.VECTOR4F.ordinal());
        stream.writeFloat(vec.x);
        stream.writeFloat(vec.y);
        stream.writeFloat(vec.z);
        stream.writeFloat(vec.w);
    }
    public static Vector2f loadVector2f(ObjectInputStream stream) throws IOException {
        float x = stream.readFloat();
        float y = stream.readFloat();
        return new Vector2f(x, y);
    }
    public static Vector4f loadVector4f(ObjectInputStream stream) throws IOException {
        float x = stream.readFloat();
        float y = stream.readFloat();
        float z = stream.readFloat();
        float w = stream.readFloat();
        return new Vector4f(x, y, z, w);
    }
    public static void saveString(ObjectOutputStream stream, String str) throws IOException {
        stream.writeInt(str.length());
        stream.writeBytes(str);
    }
    public static String readString(ObjectInputStream stream) throws IOException {
        byte[] result = stream.readNBytes(stream.readInt());
        return new String(result);
    }

    public enum TYPES {
        SOLID_BLOCK,
        VECTOR2F,
        VECTOR4F,
        SPIKE,
        COLOR_TRIGGER
    }
}
