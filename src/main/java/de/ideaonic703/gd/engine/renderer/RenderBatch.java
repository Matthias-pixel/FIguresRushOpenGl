package de.ideaonic703.gd.engine.renderer;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.joml.Math.cos;
import static org.joml.Math.sin;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch> {
    public final static float[] ROTATION_SLOTS_ANGLE = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public final static Vector2f[] ROTATION_SLOTS_CENTER  = {new Vector2f(), new Vector2f(), new Vector2f(), new Vector2f(), new Vector2f(), new Vector2f(), new Vector2f(), new Vector2f()};
    //  Layout
    //
    //  Position            Color                           TexCoords       TexID
    //  float, float,       float, float, float, float      float, float    float
    private final static int POS_SIZE = 2;
    private final static int COLOR_SIZE = 4;
    private final static int TEX_COORDS_SIZE = 2;
    private final static int TEX_ID_SIZE = 1;
    private final static int SECONDARY_CAM_SIZE = 1;

    private final static int POS_OFFSET = 0;
    private final static int COLOR_OFFSET = POS_OFFSET+POS_SIZE*Float.BYTES;
    private final static int TEX_COORDS_OFFSET = COLOR_OFFSET+COLOR_SIZE*Float.BYTES;
    private final static int TEX_ID_OFFSET = TEX_COORDS_OFFSET+TEX_COORDS_SIZE*Float.BYTES;
    private final static int SECONDARY_CAM_OFFSET = TEX_ID_OFFSET+SECONDARY_CAM_SIZE* Float.BYTES;

    private final static int VERTEX_SIZE = POS_SIZE+COLOR_SIZE+TEX_COORDS_SIZE+TEX_ID_SIZE+SECONDARY_CAM_SIZE;
    private final static int VERTEX_SIZE_BYTES = VERTEX_SIZE*Float.BYTES;

    private final SpriteRenderer[] sprites;
    private int spriteCount;
    private final float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private List<Texture> textures;
    private int vaoID, vboID;
    private final int maxBatchSize;
    private final Shader shader;
    private int zIndex;
    private int rotationSlot;

    public RenderBatch(int maxBatchSize, int zIndex, int rotationSlot) {
        this.rotationSlot = rotationSlot;
        this.zIndex = zIndex;
        this.maxBatchSize = maxBatchSize;
        shader = AssetPool.getShader("assets/shaders/default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        vertices = new float[maxBatchSize*4*VERTEX_SIZE];
        this.spriteCount = 0;
        this.textures = new ArrayList<>(8);
    }

    public boolean addSpriteRenderer(SpriteRenderer sprite) {
        if(!hasRoom()) return false;
        sprites[spriteCount] = sprite;
        if(sprite.getTexture() != null) {
            if(!textures.contains(sprite.getTexture())) {
                textures.add(sprite.getTexture());
            }
        }
        loadVertexProperties(spriteCount++);
        return true;
    }

    public void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length *Float.BYTES, GL_DYNAMIC_DRAW);
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(4, SECONDARY_CAM_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, SECONDARY_CAM_OFFSET);
        glEnableVertexAttribArray(4);

    }
    public void render() {
        boolean rebufferData = false;
        for(int i = 0; i < sprites.length; i++) {
            SpriteRenderer spriteRenderer = sprites[i];
            if(spriteRenderer != null && spriteRenderer.isDirty()) {
                loadVertexProperties(i);
                spriteRenderer.clean();
                rebufferData = true;
            }
        }
        if(rebufferData) {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uProjection2", Window.getScene().getCamera2().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());
        shader.uploadMat4f("uView2", Window.getScene().getCamera2().getViewMatrix());
        for(int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0+i+1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, this.spriteCount*6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (Texture texture : textures) {
            texture.unbind();
        }
        shader.detach();
    }
    private static Vector2f rotateVertex(Vector2f vertex, Vector2f center, float angle) {
        vertex = new Vector2f(vertex);
        float s = sin(angle);
        float c = cos(angle);

        // translate point back to origin:
        vertex.x -= center.x;
        vertex.y -= center.y;

        // rotate point
        float xnew = vertex.x * c - vertex.y * s;
        float ynew = vertex.x * s + vertex.y * c;

        // translate point back:
        vertex.x = xnew + center.x;
        vertex.y = ynew + center.y;
        return vertex;
    }
    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];
        float secondaryCamera = sprite.usesSecondaryCamera() ? 1.0f : 0.0f;
        int offset = index*4*VERTEX_SIZE;
        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();
        if(texCoords.length != 4) throw new IndexOutOfBoundsException();
        int texID = 0;
        if(sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if(textures.get(i) == sprite.getTexture()) {
                    texID = i+1;
                    break;
                }
            }
        }

        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for(int i = 0; i < 4; i++) {
            if(i == 1) {
                yAdd = 0.0f;
            } else if(i == 2) {
                xAdd = 0.0f;
            } else if(i == 3) {
                yAdd = 1.0f;
            }

            // position
            if(this.rotationSlot != 0) {
                Vector2f position = new Vector2f(sprite.getTransform().getPrecisePosition().x + (xAdd*sprite.getTransform().getScale().x), sprite.getTransform().getPrecisePosition().y + (yAdd*sprite.getTransform().getScale().y));
                Vector2f center = ROTATION_SLOTS_CENTER[this.rotationSlot];
                Vector2f rotatedPosition = RenderBatch.rotateVertex(position, center, RenderBatch.ROTATION_SLOTS_ANGLE[this.rotationSlot]);
                vertices[offset] = rotatedPosition.x;
                vertices[offset+1] = rotatedPosition.y;
            } else {
                vertices[offset] = sprite.getTransform().getPrecisePosition().x + (xAdd*sprite.getTransform().getScale().x);
                vertices[offset+1] = sprite.getTransform().getPrecisePosition().y + (yAdd*sprite.getTransform().getScale().y);
            }

            // color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            // Texture coordinates
            vertices[offset+6] = texCoords[i].x;
            vertices[offset+7] = texCoords[i].y;

            // Texture ID
            vertices[offset+8] = texID;

            //Secondary Camera
            vertices[offset+9] = secondaryCamera;

            offset += VERTEX_SIZE;
        }
    }
    private int[] generateIndices() {
        int[] indices = new int[maxBatchSize*6];
        for(int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(indices, i);
        }
        return indices;
    }
    private void loadElementIndices(int[] indices, int i) {
        int offsetArrayIndex = 6*i;
        int offset = 4*i;
        indices[offsetArrayIndex] = offset + 3;
        indices[offsetArrayIndex+1] = offset + 2;
        indices[offsetArrayIndex+2] = offset;
        indices[offsetArrayIndex+3] = offset;
        indices[offsetArrayIndex+4] = offset + 2;
        indices[offsetArrayIndex+5] = offset + 1;
    }
    public boolean hasRoom() {
        return spriteCount < maxBatchSize-1;
    }
    public boolean hasTextureRoom() {
        return this.textures.size() < 8;
    }
    public boolean hasTexture(Texture texture) {
        return this.textures.contains(texture);
    }
    public int getzIndex() {
        return this.zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.getzIndex());
    }

    public int getRotationSlot() {
        return this.rotationSlot;
    }
}
