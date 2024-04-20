package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.models.State;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.singletons.GameManager;
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
        if (GameManager.result < 0) {
            AssetManager.write("YOU LOSE!", Options.GAME_WIDTH/2 - 30, 600);

        }
        else if (GameManager.result > 0) {
            AssetManager.write("YOU WIN!", Options.GAME_WIDTH/2 - 30,  600);
        }
        main_menu_button.render();
    }
    @Override
    public void dispose() {
        main_menu_button.dispose();
    }
    @Override
    public void handleInput() {

        if (main_menu_button.handleInput()) {
            GameManager.setState(State.menu);
        }
    }
}