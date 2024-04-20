package com.mygdx.group17.shipocalypse;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.group17.shipocalypse.models.Options;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Shipocalypse");
		config.setWindowedMode(Options.GAME_WIDTH, Options.GAME_HEIGHT);
		//new Lwjgl3Application(new Shipocalypse(), config);
	}
}
