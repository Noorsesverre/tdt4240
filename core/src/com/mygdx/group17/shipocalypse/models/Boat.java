package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.Shipocalypse;

public class Boat {

    public int _posx;
    public int _posy;
    public int _boatSize;
    public boolean _isVertical = false;
    public boolean _isPlaced = false;
    public Texture _texture;

    public Boat (int posx, int posy, int boatSize) {
        _posx = posx;
        _posy = posy;
        _boatSize = boatSize;
        _texture = FindCorrectTexture(boatSize);
    }

    private Texture FindCorrectTexture(int boatSize) {
        if (boatSize < 1 || boatSize > 4) {
            throw new RuntimeException("Boat size needs to be in the range of 1-4");
        }

        return new Texture("ship" + boatSize + ".png");
    }

    public void render(SpriteBatch batch) {
        Sprite sprite = new Sprite(_texture);
        sprite.setPosition(_posx, _posy);

        if (_isVertical) {
            sprite.rotate(90);
        }

        sprite.draw(batch);
    }

    public void handleInput() {
        if (Gdx.input.isTouched()) {
            int input_x = Gdx.input.getX();
            int input_y = Shipocalypse.GAME_HEIGHT - Gdx.input.getY();

            Rectangle touch_rectangle = new Rectangle(input_x - 1, input_y - 1, 2, 2);


        }

    }

    public void turn_boat() {
        this._isVertical = !this._isVertical;
    }

    public Rectangle get_rectangle() {
        return new Rectangle(_posx, _posy, _texture.getWidth(), _texture.getHeight());
    }
}
