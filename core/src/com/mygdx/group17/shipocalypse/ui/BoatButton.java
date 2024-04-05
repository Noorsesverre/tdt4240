package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;

public class BoatButton extends Button {

    private int max_allowed_boats;
    private int boat_size;
    private boolean isVertical = false;
    private Texture texture;
    private Sprite sprite;

    public BoatButton(ShapeRenderer shape, int x, int y, int boat_size, int max_allowed_boats) {
        super(shape, x, y);
        this.boat_size = boat_size;
        this.max_allowed_boats = max_allowed_boats;
        this.texture = AssetManager.ship_textures.get(this.boat_size - 1);
        this.sprite = AssetManager.ship_sprites.get(this.boat_size - 1);
    }

    public void render() {
        sprite.setScale(0.5f, 0.5f);
        sprite.setPosition(get_position().x, get_position().y);
        sprite.draw(AssetManager.batch);
        BitmapFont bf = new BitmapFont();
        bf.draw(AssetManager.batch, String.valueOf(max_allowed_boats), get_position().x + texture.getWidth() / 2, get_position().y);
    }

    public Texture getTexture() {
        return texture;
    }

    public int getSize() {
        return boat_size;
    }

    @Override
    public boolean handleInput() {
        return false;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
    public boolean getNextOrientation() {
        this.isVertical = !this.isVertical;
        return this.isVertical;
    }

    public int getAllowedBoats() {
        return max_allowed_boats;
    }
}
