package com.mygdx.group17.shipocalypse;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mygdx.group17.shipocalypse.models.Game;
import com.mygdx.group17.shipocalypse.models.Tile;
import com.mygdx.group17.shipocalypse.singletons.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AndroidFirebase implements FirebaseInterface{

    private final FirebaseDatabase db;

    public AndroidFirebase() {
        String reference_url = "https://shipocalypse-tdt4240-default-rtdb.europe-west1.firebasedatabase.app/";
        db = FirebaseDatabase.getInstance(reference_url);
    }

    @Override
    public String createGame(HashMap<String, Object> options) {
        DatabaseReference games = db.getReference("active_games");
        String game_id = UUID.randomUUID().toString().substring(0,6); // Generate a random pseudo-unique ID
        games.child(game_id).child("users").child(GameManager.getUserId()).child("status").setValue("host");
        games.child(game_id).child("users").child(GameManager.getUserId()).child("turn").setValue(true);
        games.child(game_id).child("users").child(GameManager.getUserId()).child("ready").setValue(false);
        games.child(game_id).child("options").setValue(options.toString());
        games.child(game_id).child("lastmove").setValue("NONE");
        return game_id;
    }
    @Override
    public void removeGame(String game_id) {
        DatabaseReference games = db.getReference("active_games");
        games.child(game_id).removeValue();
    }

    @Override
    public void playerReady(String user_id, String game_id) {
        DatabaseReference user = db.getReference("active_games/" + game_id + "/users/" + user_id);
        user.child("ready").setValue(true);
    }


    private void switchTurn(String game_id) {
        DatabaseReference users = db.getReference("active_games/" + game_id + "/users");
        Task<DataSnapshot> read = users.get();
        while (!read.isComplete()) {
           // System.out.println("reading from db... 1");
        }
        DataSnapshot snapshot = read.getResult();
        for (DataSnapshot user : snapshot.getChildren()) {
            if ((boolean) user.child("turn").getValue() == false) {
                users.child(user.getKey()).child("turn").setValue(true);
            } else {
                users.child(user.getKey()).child("turn").setValue(false);
            }
        }
    }

    @Override
    public boolean checkTurn(String game_id, String user_id) {
        DatabaseReference user = db.getReference("active_games/" + game_id + "/users/" + user_id);
        Task<DataSnapshot> read = user.get();
        while (!read.isComplete()) {
           // System.out.println("reading from db... 2");
        }
        DataSnapshot snapshot = read.getResult();
        return (boolean) snapshot.child("turn").getValue();
    }

    @Override
    public boolean bothPlayersReady(String game_id) {
        DatabaseReference users = db.getReference("active_games/" + game_id + "/users");
        Task<DataSnapshot> read = users.get();
        boolean ready = true;
        while (!read.isComplete()) {
          //  System.out.println("reading from db... 3");
        }
        DataSnapshot snapshot = read.getResult();
        if ((int)snapshot.getChildrenCount() < 2) {
            ready = false;
        }
        for (DataSnapshot user : snapshot.getChildren()) {
            if (user.child("ready").getValue() != null) {
                if (!(boolean) user.child("ready").getValue()) {
                    ready = false;
                }
            } else {
                ready = false;
            }
        }
        return ready;
    }

    @Override
    public HashMap<String, HashMap<String, Object>> getOpenGames() {
        DatabaseReference active_games = db.getReference("active_games");
        HashMap<String, HashMap<String, Object>> open_game_id = new HashMap<String, HashMap<String, Object>>();
        System.out.println(active_games.get());
        Task<DataSnapshot> read = active_games.get();
        while (!read.isComplete()) {
          //  System.out.println("reading from db... 4");
        }
        DataSnapshot snapshot = read.getResult();
        for (DataSnapshot game : snapshot.getChildren()) {
            if (game.child("users").getChildrenCount() < 2) {
                HashMap<String, Object> options = new HashMap<>();

                String options_string = (String) game.child("options").getValue();

                String times_string = options_string;
                times_string = times_string.split(" boats")[0];
                times_string = times_string.substring(1, times_string.length() - 1);

                String boats_string = options_string;
                boats_string = boats_string.split(", grids")[0];
                boats_string = boats_string.split(", ", 2)[1];

                String grids_string = options_string;
                grids_string = grids_string.split(", grids")[1];
                grids_string = "{grids" + grids_string;
                grids_string = grids_string.substring(1, grids_string.length() - 1);

                options.put("times", Integer.valueOf(times_string.split("=")[1]));

                String boat_options = boats_string.split("=", 2)[1];
                boat_options = boat_options.substring(1, boat_options.length() - 1);
                String[] keyValuePairs = boat_options.split(",");
                HashMap<Integer, Integer> boat_map = new HashMap<>();

                for (String pair : keyValuePairs) {
                    String[] entry = pair.split("=");
                    boat_map.put(Integer.valueOf(entry[0].trim()), Integer.valueOf(entry[1].trim()));
                }

                options.put("boats", boat_map);

                options.put("grids", Integer.valueOf(grids_string.split("=")[1]));
                open_game_id.put(game.getKey(), options);
            }
        }
        return open_game_id;
    }

    public void transferGameFromJoinedToActive(String game_id) {
        DatabaseReference joinable_games = db.getReference("joinable_games");
        DatabaseReference active_games = db.getReference("active_games");
        active_games.child(game_id).setValue(joinable_games.child(game_id).get()); // Copy the game from joinable to active
        joinable_games.child(game_id).removeValue(); // Delete from joinable
        active_games.child(game_id).child("host").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    active_games.child(game_id).child(snapshot.getValue(String.class)).setValue(1);
                }
            }
        });
        active_games.child(game_id).child("joiner").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    active_games.child(game_id).child(snapshot.getValue(String.class)).setValue(0);
                }
            }
        });
    }


    public void postTurnInfo(String game_id, String sender_id, HashMap<Object, Object> turn_info) {
        DatabaseReference turn = db.getReference("active_games/" + game_id + "/lastmove");
        turn.child("missile").setValue(String.valueOf(turn_info.get("missile")));
        turn.child("tile").removeValue();
        int i = 0;
        for (Tile tile : (ArrayList<Tile>) turn_info.get("tiles")) {
            turn.child("tile").child(String.valueOf(i)).child("X").setValue(String.valueOf(tile._index_x));
            turn.child("tile").child(String.valueOf(i)).child("Y").setValue(String.valueOf(tile._index_y));
            ++i;
        }
        switchTurn(game_id);
    }
    @Override
    public void postGridInfo(String game_id, String sender_id, String grid_info) {
        DatabaseReference grid = db.getReference("active_games/" + game_id + "/grids/" + sender_id);
        grid.setValue(grid_info);
    }

    public String getOpponentInfo(String game_id) {
        DatabaseReference grids = db.getReference("active_games/" + game_id + "/grids/");
        final String[] grid_info = new String[1];

        Task<DataSnapshot> read = grids.get();
        while (!read.isComplete()) {
           // System.out.println("reading from db... 5");
        }
        DataSnapshot snapshot = read.getResult();
        for (DataSnapshot child : snapshot.getChildren()) {
            if (!Objects.equals(child.getKey(), GameManager.getUserId())) {
                grid_info[0] = child.getValue(String.class);
            }
        }
        return grid_info[0];

    }

    public HashMap<String, Object> getTurnInfo(String game_id) {
        DatabaseReference move = db.getReference("active_games/" + game_id + "/lastmove");

        HashMap<String, Object> move_info = new HashMap<>();

        Task<DataSnapshot> read = move.get();

        while (!read.isComplete()) {
           // System.out.println("reading from db... 6");
        }
        DataSnapshot snapshot = read.getResult();
        for (DataSnapshot child : snapshot.getChildren()) {
            if (Objects.equals(child.getKey(), "missile")) {
                move_info.put("missile", Integer.valueOf((String) child.getValue()));
            } else if (Objects.equals(child.getKey(), "tile")) {
                ArrayList<HashMap<String, Integer>> tiles = new ArrayList<>();
                for (DataSnapshot tile : child.getChildren()) {
                    tiles.add(new HashMap<String, Integer>() {
                        {
                            put("X", Integer.valueOf((String) tile.child("X").getValue()));
                            put("Y", Integer.valueOf((String) tile.child("Y").getValue()));
                        }
                    });
                }
                move_info.put("tile", tiles);
            }
        }
        System.out.println(move_info);
        return move_info;
    }
    @Override
    public void joinGame(String game_id, String user_id) {
        DatabaseReference games = db.getReference("active_games");
        games.child(game_id).child("users").child(GameManager.getUserId()).child("status").setValue("joined");
        games.child(game_id).child("users").child(GameManager.getUserId()).child("turn").setValue(false);
        games.child(game_id).child("users").child(GameManager.getUserId()).child("ready").setValue(false);
    }


    @Override
    public void addUser(String user_id) {
        DatabaseReference user = db.getReference("users/" + user_id);
        user.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        return;
                    } else {
                        db.getReference("users").child(user_id).setValue("user");
                    }
                }
            }
        });
    }
    @Override
    public void hostGame(Game game) {
        DatabaseReference host = db.getReference("users/" + game.users[0]);
        host.child("games").child(game.id).setValue("hosting"); // Add game id to user in firebase
        updateGame(game);
    }
    @Override
    public void updateGame(Game game) {
        DatabaseReference games = db.getReference("games");
    }
    public void addGame(String user_id, String game_id) {
        DatabaseReference user = db.getReference("users/" + user_id);
        DatabaseReference game = db.getReference("games/" + game_id);
    }
}
