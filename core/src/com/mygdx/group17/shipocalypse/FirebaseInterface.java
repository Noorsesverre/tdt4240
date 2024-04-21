package com.mygdx.group17.shipocalypse;

import com.mygdx.group17.shipocalypse.models.Game;

import java.util.HashMap;
import java.util.Map;

public interface FirebaseInterface {

    String createGame(HashMap<String, Object> options);

    void addUser(String user_id);

    void hostGame(Game game);
    void updateGame(Game game);
    void postGridInfo(String game_id, String sender_id, String grid_info);

    HashMap<String, HashMap<String, Object>> getOpenGames();

    void playerReady(String user_id, String game_id);
    boolean bothPlayersReady(String game_id);
    void joinGame(String game_id, String user_id);
    boolean checkTurn(String game_id, String user_id);
    void postTurnInfo(String game_id, String sender_id, HashMap<Object, Object> turn_info);
    HashMap<String, Object> getTurnInfo(String game_id);
    String getOpponentInfo(String game_id);

    void removeGame(String game_id);

    }
