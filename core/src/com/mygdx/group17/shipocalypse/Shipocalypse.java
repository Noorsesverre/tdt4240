package com.mygdx.group17.shipocalypse;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.group17.shipocalypse.ui.LoginScreen;
import com.mygdx.group17.shipocalypse.views.ConfigureState;
import com.mygdx.group17.shipocalypse.views.MenuState;
import com.mygdx.group17.shipocalypse.views.PlayState;

public class Shipocalypse extends ApplicationAdapter {

	public static final int GAME_WIDTH = 820;
	public static final int GAME_HEIGHT = 860;
	SpriteBatch batch;
	Texture ship;
	Texture sea;

	ConfigureState playstate;

	BitmapFont bf;

	ShapeRenderer shape;

	LoginScreen loginScreen; // Your custom login screen class
	private FirebaseInterface firebaseInterface;


	public Shipocalypse(FirebaseInterface firebaseInterface) {
		this.firebaseInterface = firebaseInterface;
	}

	@Override
	public void create () {
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		ship = new Texture("ship4.png");
		sea = new Texture("sea.png");

		playstate = new ConfigureState();

		bf = new BitmapFont();

		loginScreen = new LoginScreen(firebaseInterface); // Initialize your login screen here
	}

	@Override
	public void render () {
		/*ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(sea, 0, 0);


		bf.draw(batch, "Mouse location: {" + (Gdx.input.getX()) + ", " + (Shipocalypse.GAME_HEIGHT - Gdx.input.getY()) + "}", 15,20);
		batch.end();

		playstate.handleInput();

		playstate.render(batch);*/ //commented out to check the login screen

		loginScreen.render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sea.dispose();
	}
}
