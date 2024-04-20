package com.mygdx.group17.shipocalypse.controllers;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.ui.MenuButton;
import com.mygdx.group17.shipocalypse.singletons.GameManager;

import java.util.ArrayList;
import java.util.HashMap;

public class JoinState extends GameState {
    HashMap<String, HashMap<String, Object>> open_game_id;
    ArrayList<MenuButton> menu_buttons;
    private Texture title;
    private ShapeRenderer shapeRenderer;

    public JoinState() {
        this.title = new Texture("title.png");
        this.shapeRenderer = AssetManager.getInstance().shape;


        open_game_id = GameManager.getOpenGames();

        System.out.println(open_game_id);
        menu_buttons = new ArrayList<MenuButton>();

        float buttonGameCenter = Options.GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;

        int y = 550;

        for (String game_id : open_game_id.keySet()) {
            menu_buttons.add(new MenuButton(shapeRenderer, (int)buttonGameCenter, y, game_id, Action.selectGame));
            y = y - 100;
        }

    }

    @Override
    public void render() {
        AssetManager.draw(AssetManager.title, Options.GAME_WIDTH / 2 - AssetManager.title.getWidth() / 2, 200);

        for (MenuButton button : menu_buttons) {
            button.render();
        }
    }

    @Override
    public void dispose() {
        for (MenuButton button : menu_buttons) {
            button.dispose();
        }
    }

    @Override
    public void handleInput() {
        for (MenuButton button : menu_buttons) {
            if (button.handleInput()) {
                GameManager.joinGame(button.get_text(), open_game_id.get(button.get_text()));
            }
        }
    }

}

