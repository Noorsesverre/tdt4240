package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;


public class Tile {

    ArrayList<Tile> adjacent_tiles;

    public static final int TILE_SIZE = 30;
    public int _posx;
    public int _posy;
    public int _index_x;
    public int _index_y;
    public boolean occupied;
    public boolean selected;

    public boolean is_hit;

    public boolean burning;

    public Tile(int posx, int posy, int index_x, int index_y) {
        _posx = posx;
        _posy = posy;
        _index_y = index_y;
        _index_x = index_x;
        is_hit = false;
        selected = false;
        burning = false;
        adjacent_tiles = new ArrayList<>();
    }
    public void addAdjacentTile(Tile tile) {
        adjacent_tiles.add(tile);
    }
    public ArrayList<Tile> getAdjacentTiles() {
        return(adjacent_tiles);
    }
    public Rectangle get_rectangle() {
        return new Rectangle(_posx, _posy, TILE_SIZE, TILE_SIZE);
    }

    public Rectangle getCenter() { return new Rectangle(_posx + TILE_SIZE/2, _posy + TILE_SIZE/2, 2, 2); }

    public Integer[] getIndex() {
        Integer[] result = new Integer[2];
        result[0] = _index_x;
        result[1] = _index_y;
        return result;
    }

    public void setBurning() {
        burning = true;
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
    public void hit() {
        is_hit = true;
    }
    public boolean isHit() { return is_hit; }

    public void shiftDown() {
        _posy = _posy - 100;
    }
    public void shiftUp() {
        _posy = _posy + 300;
    }
}
