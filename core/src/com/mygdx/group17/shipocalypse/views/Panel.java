package com.mygdx.group17.shipocalypse.views;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Panel extends Table {
    private boolean isVisible;

    public Panel(final Drawable background, final float width, final float height) {
        setBackground(background);
        setSize(width, height);
        isVisible = false;

        // Add a listener to show/hide the panel when clicked
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isVisible = !isVisible;
                event.handle();
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible) {
            // Draw the panel only if it's visible
            super.draw(batch, parentAlpha);
        }
    }
}
