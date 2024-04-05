package com.mygdx.group17.shipocalypse;

import com.badlogic.gdx.ApplicationAdapter;

import com.mygdx.group17.shipocalypse.singletons.GameManager;
import com.mygdx.group17.shipocalypse.models.State;

public class Shipocalypse extends ApplicationAdapter {

	GameManager gameManager;

	@Override
	public void create () {

		GameManager.init(this);
		GameManager.setState(State.menu);

	}

	@Override
	public void render () {

		GameManager.handleInput();
		GameManager.render();

	}
	
	@Override
	public void dispose () {

	}
}
