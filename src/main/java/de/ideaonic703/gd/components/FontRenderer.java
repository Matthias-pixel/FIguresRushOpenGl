package de.ideaonic703.gd.components;

import de.ideaonic703.gd.engine.Component;
import de.ideaonic703.gd.engine.Font;
import de.ideaonic703.gd.engine.Transform;
import de.ideaonic703.gd.engine.renderer.Texture;
import org.joml.Vector4f;

public class FontRenderer extends Component {
    private Vector4f color;
    private Font font;
    private String text;
    private Transform lastTransform;
    private SpriteRenderer[] spriteRenderers;
    private float scale = 1.0f;
    private boolean dirty = true;

    public FontRenderer(String text, Font font, Vector4f color) {
        this.text = text;
        this.font = font;
        this.color = color;
    }
    public FontRenderer(String text, Font font) {
        this(text, font, new Vector4f(1, 1, 1, 1));
    }
    public void setColor(Vector4f color) {this.color = color; this.makeDirty();}
    public void setText(String text) {this.text = text; this.makeDirty();}
    public void setFont(Font font) {this.font = font; this.makeDirty();}
    public boolean isDirty() {return this.dirty;}
    public void makeDirty() {
        for(SpriteRenderer spriteRenderer : this.spriteRenderers) spriteRenderer.makeDirty();
        this.dirty = true;
    }
    public void clean() {this.dirty = false;}
    public Texture getTexture() {return this.font.getTexture();}
    public SpriteRenderer[] getSpriteRenderers() {
        if(this.dirty) {
            this.spriteRenderers = new SpriteRenderer[this.text.length()];
            int prv = -1;
            int offset = 0;
            for (int i = 0; i < this.text.length(); i++) {
                char chr = this.text.charAt(i);
                this.spriteRenderers[i] = this.font.getSpriteRenderer(prv, chr, color);
                this.spriteRenderers[i].setOffset(this.spriteRenderers[i].getOffset().add(offset, 0).mul(this.scale));
                this.spriteRenderers[i].setScaleOverride(this.spriteRenderers[i].getScaleOverride().mul(this.scale));
                offset += this.font.getXAdvance(chr);
                this.spriteRenderers[i].gameObject = this.gameObject;
                prv = chr;
            }
        }
        return this.spriteRenderers;
    }

    @Override
    public void start() {
        super.start();
        this.lastTransform = this.gameObject.transform.copy();
    }
    @Override
    public void update(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            this.makeDirty();
        }
    }
    public void setScale(float scale) {
        this.scale = scale;
    }
}
