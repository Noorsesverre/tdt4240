package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

public class Grid {

    private Tile[][] _tiles;

    public static final int GRID_POS_X = 200;
    public static final int GRID_POS_Y = 100;
    public static final int GRID_GAP = 5;


    public Grid(int sizeX, int sizeY) {
        _tiles = new Tile[sizeX][sizeY];

        for (int x = 0; x < sizeX; x++ )  {
            for (int y = 0; y < sizeY; y++) {
                int x_gap = GRID_GAP, y_gap = GRID_GAP;

                _tiles[x][y] = new Tile(
                        GRID_POS_X + x * x_gap + x * Tile.TILE_SIZE,
                        GRID_POS_Y + y * y_gap + y * Tile.TILE_SIZE,
                        x,
                        y
                );
            }
        }
    }

    public Tile[][] get_tiles() {
        return _tiles;
    }
}
