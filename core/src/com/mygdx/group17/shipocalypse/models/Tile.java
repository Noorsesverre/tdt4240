package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;


public class Tile {
    public ArrayList<Tile> adjacent_tiles;
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

    // FOLLOWING IS REQUIRED FOR FIREBASE SERIALIZATION
    // These methods are not used directly by our app, but must exist.
    public Tile() {}
    public ArrayList<Tile> getAdjacent_tiles() { return adjacent_tiles; }
    public int get_posx() { return _posx; }
    public int get_posy() { return _posy; }
    public int get_index_x() { return _index_x; }
    public int get_index_y() { return _index_y; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    public void setSelected(boolean selected) { this.selected = selected; }
    public boolean isIs_hit() { return is_hit; }
    public void setIs_hit(boolean is_hit) { this.is_hit = is_hit; }
    public boolean isBurning() { return burning; }
    public void setAdjacent_tiles(ArrayList<Tile> adjacent_tiles) { this.adjacent_tiles = adjacent_tiles; }
    public void set_posx(int _posx) { this._posx = _posx; }
    public void set_posy(int _posy) { this._posy = _posy; }
    public void set_index_x(int _index_x) { this._index_x = _index_x; }
    public void setBurning(boolean burning) { this.burning = burning; }

}
