package com.mygdx.group17.shipocalypse;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.singletons.*;
import com.mygdx.group17.shipocalypse.models.State;

public class Shipocalypse extends ApplicationAdapter {
	OrthographicCamera camera;
	private FitViewport viewport;
	private Stage stage;
	private FirebaseInterface firebaseInterface;


	public Shipocalypse(FirebaseInterface firebaseInterface) {
		this.firebaseInterface = firebaseInterface;
	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.position.set(Options.GAME_WIDTH / 2f, Options.GAME_HEIGHT / 2f, 0);
		camera.update();
		viewport = new FitViewport(Options.GAME_WIDTH, Options.GAME_HEIGHT, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		GameManager.init(this);
		GameManager.setFirebase(firebaseInterface);
		GameManager.setState(State.menu);
		AssetManager.setViewport(viewport);
	}

	@Override
	public void render () {
		GameManager.handleInput();
		viewport.apply();
		GameManager.render();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	@Override
	public void dispose () {

	}
}
