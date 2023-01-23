package de.ideaonic703.gd.engine.renderer;

import de.ideaonic703.gd.AssetPool;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.Window;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderBatch {
    //  Layout
    //
    //  Position            Color
    //  float, float,       float, float, float, float
    private final static int POS_SIZE = 2;
    private final static int COLOR_SIZE = 4;

    private final static int POS_OFFSET = 0;
    private final static int COLOR_OFFSET = POS_OFFSET+POS_SIZE*Float.BYTES;
    private final static int VERTEX_SIZE = 6;
    private final static int VERTEX_SIZE_BYTES = VERTEX_SIZE*Float.BYTES;

    private final SpriteRenderer[] sprites;
    private int spriteCount;
    private final float[] vertices;

    private int vaoID, vboID;
    private final int maxBatchSize;
    private final Shader shader;

    public RenderBatch(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        shader = AssetPool.getShader("assets/shaders/default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        vertices = new float[maxBatchSize*4*VERTEX_SIZE];
        this.spriteCount = 0;
    }

    public boolean addSprite(SpriteRenderer sprite) {
        if(!hasRoom()) return false;
        sprites[spriteCount] = sprite;
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
    }
    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, this.spriteCount*6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }
    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];
        int offset = index*4*VERTEX_SIZE;
        Vector4f color = sprite.getColor();
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
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd*sprite.gameObject.transform.scale.x);
            vertices[offset+1] = sprite.gameObject.transform.position.y + (yAdd*sprite.gameObject.transform.scale.y);

            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

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
}
