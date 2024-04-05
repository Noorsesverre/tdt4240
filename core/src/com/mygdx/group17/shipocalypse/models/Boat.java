package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Boat {

    public int _posx;
    public int _posy;
    public int _boatSize;
    public boolean _isVertical = false;
    public Texture _texture;

    private ArrayList<Tile> tiles;

    public Boat (int posx, int posy, int boatSize) {
        _posx = posx;
        _posy = posy;
        _boatSize = boatSize;
        _texture = FindCorrectTexture(boatSize);
    }

    public void addTiles(ArrayList<Tile> list) {
        tiles = list;
        for (Tile tile : tiles) {
            tile.assign();
        }
    }

    public void setPosition(int x, int y) {
        _posx = x;
        _posy = y;
    }

    public static Texture FindCorrectTexture(int boatSize) {
        if (boatSize < 1 || boatSize > 4) {
            throw new RuntimeException("Boat size needs to be in the range of 1-4");
        }

        return new Texture("ship" + boatSize + ".png");
    }

    public void render(SpriteBatch batch) {
        Sprite sprite = new Sprite(_texture);
        sprite.setPosition(_posx , _posy);
        sprite.setOrigin(Tile.TILE_SIZE / 2, Tile.TILE_SIZE / 2);

        if (_isVertical) {
            sprite.rotate(90);
        }

        sprite.draw(batch);
    }

    public void turn_boat() {
        this._isVertical = !this._isVertical;
    }

    public Rectangle get_rectangle() {
        if (_isVertical) {
            return new Rectangle(_posx, _posy, _texture.getHeight(), _texture.getWidth());
        }
        return new Rectangle(_posx, _posy, _texture.getWidth(), _texture.getHeight());
    }

    public Rectangle get_rectangle(int x, int y, boolean inversion, int origin) {
        if (_isVertical ^ inversion) {
            return new Rectangle(x, y - (Tile.TILE_SIZE*origin) - 5*(origin-1), _texture.getHeight(), _texture.getWidth());
        }
        return new Rectangle(x - (Tile.TILE_SIZE*origin) - 5*(origin-1), y, _texture.getWidth(), _texture.getHeight());
    }

    public int getSize() {
        return _boatSize;
    }

    public ArrayList<Tile> getTiles() {

        return tiles;
    }

}
