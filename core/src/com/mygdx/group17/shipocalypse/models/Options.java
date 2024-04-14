package com.mygdx.group17.shipocalypse.models;

import com.badlogic.gdx.graphics.Color;

import java.util.LinkedHashMap;

public class Options {

    public static final int GAME_WIDTH = 540;
    public static final int GAME_HEIGHT = 960;

    public static final Color[] BG_COLOR = new Color[] {Color.CYAN, Color.WHITE};

    public static LinkedHashMap<String, LinkedHashMap<String, Object>> options = new LinkedHashMap<String, LinkedHashMap<String, Object>>() {
        {
            put("grids", new LinkedHashMap<String, Object>() {
                {
                    put("8x8", 8);
                    put("10x10", 10);
                    put("12x12", 12);
                }
            });
            put("times", new LinkedHashMap<String, Object>() {
                {
                    put("1 min", 1);
                    put("2 min", 2);
                    put("5 min", 5);
                }
            });
            put("boats", new LinkedHashMap<String, Object>() {
                {
                    put("Few", new LinkedHashMap<Integer, Integer>() {
                        {
                            put(1,1);
                            put(2,1);
                            put(3,1);
                            put(4,1);
                        }
                    });
                    put("More", new LinkedHashMap<Integer, Integer>() {
                        {
                            put(1,2);
                            put(2,1);
                            put(3,2);
                            put(4,1);
                        }
                    });
                    put("Many", new LinkedHashMap<Integer, Integer>() {
                        {
                            put(1,3);
                            put(2,2);
                            put(3,3);
                            put(4,2);
                        }
                    });

                }

            });
        }};

    public static LinkedHashMap<String, LinkedHashMap> defaults = new LinkedHashMap<String, LinkedHashMap>() {
        {
            put("grids", new LinkedHashMap<String, Object>() {
                {
                    put("10x10", 10);
                }
            });
            put("times", new LinkedHashMap<String, Object>() {
                {
                    put("5 min", 5);
                }
            });
            put("boats", new LinkedHashMap<String, Object>() {
                {
                    put("Few", options.get("boats").get("Small"));
                }
            });
        }
    };
}
