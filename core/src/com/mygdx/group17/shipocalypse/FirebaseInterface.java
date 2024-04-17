package com.mygdx.group17.shipocalypse;

import com.mygdx.group17.shipocalypse.controllers.GameState;
import com.mygdx.group17.shipocalypse.controllers.PlayState;

import java.util.Map;

public interface FirebaseInterface {
    void writeToDatabase(String gameID, Map<String, Object> data);

    String createGame(int gridX, int gridY, Map<Integer, Integer> boats);

    }
