package de.ideaonic703.gd.components;

import de.ideaonic703.gd.engine.Component;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if(gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("FontRenderer of " + gameObject + " found SpriteRenderer");
        }
    }

    @Override
    public void update(float dt) {

    }
}
