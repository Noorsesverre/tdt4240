package com.mygdx.group17.shipocalypse.views;

import com.mygdx.group17.shipocalypse.FirebaseInterface;
import com.mygdx.group17.shipocalypse.models.User;

public class LoginState {

    private FirebaseInterface firebaseInterface;
    private User user; // Model instance

    public LoginState(FirebaseInterface firebaseInterface) {
        this.firebaseInterface = firebaseInterface;
        this.user = new User();
    }

    public void attemptLogin(String email, String password) {
        firebaseInterface.signIn(email, password, new FirebaseInterface.Callback() {
            @Override
            public void onSuccess() {
                // Login success, update the user model and change to the game state
            }

            @Override
            public void onFailure(String message) {
                // Login failed, display the message to the user
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    // Methods for updating the view based on login success or failure
}