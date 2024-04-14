package com.mygdx.group17.shipocalypse.models;

import com.mygdx.group17.shipocalypse.models.Options;


public class Grid {

    private Tile[][] _tiles;
    public int GRID_POS_X;
    public int GRID_POS_Y;
    public static final int GRID_GAP = 2;
    public static final int x_center = Options.GAME_WIDTH / 2;
    public static final int y_center = Options.GAME_HEIGHT / 2;

    public Grid(int sizeX, int sizeY) {
        GRID_POS_X = x_center - (sizeX * Tile.TILE_SIZE / 2);
        GRID_POS_Y = y_center - (sizeY * Tile.TILE_SIZE / 2);

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
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (x - 1 >= 0) {
                    _tiles[x][y].addAdjacentTile(_tiles[x-1][y]);
                }
                if (x + 1 < sizeX) {
                    _tiles[x][y].addAdjacentTile(_tiles[x+1][y]);
                }
                if (y - 1 >= 0) {
                    _tiles[x][y].addAdjacentTile(_tiles[x][y-1]);
                }
                if (y + 1 < sizeY) {
                    _tiles[x][y].addAdjacentTile(_tiles[x][y+1]);
                }
            }
        }
    }

    public Tile[][] get_tiles() {
        return _tiles;
    }


    public int getSize() { return _tiles.length; }


}
