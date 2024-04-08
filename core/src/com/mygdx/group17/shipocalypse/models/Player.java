package com.mygdx.group17.shipocalypse.models;


import java.util.Dictionary;
import com.mygdx.group17.shipocalypse.models.BoatConfiguration;

public class Player {

    private Grid grid;

    private BoatConfiguration boat_configuration;
    private Tile current_target;

    public Player(int gridSizeX, int gridSizeY, BoatConfiguration boats) {
        grid = new Grid(gridSizeX, gridSizeY);
        boat_configuration = boats;
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

    public void setBoatConfig(BoatConfiguration boatconfig) {
        boat_configuration = boatconfig;
    }

    public BoatConfiguration getBoatConfig() {
        return boat_configuration;
    }

    public int getGridSize() {
        return grid.getSize();
    }


}
