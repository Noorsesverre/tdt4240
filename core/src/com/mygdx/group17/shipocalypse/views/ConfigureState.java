package com.mygdx.group17.shipocalypse.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.*;
import com.mygdx.group17.shipocalypse.ui.BoatButton;

public class ConfigureState extends GameState{

    private Player _player;
    private Boat[] _boats;

    public ConfigureState() {
        _player = new Player(10, 10);
        _boats = new Boat[] {
                new Boat(0, 0, 1),
                new Boat(0, 0, 2),
                new Boat(0, 0, 3),
                new Boat(0, 0, 4)
        };
    }

    @Override
    public void render(SpriteBatch batch) {
        ShapeRenderer shaperenderer = new ShapeRenderer();
        for (Tile[] list : _player.get_grid().get_tiles()) {
            for (Tile tile : list) {
                shaperenderer.begin(ShapeRenderer.ShapeType.Line);
                shaperenderer.setColor(Color.BLACK);
                shaperenderer.rect(tile._posx, tile._posy, Tile.TILE_SIZE, Tile.TILE_SIZE);
                shaperenderer.end();
            }
        }

        batch.begin();

        BoatButton boatbtn1 = new BoatButton(shaperenderer, 50, Shipocalypse.GAME_HEIGHT - 200, new Texture("ship1.png"));
        boatbtn1.render(batch);

        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleInput() {
        for (Boat boat : _boats) {
            boat.handleInput();
        }
    }
}
