package de.ideaonic703.gd.engine.renderer;

import de.ideaonic703.gd.components.SpriteRenderer;
import de.ideaonic703.gd.engine.GameObject;

import java.util.ArrayList;
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
    }

    private void add(SpriteRenderer sprite) {
        boolean added = false;
        for(RenderBatch batch : batches)  {
            if(batch.hasRoom()) {
                batch.addSprite(sprite);
                added = true;
                break;
            }
        }
        if(!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
        }
    }

    public void render() {
        for(RenderBatch batch : batches) {
            batch.render();
        }
    }
}