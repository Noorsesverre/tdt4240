package com.mygdx.group17.shipocalypse.controllers;

import static com.mygdx.group17.shipocalypse.Shipocalypse.GAME_HEIGHT;
import static com.mygdx.group17.shipocalypse.Shipocalypse.GAME_WIDTH;

import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class PlayState extends GameState {

    private Texture title;


    public PlayState() {
        this.title = new Texture("title.png");

    }

    @Override
    public void render() {
        AssetManager.batch.begin();
        AssetManager.batch.draw(title, 300, GAME_HEIGHT - 200);
        AssetManager.batch.end();

    }

    @Override
    public void dispose() {
    }

    @Override
    public void handleInput() {
    }

}

