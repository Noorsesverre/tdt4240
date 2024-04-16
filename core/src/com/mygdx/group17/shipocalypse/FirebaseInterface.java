package com.mygdx.group17.shipocalypse;

public interface FirebaseInterface {
    void addUsername(String username, Callback callback);

    void signIn(String email, String password, Callback callback);
    void signUp(String email, String password, Callback callback);
    void signOut();
    boolean isSignedIn();
    // ... any other auth related methods you need

    interface Callback {
        void onSuccess();
        void onFailure(String message);

        void onFailure(Throwable throwable);
    }
}