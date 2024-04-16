package com.mygdx.group17.shipocalypse;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.firebase.FirebaseApp;
import com.mygdx.group17.shipocalypse.ui.LoginScreen;

public class AndroidLauncher extends AndroidApplication {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// Create an instance of the AndroidFirebase (implements FirebaseInterface)
		AndroidFirebase firebaseAuth = new AndroidFirebase();

		// Pass the interface implementation to the Shipocalypse constructor
		initialize(new Shipocalypse(firebaseAuth), config);
	}

}
