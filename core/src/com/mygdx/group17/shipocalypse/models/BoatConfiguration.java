package com.mygdx.group17.shipocalypse.models;


import java.util.ArrayList;
import java.util.List;

public class BoatConfiguration {

    public List<Boat> boats;

    public BoatConfiguration() {
        this.boats = new ArrayList<>();
    }

    public void AddBoat(Boat boat, ArrayList<Tile> tiles) {
        boats.add(boat);
        boat.addTiles(tiles);
    }
    public void RemoveBoat(Boat boat) {
        for (Tile tile : boat.getTiles()) {
            tile.unAssign();
        }
        this.boats.remove(boat);
    }

    public void RotateBoat(Boat _boat) {
        _boat.turn_boat();
    }

    public void debug() {
        for (Boat boat : boats) {
            System.out.println("X: " + String.valueOf(boat._posx) + ", Y: " + String.valueOf(boat._posy) + ", S: " + String.valueOf(boat._boatSize));
        }
    }

}
