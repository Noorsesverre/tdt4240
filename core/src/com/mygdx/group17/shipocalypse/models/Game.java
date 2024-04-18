package com.mygdx.group17.shipocalypse.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public HashMap<String, Object> createSerializableData() {
        return new HashMap<String, Object>() {
            {
                put("grid_size", players[0].get_grid()._tiles.length);
                put("id", id);
                put("player_turn", player_turn);
                put("users", new HashMap<Object, Object>() {
                    {
                        int i = 0;
                        for (String user : users) {
                            put(String.valueOf(i), user);
                            ++i;
                        }
                    }
                });
                put("players", new HashMap<Object, Object>() {
                    {
                        int i = 0;
                        for (Player player : players) {
                            if (player != null) {
                                put(String.valueOf(i), new HashMap<Object, Object>() {
                                    {
                                        put("grid", new HashMap<Object, Object>() {
                                            {
                                                int x = 0;
                                                for (Tile[] tileset : player.get_grid().get_tiles_aslist()) {
                                                    put(String.valueOf(x), new HashMap<Object, Object>() {
                                                        {
                                                            int y = 0;
                                                            for (Tile tile : tileset) {
                                                                put(String.valueOf(y), new HashMap<Object, Object>() {
                                                                    {
                                                                        put("occupied", tile.isOccupied());
                                                                        put("is_hit", tile.isIs_hit());
                                                                        put("burning", tile.isBurning());
                                                                    }
                                                                });
                                                                ++y;
                                                            }
                                                        }
                                                    });
                                                    ++x;
                                                }
                                            }
                                        });
                                        put("boatconfig", new HashMap<Object, Object>() {
                                            {
                                                int z = 0;
                                                for (Boat boat : player.getBoat_configuration().boats) {
                                                    put(String.valueOf(z), new HashMap<Object, Object>() {
                                                        {
                                                            put("display", boat.getDisplay());
                                                            put("posx", boat.get_posx());
                                                            put("posy", boat.get_posy());
                                                            put("boatSize", boat.get_boatSize());
                                                            put("isVertical", boat.get_isVertical());
                                                            put("sunk", boat.isSunk());
                                                            put("tiles", new HashMap<Object, Object>() {
                                                                {
                                                                    int t = 0;
                                                                    for (Tile tile : boat.getTiles()) {
                                                                        put(String.valueOf(tile.get_index_x()), String.valueOf(tile.get_index_y()));
                                                                    }
                                                                    ++t;
                                                                }
                                                            });
                                                            put("hits", new HashMap<Object, Object>() {
                                                                {
                                                                    int h = 0;
                                                                    for (boolean hit : boat.getHits()) {
                                                                        put(String.valueOf(h), hit);
                                                                    }
                                                                    ++h;
                                                                }
                                                            });
                                                        }
                                                    });
                                                    ++z;
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                });
            }
        };
    }

    public Game deserializeFirebaseData(HashMap<String, Object> data) {
        Game fetched_game = new Game();
        for (String key : data.keySet()) {
            switch (key) {
                case "grid_size":
                    break;
                case "id":
                    fetched_game.setId((String) data.get(key));
                    break;
                case "player_turn":
                    fetched_game.setPlayer_turn((int) data.get(key));
                    break;
                case "users":
                    HashMap<String, Object> data_user_map = (HashMap<String, Object>) data.get(key);
                    String[] data_users = new String[2];
                    for (String i : data_user_map.keySet()) {
                        data_users[Integer.valueOf(i)] = (String) data_user_map.get(i);
                    }
                    fetched_game.setUsers(data_users);
                    break;
                case "players":
                    HashMap<String, HashMap<String, Object>> data_player_map = (HashMap<String, HashMap<String, Object>>) data.get(key);
                    Player[] data_players = new Player[2];
                    for (String i : data_player_map.keySet()) {
                        Player player = new Player();
                        for (String player_key : data_player_map.get(i).keySet()) {
                            switch(player_key) {
                                case "grid":
                                    Grid player_grid = new Grid();
                                    HashMap<String, HashMap<String, HashMap<String, Object>>> data_grid_map = (HashMap<String, HashMap<String, HashMap<String, Object>>>) data_player_map.get(i).get(player_key);
                                    Tile[][] fetched_tiles = new Tile[(int) data.get("grid_size")][(int) data.get("grid_size")];
                                    for (String grid_xkey : data_grid_map.keySet()) {
                                        HashMap<String, HashMap<String, Object>> data_grid_key_map = (HashMap<String, HashMap<String, Object>>) data_grid_map.get(grid_xkey);
                                        for (String grid_ykey : data_grid_key_map.keySet()) {
                                            Tile fetched_tile = new Tile();
                                            fetched_tile.setIs_hit((boolean)data_grid_key_map.get(grid_ykey).get("is_hit"));
                                            fetched_tile.setBurning((boolean)data_grid_key_map.get(grid_ykey).get("burning"));
                                            fetched_tile.setOccupied((boolean)data_grid_key_map.get(grid_ykey).get("occupied"));
                                            fetched_tiles[Integer.valueOf(grid_xkey)][Integer.valueOf(grid_ykey)] = fetched_tile;
                                        }
                                    }
                                    break;
                                case "boatconfig":
                                    break;
                            }
                        }
                        fetched_game.setPlayers(data_players);
                    }

                    break;
            }
        }
        return fetched_game;
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

    public void setPlayers(Player[] _players) { players = _players; }
    public void setUsers(String[] _users) { users = _users; }
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
