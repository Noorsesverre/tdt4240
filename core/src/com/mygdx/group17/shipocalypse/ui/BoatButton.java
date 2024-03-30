package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BoatButton extends Button {

    private Texture _texture;
    public int max_allowed_boats;
    public int boat_size;
    public boolean isVertical = false;

    public BoatButton(ShapeRenderer shape, int x, int y, int boat_size, int max_allowed_boats) {
        super(shape, x, y);
        _texture = new Texture("ship" + boat_size + ".png");
        this.boat_size = boat_size;
        this.max_allowed_boats = max_allowed_boats;
    }

    public void render(SpriteBatch batch) {

        Sprite icon = new Sprite(_texture);

        icon.setScale(0.5f, 0.5f);

        icon.setPosition(get_position().x, get_position().y);

        icon.draw(batch);

        BitmapFont bf = new BitmapFont();

        bf.draw(batch, String.valueOf(max_allowed_boats), get_position().x + _texture.getWidth() / 2, get_position().y);
    }

    public Texture get_texture() {
        return _texture;
    }

    @Override
    public boolean handleInput() {
        return false;
    }

    @Override
    public void dispose() {

    }

    public boolean getNextOrientation() {
        this.isVertical = !this.isVertical;

        return this.isVertical;
    }
}
