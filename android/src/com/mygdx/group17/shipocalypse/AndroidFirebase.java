package com.mygdx.group17.shipocalypse;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AndroidFirebase implements FirebaseInterface{
    private FirebaseDatabase database;

    public AndroidFirebase() {
        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void writeHostCode(String hostId, String boardSize, String missilNr) {
        DatabaseReference gameRef = database.getReference("hostnr");

        Map<String, Object> gameValues = new HashMap<>();
        gameValues.put("boardSize", boardSize);
        gameValues.put("missilNumber", missilNr);

       gameRef.child(hostId).setValue(gameValues);

    }
}
