package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.math.Rectangle;


public class Tile {

    public static final int TILE_SIZE = 61;
    public int _posx;
    public int _posy;
    public int _index_x;
    public int _index_y;
    public boolean occupied;
    private boolean selected;

    public Tile(int posx, int posy, int index_x, int index_y) {
        _posx = posx;
        _posy = posy;
        _index_y = index_y;
        _index_x = index_x;
        selected = false;
    }

    public Rectangle get_rectangle() {
        return new Rectangle(_posx, _posy, TILE_SIZE, TILE_SIZE);
    }

    public Rectangle getCenter() { return new Rectangle(_posx + TILE_SIZE/2, _posy + TILE_SIZE/2, 5, 5); }

    public Integer[] getIndex() {
        Integer[] result = new Integer[2];
        result[0] = _index_x;
        result[1] = _index_y;
        return result;
    }

    public boolean isOccupied() { return occupied; }

    public Tile assign() {
        occupied = true;
        return this;
    }

    public void unAssign() {
        occupied = false;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

}
