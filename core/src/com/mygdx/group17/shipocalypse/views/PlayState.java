package com.mygdx.group17.shipocalypse.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.*;
public class PlayState extends GameState{

    private Player _player;
    private Boat[] _boats;

    public PlayState() {
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

        for (Boat boat : _boats) {
            boat.render(batch);
        }

        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched()) {
            int input_x = Gdx.input.getX();
            int input_y = Shipocalypse.GAME_HEIGHT - Gdx.input.getY();

            Rectangle touch_rectangle = new Rectangle(input_x - 1, input_y - 1, 2, 2);

            for (Tile[] list : _player.get_grid().get_tiles()) {
                for (Tile tile : list) {
                    if (touch_rectangle.overlaps(tile.get_rectangle())) {

                    }
                }
            }
        }
    }
}
