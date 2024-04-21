package com.mygdx.group17.shipocalypse.models;


import java.util.ArrayList;

public class BoatConfiguration {

    public ArrayList<Boat> boats;

    public BoatConfiguration() {
        this.boats = new ArrayList<>();
    }

    public void AddBoat(Boat boat, ArrayList<Tile> tiles) {
        boats.add(boat);
        boat.addTiles(tiles);
    }

    public String debug() {
        StringBuilder debug_string = new StringBuilder();
        for (Boat boat : boats) {
            debug_string.append("BOAT: \n");
            debug_string.append("- size : ").append(boat.getSize()).append("\n");
            debug_string.append("- position : ").append(boat._posx).append(" - ").append(boat._posy).append("\n");
        }
        return debug_string.toString();
    }
    public void RemoveBoat(Boat boat) {
        for (Tile tile : boat.getTiles()) {
            tile.unAssign();
        }
        this.boats.remove(boat);
    }

    // FOLLOWING IS REQUIRED FOR FIREBASE SERIALIZATION
    // These methods are not used directly by our app, but must exist.
    public ArrayList<Boat> getBoats() { return boats; }
    public void setBoats(ArrayList<Boat> _boats) { boats = _boats; }


}
