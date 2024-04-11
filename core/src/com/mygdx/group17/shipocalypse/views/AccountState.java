package com.mygdx.group17.shipocalypse.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.ui.AccountButton;
import com.mygdx.group17.shipocalypse.ui.MenuButton;

public class AccountState extends GameState {

    private AccountButton host_button;
    private AccountButton join_button;
    private AccountButton exit_button;
    private Stage stage;
    private TextField emailField;
    private TextField passwordField;
    private Skin skin;

    int GAME_WIDTH = Gdx.graphics.getWidth();
    int GAME_HEIGHT = Gdx.graphics.getHeight();

    public AccountState(ShapeRenderer shaperenderer) {
        float buttonGameCenter = GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;

        this.host_button = new AccountButton(shaperenderer, (int) buttonGameCenter, 500, "join game", Action.joinGame);
        this.join_button = new AccountButton(shaperenderer, (int) buttonGameCenter, 400, "host game", Action.hostGame);
        this.exit_button = new AccountButton(shaperenderer, (int) buttonGameCenter, 200, "exit", Action.exit);

        // Create a skin for text fields
        skin = new Skin();
        skin.add("default", new TextField.TextFieldStyle());
        TextFieldStyle style = skin.get("default", TextField.TextFieldStyle.class);
        Drawable cursor = skin.newDrawable(skin.getDrawable("cursor"), 1, 1, 1, 0.8f);
        style.cursor = cursor;
        NinePatchDrawable cursorPatch = new NinePatchDrawable(skin.getPatch("cursor"));
        style.cursor = cursorPatch;

        // Create a stage for UI elements
        stage = new Stage(new StretchViewport(GAME_WIDTH, GAME_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        // Create email and password fields
        emailField = new TextField("", style);
        emailField.setMessageText("Email");
        emailField.setPosition(GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 + 50);
        emailField.setSize(200, 30);

        passwordField = new TextField("", style);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setPosition(GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 - 50);
        passwordField.setSize(200, 30);

        // Add fields to the stage
        stage.addActor(emailField);
        stage.addActor(passwordField);
    }

    @Override
    public void render(SpriteBatch batch) {
        host_button.render(batch);
        join_button.render(batch);
        exit_button.render(batch);
        stage.draw();
    }

    @Override
    public void dispose() {
        host_button.dispose();
        join_button.dispose();
        exit_button.dispose();
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void handleInput() {
        host_button.handleInput();
        join_button.handleInput();
        exit_button.handleInput();
    }
}

