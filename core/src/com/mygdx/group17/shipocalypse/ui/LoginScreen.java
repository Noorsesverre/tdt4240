package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.group17.shipocalypse.FirebaseInterface;

public class LoginScreen implements Screen {
    private Stage stage;
    private TextField usernameTextField;
    private TextField passwordTextField;
    private BitmapFont font;
    private Skin skin;
    private TextButton submitButton;


    public LoginScreen(FirebaseInterface firebaseInterface) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();
        skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap)); // Add the white texture to the skin

        // Dispose of the Pixmap
        pixmap.dispose();

        // Create a basic TextFieldStyle
        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = font;
        textStyle.fontColor = Color.WHITE;
        textStyle.background = skin.newDrawable("white", Color.DARK_GRAY); // Just a solid color
        textStyle.cursor = skin.newDrawable("white", Color.WHITE); // Color of the cursor
        textStyle.selection = skin.newDrawable("white", new Color(0.5f, 0.5f, 0.5f, 0.5f)); // Color of the text selection

        // Initialize username text field
        usernameTextField = new TextField("", textStyle);
        usernameTextField.setMessageText("Username");
        usernameTextField.setPosition(Gdx.graphics.getWidth() * 0.5f - 100, Gdx.graphics.getHeight() * 0.6f);
        usernameTextField.setSize(200, 30);
        stage.addActor(usernameTextField);

        // Initialize password text field
        passwordTextField = new TextField("", textStyle);
        passwordTextField.setMessageText("Password");
        passwordTextField.setPosition(Gdx.graphics.getWidth() * 0.5f - 100, Gdx.graphics.getHeight() * 0.5f);
        passwordTextField.setSize(200, 30);
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);
        stage.addActor(passwordTextField);

        // Note: You'd need to load a proper UI skin or create a new skin for better visuals

        // Button Style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        buttonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        buttonStyle.checked = skin.newDrawable("white", Color.DARK_GRAY);
        buttonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY); // Change color on hover

        // Button
        submitButton = new TextButton("Submit", buttonStyle);
        submitButton.setPosition(Gdx.graphics.getWidth() * 0.5f - 50, Gdx.graphics.getHeight() * 0.4f);
        submitButton.setSize(100, 30);
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameTextField.getText();
                // Call Firebase interface to add the username
                firebaseInterface.addUsername(username, new FirebaseInterface.Callback() {
                    @Override
                    public void onSuccess() {
                        Gdx.app.log("LoginScreen", "Username added successfully");
                    }

                    @Override
                    public void onFailure(String message) {
                        Gdx.app.log("LoginScreen", "Failed to add username: " + message);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                });
            }
        });

        stage.addActor(submitButton);
    }

    @Override
    public void show() {
        // Called when this screen becomes the current screen
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    // Implement other methods as needed (resize, pause, resume, hide, dispose)

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        skin.dispose();
    }
}
