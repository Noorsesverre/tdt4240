package com.mygdx.group17.shipocalypse.models;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class Options {
    public static HashMap<String, HashMap<String, Object>> options = new HashMap<String, HashMap<String, Object>>() {
        {
            put("grids", new HashMap<String, Object>() {
                {
                    put("8x8", 8);
                    put("10x10", 10);
                    put("12x12", 12);
                }
            });
            put("times", new HashMap<String, Object>() {
                {
                    put("1 min", 1);
                    put("2 min", 2);
                    put("5 min", 5);
                }
            });
            put("boats", new HashMap<String, Object>() {
                {
                    put("Few", new HashMap<Integer, Integer>() {
                        {
                            put(1,1);
                            put(2,1);
                            put(3,1);
                            put(4,1);
                        }
                    });
                    put("More", new HashMap<Integer, Integer>() {
                        {
                            put(1,2);
                            put(2,1);
                            put(3,2);
                            put(4,1);
                        }
                    });
                    put("Many", new HashMap<Integer, Integer>() {
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

    public static HashMap<String, HashMap> defaults = new HashMap<String, HashMap>() {
        {
            put("grids", new HashMap<String, Object>() {
                {
                    put("10x10", 10);
                }
            });
            put("times", new HashMap<String, Object>() {
                {
                    put("5 min", 5);
                }
            });
            put("boats", new HashMap<String, Object>() {
                {
                    put("Few", options.get("boats").get("Small"));
                }
            });
        }
    };
}
