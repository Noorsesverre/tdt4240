package com.mygdx.group17.shipocalypse.models;

import java.util.HashMap;
import java.util.Map;

public class GameConfig {
    private int grid_x;
    private int grid_y;

    private static Map<Integer, Integer> allowed_boats;

    public GameConfig() {
        allowed_boats = new HashMap<>();
    }

    public GameConfig(int x, int y) {
        grid_x = x;
        grid_y = y;
        allowed_boats = new HashMap<>();
    }

    public GameConfig(int x, int y, Map<Integer, Integer> boats) {
        grid_x = x;
        grid_y = y;
        allowed_boats = boats;
    }

    public void setGrid_x(int x) {
        grid_x = x;
    }

    public void setGrid_y(int y) {
        grid_y = y;
    }

    public static void increase_allowed_boat_type(int boat_size) {
        allowed_boats.put(boat_size, allowed_boats.get(boat_size) + 1 );
    }

    public static boolean subtract_allowed_boat_type(int boat_size) {
        int current_value = allowed_boats.get(boat_size);

        if (current_value - 1 < 0) {
            return false;
        } else {
            allowed_boats.put(boat_size, current_value - 1);
            return true;
        }
    }

    public boolean allBoatsPlaced() {
        boolean result = true;
        for (Integer boat_type : allowed_boats.keySet()) {
            if (allowed_boats.get(boat_type) != 0) {
                result = false;
            }
        }
        return result;
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
