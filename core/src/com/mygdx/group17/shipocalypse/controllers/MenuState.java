package com.mygdx.group17.shipocalypse.controllers;

import com.mygdx.group17.shipocalypse.models.Options;

import com.mygdx.group17.shipocalypse.models.State;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;

import com.mygdx.group17.shipocalypse.singletons.GameManager;
import com.mygdx.group17.shipocalypse.ui.MenuButton;

public class MenuState extends GameState {
    private final MenuButton host_button;
    private final MenuButton join_button;
    private final MenuButton exit_button;

    public MenuState() {
        float buttonGameCenter = Options.GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;
        this.host_button = new MenuButton(AssetManager.shape, (int)buttonGameCenter, 500, "host game");
        this.join_button = new MenuButton(AssetManager.shape, (int)buttonGameCenter, 400, "join game");
        this.exit_button = new MenuButton(AssetManager.shape, (int)buttonGameCenter, 300, "exit");
    }

    @Override
    public void render() {
        AssetManager.draw(AssetManager.title, Options.GAME_WIDTH / 2 - AssetManager.title.getWidth() / 2, 200);
        host_button.render();
        join_button.render();
        exit_button.render();
    }
    @Override
    public void dispose() {
        host_button.dispose();
        join_button.dispose();
        exit_button.dispose();
    }
    @Override
    public void handleInput() {
        if (host_button.handleInput()) {
            GameManager.setState(State.host);
        }
        if (join_button.handleInput()) {
            GameManager.setState(State.join);
        }
        if (exit_button.handleInput()) {
            System.exit(0);
        }
    }
}