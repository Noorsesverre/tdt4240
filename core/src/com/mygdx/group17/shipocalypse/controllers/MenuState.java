package com.mygdx.group17.shipocalypse.controllers;

import static com.mygdx.group17.shipocalypse.Shipocalypse.GAME_HEIGHT;
import static com.mygdx.group17.shipocalypse.Shipocalypse.GAME_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.ui.MenuButton;

import com.mygdx.group17.shipocalypse.singletons.AssetManager;

public class MenuState extends GameState {
    private Texture title;
    private MenuButton host_button;
    private MenuButton join_button;
    private MenuButton exit_button;
    private ShapeRenderer shapeRenderer;

    public MenuState() {

        this.title = new Texture("title.png");
        this.shapeRenderer = AssetManager.getInstance().shape;
        float buttonGameCenter = GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;
        this.host_button = new MenuButton(shapeRenderer, (int)buttonGameCenter, 500, "host game", Action.hostGame);
        this.join_button = new MenuButton(shapeRenderer, (int)buttonGameCenter, 400, "join game", Action.joinGame);
        this.exit_button = new MenuButton(shapeRenderer, (int)buttonGameCenter, 300, "exit", Action.exit);
    }

    @Override
    public void render() {
        AssetManager.batch.begin();
        AssetManager.batch.draw(title, 300, GAME_HEIGHT - 200);
        AssetManager.batch.end();
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