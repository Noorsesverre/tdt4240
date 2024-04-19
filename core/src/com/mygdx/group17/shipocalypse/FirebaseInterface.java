package com.mygdx.group17.shipocalypse;

import com.mygdx.group17.shipocalypse.controllers.GameState;
import com.mygdx.group17.shipocalypse.controllers.PlayState;
import com.mygdx.group17.shipocalypse.models.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface FirebaseInterface {
    void writeToDatabase(String gameID, Map<String, Object> data);

    String createGame(HashMap<String, Object> options);

    void addUser(String user_id);

    void hostGame(Game game);
    void updateGame(Game game);
    void postGridInfo(String game_id, String sender_id, String grid_info);

    HashMap<String, HashMap<String, Object>> getOpenGames();

    void playerReady(String user_id, String game_id);
    boolean bothPlayersReady(String game_id);

    }
