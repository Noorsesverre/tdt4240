package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;

public class BoatButton extends Button {

    private final int max_allowed_boats;
    private final int boat_size;
    private final int x;
    private final int y;
    private boolean isVertical = false;
    private final Texture texture;
    private final Sprite sprite;

    public BoatButton(ShapeRenderer shape, int x, int y, int boat_size, int max_allowed_boats) {
        super(shape, x, y);
        this.x = x;
        this.y = y;
        this.boat_size = boat_size;
        this.max_allowed_boats = max_allowed_boats;
        this.texture = AssetManager.ship_textures.get(this.boat_size - 1);
        this.sprite = AssetManager.ship_sprites.get(this.boat_size - 1);
    }

    public void render() {
        sprite.setScale(0.5f, 0.5f);
        sprite.setPosition(get_position().x, get_position().y);
        AssetManager.draw(sprite);
        BitmapFont bf = new BitmapFont();
        AssetManager.write(String.valueOf(max_allowed_boats), (int)get_position().x + texture.getWidth() / 2, (int)get_position().y);
    }

    @Override
    public Rectangle get_rectangle() {
        return new Rectangle(x, y, texture.getWidth(), texture.getHeight());
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
