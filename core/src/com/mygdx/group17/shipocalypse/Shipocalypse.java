package com.mygdx.group17.shipocalypse;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.group17.shipocalypse.views.AccountState;

public class Shipocalypse extends ApplicationAdapter {

	public static int GAME_WIDTH = 820;
	public static int GAME_HEIGHT = 860;
	SpriteBatch batch;
	Texture ship;
	Texture sea;

	AccountState playstate;

	BitmapFont bf;

	ShapeRenderer shape;


	@Override
	public void create () {
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		ship = new Texture("ship4.png");
		sea = new Texture("sea.png");

		playstate = new AccountState(shape);

		bf = new BitmapFont();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		playstate.render(batch);
		batch.begin();
		batch.draw(sea, 0, 0);



		bf.draw(batch, "Mouse location: {" + (Gdx.input.getX()) + ", " + (Shipocalypse.GAME_HEIGHT - Gdx.input.getY()) + "}", 15,20);
		batch.end();

		playstate.handleInput();

		playstate.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sea.dispose();
	}
}
