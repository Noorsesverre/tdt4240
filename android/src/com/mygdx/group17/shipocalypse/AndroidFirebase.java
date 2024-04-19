package com.mygdx.group17.shipocalypse;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mygdx.group17.shipocalypse.controllers.GameState;
import com.mygdx.group17.shipocalypse.controllers.PlayState;
import com.mygdx.group17.shipocalypse.models.Game;
import com.mygdx.group17.shipocalypse.singletons.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AndroidFirebase implements FirebaseInterface{

    private FirebaseDatabase db;
    private String reference_url = "https://shipocalypse-tdt4240-default-rtdb.europe-west1.firebasedatabase.app/";

    public AndroidFirebase() {
        db = FirebaseDatabase.getInstance(reference_url);
    }
    @Override
    public void writeToDatabase(String gameID, Map<String, Object> data) {
        //DatabaseReference games = db.getReference("games");
        // Get a reference to the document identified by gameID in a collection named "games"
        // Set the data in Firestore document
        //games.setValue("kai test");
    }
    @Override
    public String createGame(HashMap<String, Object> options) {
        DatabaseReference games = db.getReference("active_games");
        String game_id = UUID.randomUUID().toString().substring(0,6); // Generate a random pseudo-unique ID
        games.child(game_id).child("users").child(GameManager.getUserId()).setValue("host");
        games.child(game_id).child("options").setValue(options.toString());
        return game_id;
    }

    @Override
    public void playerReady(String user_id, String game_id) {
        DatabaseReference user = db.getReference("active_games/" + game_id + "/users/" + user_id);
        user.child("ready").setValue(1);
    }

    @Override
    public boolean bothPlayersReady(String game_id) {
        DatabaseReference users = db.getReference("active_games/" + game_id + "/users");
        Task<DataSnapshot> read = users.get();
        while (!read.isComplete()) {
            System.out.println("reading from db...");
        }
        DataSnapshot snapshot = read.getResult();
        if (snapshot.getChildrenCount() < 2) {
            return false;
        }
        for (DataSnapshot user : snapshot.getChildren()) {
            if ((int)user.child("ready").getValue() != 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public HashMap<String, HashMap<String, Object>> getOpenGames() {
        DatabaseReference active_games = db.getReference("active_games");
        HashMap<String, HashMap<String, Object>> open_game_id = new HashMap<String, HashMap<String, Object>>();
        System.out.println(active_games.get());
        Task<DataSnapshot> read = active_games.get();
        while (!read.isComplete()) {
            System.out.println("reading from db...");
        }
        DataSnapshot snapshot = read.getResult();
        for (DataSnapshot game : snapshot.getChildren()) {
            if (game.child("users").getChildrenCount() < 2) {
                open_game_id.put(game.getKey(), (HashMap) game.child("grid_size").getValue());
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


    public void postMoveInfo(String game_id, String sender_id, String receiver_id, String move_info) {
        DatabaseReference move = db.getReference("active_games/" + game_id + "/lastmove");
        DatabaseReference sender = db.getReference("active_games/" + game_id + "/" + sender_id);
        DatabaseReference receiver = db.getReference("active_games/" + game_id + "/" + receiver_id);
        move.setValue(move_info);
        sender.setValue(0);
        receiver.setValue(1);
    }
    @Override
    public void postGridInfo(String game_id, String sender_id, String grid_info) {
        DatabaseReference grid = db.getReference("active_games/" + game_id + "/grids/" + sender_id);
        grid.setValue(grid_info);
    }

    public String getGridInfo(String game_id) {
        DatabaseReference grids = db.getReference("active_games/" + game_id + "/grids/");
        final String[] grid_info = new String[1];

        Task<DataSnapshot> read = grids.get();
        while (!read.isComplete()) {
            System.out.println("reading from db...");
        }
        DataSnapshot snapshot = read.getResult();
        for (DataSnapshot child : snapshot.getChildren()) {
            if (child.getKey() != GameManager.getUserId()) {
                grid_info[0] = child.getValue(String.class);
            }
        }
        return grid_info[0];
    }

    public String getMoveInfo(String game_id) {
        DatabaseReference move = db.getReference("active_games/" + game_id + "/lastmove");
        final String[] move_info = new String[1];
        move.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    move_info[0] = snapshot.getValue(String.class);
                }
            }
        });
        return move_info[0];
    }
    public boolean joinGame(String game_id) {
        DatabaseReference game = db.getReference("joinable_games/" + game_id);
        final boolean[] joined = {false};
        game.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                         joined[0] = true;
                    }
                }
            }
        });
        return joined[0];

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
