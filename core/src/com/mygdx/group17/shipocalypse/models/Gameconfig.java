package com.mygdx.group17.shipocalypse.models;

import java.util.HashMap;
import java.util.Map;

public class Gameconfig {
    private int grid_x;
    private int grid_y;

    private final Map<Integer, Integer> allowed_boats;

    public Gameconfig() {
        allowed_boats = new HashMap<>();
    }

    public Gameconfig(int grid_x, int grid_y) {
        this.grid_x = grid_x;
        this.grid_y = grid_y;
        allowed_boats = new HashMap<>();
    }

    public Gameconfig(int grid_x, int grid_y, Map<Integer, Integer> allowed_boats) {
        this.grid_x = grid_x;
        this.grid_y = grid_y;
        this.allowed_boats = allowed_boats;
    }

    public void setGrid_x(int grid_x) {
        this.grid_x = grid_x;
    }

    public void setGrid_y(int grid_y) {
        this.grid_y = grid_y;
    }

    public void increase_allowed_boat_type(int boat_size) {
        allowed_boats.put(boat_size, allowed_boats.get(boat_size) + 1 );
    }

    public boolean subtract_allowed_boat_type(int boat_size) {
        int current_value = allowed_boats.get(boat_size);

        if (current_value - 1 < 0) {
            return false;
        } else {
            allowed_boats.put(boat_size, current_value - 1);
            return true;
        }
    }

    public int getGrid_x() {
        return grid_x;
    }

    public int getGrid_y() {
        return grid_y;
    }

    public Map<Integer, Integer> getAllowed_boats() {
        return allowed_boats;
    }
}
