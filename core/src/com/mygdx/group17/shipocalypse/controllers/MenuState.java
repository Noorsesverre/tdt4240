package com.mygdx.group17.shipocalypse.controllers;

import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.Options;

import com.mygdx.group17.shipocalypse.singletons.AssetManager;

import com.mygdx.group17.shipocalypse.ui.MenuButton;

public class MenuState extends GameState {
    private final MenuButton host_button;
    private final MenuButton join_button;
    private final MenuButton exit_button;

    public MenuState() {
        float buttonGameCenter = Options.GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;
        this.host_button = new MenuButton(AssetManager.shape, (int)buttonGameCenter, 500, "host game", Action.hostGame);
        this.join_button = new MenuButton(AssetManager.shape, (int)buttonGameCenter, 400, "join game", Action.joinGame);
        this.exit_button = new MenuButton(AssetManager.shape, (int)buttonGameCenter, 300, "exit", Action.exit);
    }

    @Override
    public void render() {
        AssetManager.draw(AssetManager.title, Options.GAME_WIDTH / 2 - AssetManager.title.getWidth() / 2, 200);
        host_button.render(AssetManager.batch);
        join_button.render(AssetManager.batch);
        exit_button.render(AssetManager.batch);
    }
    @Override
    public void dispose() {
        host_button.dispose();
        join_button.dispose();
        exit_button.dispose();
    }
    @Override
    public void handleInput() {
        host_button.handleInput();
        join_button.handleInput();
        exit_button.handleInput();
    }
}