package de.ideaonic703.gd.game.objects;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.Serializer;
import de.ideaonic703.gd.components.ComplexSpritesheet;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.Transform;
import org.joml.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Spike extends LevelObject {
    private final static boolean[] FLAGS = new boolean[]{
            /*Solid*/ true,
            /*Dangerous*/ true,
    };
    private final static Transform HITBOX = new Transform(new Vector2f(0.2f, 0f), new Vector2f(0.6f, 0.3f));
    public static final int BLOCK_DEFAULT = 0;
    private String[] spriteMap = {"spike_01_001.png"};
    public static int BLOCK_Z = -1;
    private int type;

    public Spike(Vector2f position, int type) {
        super(new Transform(position, new Vector2f(1, 1)), -1);
        this.setType(type);
    }
    public void init() {
        ComplexSpritesheet spritesheet = AssetPool.getComplexSpritesheet("assets/gdresources/GJ_GameSheet-uhd");
        this.addComponent(new SpriteRenderer(spritesheet.getSprite(spriteMap[type])));
        this.getComponent(SpriteRenderer.class).setUsedCamera(1.0f);
    }
    @Override
    public void start() {
        super.start();
    }
    public void setType(int type) {
        this.type = type;
    }

    public void save(ObjectOutputStream stream) throws IOException {
        stream.writeInt(Serializer.TYPES.SPIKE.ordinal());
        stream.writeInt(type);
        Serializer.saveVector(stream, transform.getPrecisePosition());
    }
    public static Spike load(ObjectInputStream stream) throws IOException {
        int type = stream.readInt();
        assert stream.readInt() == Serializer.TYPES.VECTOR2F.ordinal();
        Vector2f position = Serializer.loadVector2f(stream);
        return new Spike(position, type);
    }
    public boolean[] flags() {
         return FLAGS;
    }
    @Override
    protected Transform hitbox() {
        return HITBOX;
    }
}
