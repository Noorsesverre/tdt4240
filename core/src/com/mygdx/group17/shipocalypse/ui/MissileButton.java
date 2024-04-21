package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.group17.shipocalypse.models.Missile;
import com.mygdx.group17.shipocalypse.models.MissileType;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;

public class MissileButton extends Button {
    private Missile missile;

    public MissileButton(ShapeRenderer shape, int x, int y, int width, Missile missile) {
        super(shape, x, y, missile.getMissileText(), width);
        this.missile = missile;
    }

    public Missile getMissile() {
        return missile;
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

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}

