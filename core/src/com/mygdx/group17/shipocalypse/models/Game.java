package com.mygdx.group17.shipocalypse.models;

import java.util.ArrayList;

public class Game {
    public Player[] players;
    public String[] users;
    public int player_turn;
    public String id;

    public Game(String host_id, String game_id) {
        player_turn = 0;
        players = new Player[2];
        users = new String[2];
        users[0] = host_id;
        id = game_id;
    }

    public void addPlayer(Player player, int index) {
        players[index] = player;
    }

    public boolean hasTurn(String user_id) {
        return users[player_turn] == user_id;
    }

    public boolean isJoinable() {
        return players[1] == null;
    }

    public void swapTurn() {
        if (player_turn == 0) {
            player_turn = 1;
        } else {
            player_turn = 0;
        }
    }


    // FOLLOWING IS REQUIRED FOR FIREBASE SERIALIZATION
    // These methods are not used directly by our app, but must exist.

    public Game() {}

    public void setPlayers(ArrayList<Player> _players) {
        players = new Player[2];
        players[0] = _players.get(0);
        players[1] = _players.get(1);
    }
    public void setUsers(ArrayList<String> _users) {
        users = new String[2];
        users[0] = _users.get(0);
        users[1] = _users.get(1);
        }
    public void setPlayer_turn(int _player_turn) { player_turn = _player_turn; }
    public void setId(String _id) { id = _id; }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<Player>() {
            {
                add(players[0]);
                add(players[1]);
            }
        };
    }
    public ArrayList<String> getUsers() {
        return new ArrayList<String>() {
            {
                add(users[0]);
                add(users[1]);
            }
        };
    }
    public int getPlayer_turn() { return player_turn; }
    public String getId() { return id; }

}
