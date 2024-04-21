package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;

import java.util.ArrayList;

public class Boat {

    public boolean display = false;

    public int _posx;
    public int _posy;
    public int _boatSize;
    public boolean _isVertical = false;
    public Texture _texture;
    private final boolean[] hits;
    private boolean sunk;
    private ArrayList<Tile> tiles;

    public Boat (int posx, int posy, int boatSize) {
        _posx = posx;
        _posy = posy;
        _boatSize = boatSize;
        hits = new boolean[_boatSize];
        sunk = false;
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

        return new Texture("ship" + boatSize + "_small.png");
    }

    public void render(SpriteBatch batch) {
        Sprite sprite = new Sprite(_texture);
        sprite.setPosition(_posx , _posy);
        sprite.setOrigin(Tile.TILE_SIZE / 2, Tile.TILE_SIZE / 2);

        if (_isVertical) {
            sprite.rotate(90);
        }

        AssetManager.draw(sprite);
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

    public void shiftDown() {
        _posy = _posy - 100;
    }
    public void shiftUp() {
        _posy = _posy + 300;
    }


    // For choosing random opponent positions...
    public void move() {
        double corr_x = Math.random() - 0.5;
        double corr_y = Math.random() - 0.5;
        corr_x = corr_x * 350;
        corr_y = corr_y * 350;
        _posx = _posx + (int) corr_x;
        _posy = _posy + (int) corr_y;
        if (_posx < 0 || _posx > Options.GAME_WIDTH) {
            _posx = Options.GAME_WIDTH / 2;
        }
        if (_posy < 0 || _posy > Options.GAME_HEIGHT) {
            _posy = Options.GAME_HEIGHT / 2;
        }
    }

    public boolean containsTile(Tile tile) {
        return tiles.contains(tile);
    }

    public boolean isSunk() { return sunk; }
    public void hit(Tile tile) {
        tile.setBurning();
        for (boolean hit : hits) {
            System.out.println(hit);
        }
        hits[tiles.indexOf(tile)] = true;
        for (boolean hit : hits) {
            System.out.println(hit);
        }
        boolean is_sunk = true;
        for (boolean hit : hits) {
            if (!hit) {
                is_sunk = false;
                break;
            }
        }
        sunk = is_sunk;
    }
    public void heal(Tile tile) {
        tile.removeBurning();
        tile.removeHit();
        hits[tiles.indexOf(tile)] = false;
        sunk = false;
        display = false;
    }
    public boolean[] getHits() { return hits; }

    public void show() {
        display = true;
        for (Tile tile : tiles) {
            for (Tile adjacent_tile : tile.getAdjacentTiles()) {
                adjacent_tile.hit();
            }
        }
    }

    public void expose(Tile targetTile) {
        targetTile.exposed();
    }
}
