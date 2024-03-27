package com.mygdx.group17.shipocalypse.views;

import static com.mygdx.group17.shipocalypse.Shipocalypse.GAME_HEIGHT;
import static com.mygdx.group17.shipocalypse.Shipocalypse.GAME_WIDTH;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.ui.MenuButton;


public class AcountState extends GameState
{

    TextButton button;
    Stage stage;
    public AcountState(ShapeRenderer shaperenderer) {

        Skin skin = new Skin(Gdx.files.internal("ui-skin.json"));
        button = new TextButton("Click me!", skin);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle button click here
                System.out.println("Button clicked!");
            }
        });


        Stage stage = new Stage();
        stage.addActor(button);
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor


    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Gdx.graphics.getDeltaTime()); // Update the stage
        stage.draw(); // Draw the stage


    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleInput() {
    }

}


















