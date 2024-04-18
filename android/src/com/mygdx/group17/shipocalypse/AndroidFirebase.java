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

import java.util.HashMap;
import java.util.Map;

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
    public String createGame(int gridX, int gridY, Map<Integer, Integer> boats) {
        String game_code = "test code";
        DatabaseReference games = db.getReference("games_list");
        DatabaseReference game_ref = games.push();
        for (int key : boats.keySet()) {
            DatabaseReference boat_ref = game_ref.child(String.valueOf(key));
            boat_ref.setValue(String.valueOf(boats.get(key)));
        }
        System.out.println(game_ref.toString());
        return game_ref.toString();
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
        games.child(game.id).setValue(game);
    }
    public void addGame(String user_id, String game_id) {
        DatabaseReference user = db.getReference("users/" + user_id);
        DatabaseReference game = db.getReference("games/" + game_id);


    }
}
