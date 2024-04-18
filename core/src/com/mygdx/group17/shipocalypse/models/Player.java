package com.mygdx.group17.shipocalypse.models;


import java.util.Dictionary;
import com.mygdx.group17.shipocalypse.models.BoatConfiguration;

public class Player {
    public boolean defeated = false;
    public Grid grid;

    public BoatConfiguration boat_configuration;

    public Player(int gridSizeX, int gridSizeY, BoatConfiguration boats) {
        grid = new Grid(gridSizeX, gridSizeY);
        boat_configuration = boats;
    }

    public Grid get_grid() {
        return grid;
    }

    public void checkDefeat() {
        boolean isDefeated = true;
        for (Boat boat : boat_configuration.boats) {
            if (!boat.isSunk()) {
                isDefeated = false;
                break;
            }
        }
        defeated = isDefeated;
    }

    public boolean allShipsSunk() {
        return defeated;
    }

    public BoatConfiguration getBoatConfig() {
        return boat_configuration;
    }


    // FOLLOWING IS REQUIRED FOR FIREBASE SERIALIZATION
    // These methods are not used directly by our app, but must exist.
    public Player() {}
    public boolean getDefeated() { return defeated; }
    public Grid getGrid() { return grid; }
    public BoatConfiguration getBoat_configuration() { return boat_configuration; }
    public void setDefeated(boolean _defeated) { defeated = _defeated; }
    public void setGrid(Grid _grid) { grid = _grid; }
    public void setBoat_configuration(BoatConfiguration _boat_configuration) { boat_configuration = _boat_configuration; }


}
