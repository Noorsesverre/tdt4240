package com.mygdx.group17.shipocalypse.models;


import java.util.Dictionary;
import com.mygdx.group17.shipocalypse.models.BoatConfiguration;

public class Player {
    private boolean defeated = false;
    private Grid grid;
    private BoatConfiguration boat_configuration;
    private String player_id;
    private boolean skipable_player;

    public boolean is_skipable_player() {
        return skipable_player;
    }
    public String getPlayer_id() {
        return player_id;
    }

    public Player(int gridSizeX, int gridSizeY, BoatConfiguration boats, String player_id) {
        this(gridSizeX,gridSizeY,boats,player_id,false);
    }

    public Player(int gridSizeX, int gridSizeY, BoatConfiguration boats, String player_id, boolean is_skipable) {
        grid = new Grid(gridSizeX, gridSizeY);
        boat_configuration = boats;
        this.player_id = player_id;
        this.skipable_player = is_skipable;
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

    public void hit_random_tile() {
        boolean found_tile = false;
        while (!found_tile) {
            int random_x = (int) (Math.random() * grid.getSize());
            int random_y = (int) (Math.random() * grid.getSize());

            System.out.print("Random hit (" + random_x + ", " + random_y+")");

            Tile chosen_one = grid.get_tiles()[random_x][random_y];

            if (!chosen_one.isHit() || !chosen_one.isExposed()) {
                grid.get_tiles()[random_x][random_y].hit();
                found_tile = true;
            }
            for (Boat boat: boat_configuration.boats) {
                for (Tile boat_tile: boat.getTiles()) {
                    if (chosen_one == boat_tile) {
                        boat.hit(boat_tile);
                    }
                }
            }
        }
    }

    public boolean allShipsSunk() {
        return defeated;
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
