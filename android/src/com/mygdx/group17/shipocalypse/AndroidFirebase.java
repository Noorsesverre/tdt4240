package com.mygdx.group17.shipocalypse;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mygdx.group17.shipocalypse.controllers.GameState;
import com.mygdx.group17.shipocalypse.controllers.PlayState;

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
}
