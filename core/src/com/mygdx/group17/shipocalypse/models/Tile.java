package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.css.Rect;

public class Tile {

    public static final int TILE_SIZE = 61;

    public int _posx;
    public int _posy;

    public int _index_x;
    public int _index_y;

    public Tile(int posx, int posy, int index_x, int index_y) {
        _posx = posx;
        _posy = posy;
        _index_y = index_y;
        _index_x = index_x;
    }

    public Rectangle get_rectangle() {
        return new Rectangle(_posx, _posy, TILE_SIZE, TILE_SIZE);
    }



}
