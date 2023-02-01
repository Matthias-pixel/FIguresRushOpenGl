package de.ideaonic703.gd.game.objects;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.Serializer;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.components.Spritesheet;
import de.ideaonic703.gd.engine.GameObject;
import de.ideaonic703.gd.engine.Transform;
import org.joml.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SolidBlock extends GameObject {
    public static int BLOCK_Z = -1;
    private int type;

    public SolidBlock(Vector2f position, int type) {
        super(new Transform(position), -1);
        this.setType(type);
    }
    public void setType(int type) {
        this.type = type;
        Spritesheet spritesheet = AssetPool.getSpritesheet("assets/spritesheet.png");
        this.addComponent(new SpriteRenderer(spritesheet.getSprite(type)));
    }

    public void save(ObjectOutputStream stream) throws IOException {
        stream.writeInt(Serializer.TYPES.SOLID_BLOCK.ordinal());
        stream.writeInt(type);
        Serializer.saveVector(stream, transform.getPrecisePosition());
    }
    public static SolidBlock load(ObjectInputStream stream) throws IOException {
        int type = stream.readInt();
        assert stream.readInt() == Serializer.TYPES.VECTOR2F.ordinal();
        Vector2f position = Serializer.loadVector2f(stream);
        return new SolidBlock(position, type);
    }
}
