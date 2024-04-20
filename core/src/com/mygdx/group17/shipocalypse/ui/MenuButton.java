package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.Options;
import com.badlogic.gdx.graphics.Color;

import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.singletons.GameManager;
import com.mygdx.group17.shipocalypse.models.State;

public class MenuButton extends Button {

    private Object value;
    private Color color = Color.RED;

    public MenuButton(ShapeRenderer shape, int x, int y, String text,  int width) {
        super(shape, x, y, text, width);

    }
    public MenuButton(ShapeRenderer shape, int x, int y, String text) {
        super(shape, x, y, text);
    }

    public MenuButton(ShapeRenderer shape, int x, int y, String tekst, Object v) {
        super(shape, x, y, tekst);
        this.value = v;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean handleInput() {

        if (Gdx.input.isTouched()) {

            // Use viewport/AssetManager to unproject input coordinates to game world coordinates.
            Vector3 input_vector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 projected_vector = AssetManager.unprojectInput(input_vector);
            Rectangle touch_rectangle = new Rectangle(projected_vector.x - 2, projected_vector.y - 2, 4, 4);

            if (touch_rectangle.overlaps(this.get_rectangle())) {
                return true;
            }
        }
        return false;
    }
    public void select() {
        this.color = Color.BLUE;
    }

    public void deselect() {
        this.color = Color.RED;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public void dispose() {

    }
}
