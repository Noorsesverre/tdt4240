package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Dictionary;
import java.util.List;

public class Player {

    private Grid grid;
    private Dictionary<String, Integer> _boatConfig;
    private Tile current_target;

    public Player(int gridSizeX, int gridSizeY) {
        grid = new Grid(gridSizeX, gridSizeY);
    }

    public Grid get_grid() {
        return grid;
    }

    public void select_target(int x, int y) {

        // add some checks to see if target is valid
//        current_target = Grid[x][y];
    }

    public void fire() {

    }
}
