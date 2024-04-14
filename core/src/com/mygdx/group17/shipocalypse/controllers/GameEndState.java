package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.ui.MenuButton;

public class GameEndState extends GameState {
    Texture result;
    private final MenuButton main_menu_button;

    public GameEndState(Texture _result) {
        float buttonGameCenter = Options.GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;
        result = _result;
        this.main_menu_button = new MenuButton(AssetManager.shape, (int)buttonGameCenter, 500, "main menu", Action.mainMenu);
    }

    @Override
    public void render() {
        AssetManager.draw(result, Options.GAME_WIDTH / 2 - AssetManager.title.getWidth() / 2, 200);
        main_menu_button.render(AssetManager.batch);
    }
    @Override
    public void dispose() {
        main_menu_button.dispose();
    }
    @Override
    public void handleInput() {
        main_menu_button.handleInput();
    }
}