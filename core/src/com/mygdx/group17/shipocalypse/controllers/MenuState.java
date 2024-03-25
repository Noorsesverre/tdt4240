package com.mygdx.group17.shipocalypse.controllers;

import static com.mygdx.group17.shipocalypse.Shipocalypse.GAME_HEIGHT;
import static com.mygdx.group17.shipocalypse.Shipocalypse.GAME_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.ui.MenuButton;

public class MenuState extends GameState {

    private Texture title;

    private MenuButton host_button;
    private MenuButton join_button;
    private MenuButton exit_button;

    public MenuState(ShapeRenderer shaperenderer) {
        this.title = new Texture("title.png");

        float buttonGameCenter = GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;

        this.host_button = new MenuButton(shaperenderer, (int)buttonGameCenter, 500, "join game", Action.joinGame);
        this.join_button = new MenuButton(shaperenderer, (int)buttonGameCenter, 400, "host game", Action.hostGame);
        this.exit_button = new MenuButton(shaperenderer, (int)buttonGameCenter, 300, "exit", Action.exit);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(title, 300, GAME_HEIGHT - 200);
        batch.end();

        host_button.render(batch);
        join_button.render(batch);
        exit_button.render(batch);
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

