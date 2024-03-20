package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.Boat;

public class BoatButton extends Button {

    private Texture _texture;

    public int boat_size;

    public BoatButton(ShapeRenderer shape, int x, int y, Texture texture, int boat_size) {
        super(shape, x, y);
        _texture = texture;
        this.boat_size = boat_size;
    }

    public void render(SpriteBatch batch) {

        Sprite icon = new Sprite(_texture);

        icon.setScale(0.5f, 0.5f);

        icon.setPosition(get_position().x, get_position().y);

        icon.draw(batch);

        BitmapFont bf = new BitmapFont();

        bf.draw(batch, "0", get_position().x + _texture.getWidth() / 2, get_position().y);
    }

    public Texture get_texture() {
        return _texture;
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {

    }
}
