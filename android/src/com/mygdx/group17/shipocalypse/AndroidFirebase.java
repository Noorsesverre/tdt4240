// File: android/src/com/mygdx/group17/shipocalypse/android/AndroidFirebase.java

package com.mygdx.group17.shipocalypse;

import com.badlogic.gdx.Gdx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AndroidFirebase implements FirebaseInterface {
    private FirebaseAuth auth;

    public AndroidFirebase() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void addUsername(String username, Callback callback) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Make sure to have Firestore dependency
        db.collection("users").add(user)
                .addOnSuccessListener(documentReference -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure("Fail"));
    }

    @Override
    public void signIn(String email, String password, Callback callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onSuccess();
            } else {
                callback.onFailure(task.getException().getMessage());
            }
        });
    }

    @Override
    public void signUp(String email, String password, Callback callback) {

    }

    @Override
    public void signOut() {

    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    // Implement other FirebaseInterface methods
}