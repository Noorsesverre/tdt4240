package com.mygdx.group17.shipocalypse;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.firebase.FirebaseApp;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		FirebaseApp.initializeApp(this);

		FirebaseInterface firebaseInterface = new AndroidFirebase();
		initialize(new Shipocalypse(firebaseInterface), config);
	}
}
