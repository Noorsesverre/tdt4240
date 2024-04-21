package com.mygdx.group17.shipocalypse.models;


import java.util.ArrayList;

public class Grid {

    private final Tile[][] _tiles;
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

                _tiles[x][y] = new Tile(
                        GRID_POS_X + x * GRID_GAP + x * Tile.TILE_SIZE,
                        GRID_POS_Y + y * GRID_GAP + y * Tile.TILE_SIZE,
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

    public Tile get_tile(int x, int y) { return _tiles[x][y]; }

    public int getSize() { return _tiles.length; }


    public ArrayList<Tile> get_surrounding_tiles(Tile center_tile) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        tiles.add(center_tile);

        tiles.addAll(center_tile.getAdjacentTiles());

        // NOTE: Lazy mans way of avoiding out of range index errors
        try {
            tiles.add(_tiles[center_tile._index_x - 1][center_tile._index_y + 1]);
        } catch (Exception e){ }

        try {
            tiles.add(_tiles[center_tile._index_x - 1][center_tile._index_y - 1]);

        } catch (Exception e){ }

        try {
            tiles.add(_tiles[center_tile._index_x + 1][center_tile._index_y + 1]);
        } catch (Exception e){ }

        try {
            tiles.add(_tiles[center_tile._index_x + 1][center_tile._index_y - 1]);
        } catch (Exception e){ }

        return tiles;
    }

    public Tile get_random_tile() {
        int random_x = (int) (Math.random() * getSize());
        int random_y = (int) (Math.random() * getSize());
        return _tiles[random_x][random_y];
    }
}
