package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class BoatConfiguration {

    public List<Boat> boats;

    public BoatConfiguration() {
        this.boats = new ArrayList<>();
    }

    public void AddBoat(int pos_x, int pos_y, int boat_size) {
        /*
            TODO: Create checks of previous boats
            so that they don't overlap and are "near" each other.
            Near each other, atleast one tile between, no diagonal allowed
        */

        boats.add(new Boat(pos_x, pos_y, boat_size));
    }

    public void RemoveBoat(Boat boat) {
        this.boats.remove(boat);
    }

    public void RotateBoat(Boat _boat) {
        _boat.turn_boat();
    }
}
