package com.mygdx.group17.shipocalypse.controllers;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.ui.MenuButton;
import com.mygdx.group17.shipocalypse.ui.Selector;

public class JoinState extends GameState {
    private Texture title;
    private Selector grid_selector;
    private Selector time_selector;
    private MenuButton start_button;
    private ShapeRenderer shapeRenderer;

    public JoinState() {
        this.title = new Texture("title.png");
        this.shapeRenderer = AssetManager.getInstance().shape;

        float buttonGameCenter = Options.GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;

        this.grid_selector = new Selector();
        this.grid_selector.setItems(Options.options.get("grid"));
        this.grid_selector.setX((int)buttonGameCenter);
        this.grid_selector.setY(400);

        this.start_button = new MenuButton(shapeRenderer, (int)buttonGameCenter, 300, "start", Action.exit);
    }

    @Override
    public void render() {
        AssetManager.batch.begin();
        AssetManager.batch.draw(title, 300, Options.GAME_HEIGHT - 200);
        AssetManager.batch.end();

        start_button.render(AssetManager.batch);
    }

    @Override
    public void dispose() {
        start_button.dispose();
    }

    @Override
    public void handleInput() {
        start_button.handleInput();
    }

}

