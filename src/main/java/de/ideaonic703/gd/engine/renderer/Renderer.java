package de.ideaonic703.gd.engine.renderer;

import de.ideaonic703.gd.components.FontRenderer;
import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final static int MAX_BATCH_SIZE = 1000;
    private final List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }
    public void add(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if(spr != null) add(spr);
        FontRenderer ftr = go.getComponent(FontRenderer.class);
        if(ftr != null) add(ftr);
    }
    public void remove(GameObject go) {
        for(RenderBatch batch : batches) {
            batch.remove(go);
        }
    }

    private void add(SpriteRenderer spriteRenderer) {
        boolean added = false;
        for(RenderBatch batch : batches)  {
            if(batch.hasRoom() && batch.getzIndex() == spriteRenderer.gameObject.getzIndex() && batch.getRotationSlot() == spriteRenderer.gameObject.transform.getRotationSlot()) {
                Texture tex = spriteRenderer.getTexture();
                if(tex == null || batch.hasTexture(tex) || batch.hasTextureRoom()) {
                    batch.addSpriteRenderer(spriteRenderer);
                    added = true;
                    break;
                }
            }
        }
        if(!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, spriteRenderer.gameObject.getzIndex(), spriteRenderer.gameObject.transform.getRotationSlot());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSpriteRenderer(spriteRenderer);
            Collections.sort(batches);
        }
    }
    private void add(FontRenderer fontRenderer) {
        SpriteRenderer[] spriteRenderers = fontRenderer.getSpriteRenderers();
        for(SpriteRenderer spriteRenderer : spriteRenderers) this.add(spriteRenderer);
    }

    public void render() {
        int renderedObjects = 0;
        for(RenderBatch batch : batches) {
            batch.render();
            renderedObjects += batch.getSpriteCount();
        }
        System.out.println("Rendered Objects: " + renderedObjects);
    }
}
