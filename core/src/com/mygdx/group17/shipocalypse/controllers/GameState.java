package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

public abstract class GameState {

    public abstract void render();

    public abstract void dispose();

    public abstract void handleInput();

}
