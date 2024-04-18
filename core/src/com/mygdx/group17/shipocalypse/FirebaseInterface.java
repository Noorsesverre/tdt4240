package com.mygdx.group17.shipocalypse;

import com.mygdx.group17.shipocalypse.controllers.GameState;
import com.mygdx.group17.shipocalypse.controllers.PlayState;
import com.mygdx.group17.shipocalypse.models.Game;

import java.util.Map;

public interface FirebaseInterface {
    void writeToDatabase(String gameID, Map<String, Object> data);

    String createGame(int gridX, int gridY, Map<Integer, Integer> boats);

    void addUser(String user_id);

    void hostGame(Game game);
    void updateGame(Game game);

    }
