package com.mygdx.group17.shipocalypse.models;

import java.util.HashMap;
import java.util.Map;

public class Gameconfig {
    private static int grid_x;
    private static int grid_y;

    private static Map<Integer, Integer> allowed_boats;

    public Gameconfig() {
        allowed_boats = new HashMap<>();
    }

    public Gameconfig(int x, int y) {
        grid_x = x;
        grid_y = y;
        allowed_boats = new HashMap<>();
    }

    public Gameconfig(int x, int y, Map<Integer, Integer> boats) {
        grid_x = x;
        grid_y = y;
        allowed_boats = boats;
    }

    public static void setGrid_x(int x) {
        grid_x = x;
    }

    public static void setGrid_y(int y) {
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

    public static int getGrid_x() {
        return grid_x;
    }

    public static int getGrid_y() {
        return grid_y;
    }

    public Map<Integer, Integer> getAllowed_boats() {
        return allowed_boats;
    }
}
