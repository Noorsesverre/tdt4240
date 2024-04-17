package com.mygdx.group17.shipocalypse;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class AndroidFirebase implements FirebaseInterface{

    private FirebaseFirestore db;

    public AndroidFirebase() {
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public void writeToDatabase(String gameID, Map<String, Object> data) {
        // Get a reference to the document identified by gameID in a collection named "games"
        DocumentReference docRef = db.collection("games").document(gameID);
        // Set the data in Firestore document
        docRef.set(data);
    }
}
