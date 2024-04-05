package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.Boat;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.models.Tile;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.singletons.GameManager;
import com.mygdx.group17.shipocalypse.ui.MenuButton;

public class PlayState extends GameState {

    public static boolean selection = false;

    Tile selected_tile;
    public MenuButton fire_button;

    public PlayState() {
        float buttonGameCenter = Options.GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;
        this.fire_button = new MenuButton(AssetManager.shape, (int)buttonGameCenter, 100, "fire", Action.test);
        this.selected_tile = new Tile(0, 0, 0, 0);
    }

    @Override
    public void render() {
        for (Tile[] list : GameManager.getPlayer().get_grid().get_tiles()) {
            for (Tile tile : list) {
                AssetManager.shape.begin(ShapeRenderer.ShapeType.Line);
                if (tile.isSelected()) {
                    AssetManager.shape.setColor(Color.RED);
                }
                else {
                    AssetManager.shape.setColor(Color.DARK_GRAY);
                }
                AssetManager.shape.rect(tile._posx, tile._posy, Tile.TILE_SIZE, Tile.TILE_SIZE);
                AssetManager.shape.end();
            }
        }

        AssetManager.batch.begin();


        for (Boat boat : GameManager.getPlayer().getBoatConfig().boats) {
            boat.render(AssetManager.batch);
        }

        AssetManager.batch.end();

        if (selection) {
            fire_button.render(AssetManager.batch);
        }

    }


    @Override
    public void dispose() {
    }
    @Override
    public void handleInput() {
        if (Gdx.input.isTouched() ) {
            int input_x = Gdx.input.getX();
            int input_y = Options.GAME_HEIGHT - Gdx.input.getY();
            Rectangle touch_rectangle = new Rectangle(input_x - 2, input_y - 2, 4, 4);

            for (Tile[] list : GameManager.getPlayer().get_grid().get_tiles()) {
                for (Tile tile : list) {
                    if (touch_rectangle.overlaps(tile.get_rectangle())) {
                        selected_tile = tile;
                        selection = true;
                        break;
                    }
                }
            }

            for (Tile[] list : GameManager.getPlayer().get_grid().get_tiles()) {
                for (Tile tile : list) {
                    if (tile == selected_tile) {
                        tile.select();
                    }
                    else {
                        tile.deselect();
                    }
                }
            }

        if (fire_button.handleInput()) {
            System.out.println("Fired at tile " + String.valueOf(selected_tile.getIndex()[0]) + " : " + String.valueOf(selected_tile.getIndex()[1]));
            boolean hit = false;
            for (Boat boat: GameManager.getPlayer().getBoatConfig().boats) {
                for (Tile tile : boat.getTiles()) {
                    if (selected_tile == tile) {
                        hit = true;
                    }
                }
            }
            if (hit) {
                System.out.println("You hit a ship!");
            }
            else {
                System.out.println("You hit nothing");
            }

        }
        }
    }


}

