package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameState {

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();

    public abstract void handleInput();
}
