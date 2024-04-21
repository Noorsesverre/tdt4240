package com.mygdx.group17.shipocalypse.controllers;

import com.mygdx.group17.shipocalypse.models.State;

public abstract class GameState {

    public State state = State.undefined;
    public abstract void render();

    public abstract void dispose();

    public abstract void handleInput();

}
