package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.Boat;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.models.State;
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
        debug();
        // Change the y coordinate of player/opponent tiles to display both grids.
        for (Boat boat : GameManager.getOpponent().getBoatConfig().boats) {
            boat.shiftUp();
        }
        for (Boat boat : GameManager.getPlayer().getBoatConfig().boats) {
            boat.shiftDown();
        }
        for (Tile[] list : GameManager.getPlayer().get_grid().get_tiles_aslist()) {
            for (Tile tile : list) {
                tile.shiftDown();
            }
        }
        for (Tile[] list : GameManager.getOpponent().get_grid().get_tiles_aslist()) {
            for (Tile tile : list) {
                tile.shiftUp();
            }
        }
    }

    private void debug() {

        System.out.println("Opponent boats:");
        for (Boat boat : GameManager.getOpponent().getBoatConfig().boats) {
            System.out.println("- Boat with tiles: ");
            for (Tile tile : boat.getTiles()) {
                System.out.println("  - Tile at X: " + tile._index_x + ", Y: " + tile._index_y);
            }
            System.out.println(" - Boat hit flags:");
            for (boolean hit : boat.getHits_aslist()) {
                System.out.println("  - " + hit);
            }
        }

    }

    @Override
    public void render() {

        for (Tile[] list : GameManager.getOpponent().get_grid().get_tiles_aslist()) {
            for (Tile tile : list) {
                AssetManager.shape.begin(ShapeRenderer.ShapeType.Line);
                AssetManager.shape.setColor(Color.NAVY);
                AssetManager.shape.rect(tile._posx, tile._posy , Tile.TILE_SIZE, Tile.TILE_SIZE);
                AssetManager.shape.end();
            }
        }

        for (Tile[] list : GameManager.getPlayer().get_grid().get_tiles_aslist()) {
            for (Tile tile : list) {
                AssetManager.shape.begin(ShapeRenderer.ShapeType.Line);
                AssetManager.shape.setColor(Color.NAVY);
                AssetManager.shape.rect(tile._posx, tile._posy , Tile.TILE_SIZE, Tile.TILE_SIZE);
                AssetManager.shape.end();
            }
        }

        for (Boat boat : GameManager.getOpponent().getBoatConfig().boats) {
            if (boat.isSunk()) {
                boat.render(AssetManager.batch);
            }
        }

        for (Boat boat : GameManager.getPlayer().getBoatConfig().boats) {
            boat.render(AssetManager.batch);
        }

        for (Tile[] list : GameManager.getOpponent().get_grid().get_tiles_aslist()) {
            for (Tile tile : list) {
                if (tile.burning) {
                    AssetManager.draw(AssetManager.fire_sprite, tile._posx, tile._posy + 7);
                }
                else if (tile.isHit()) {
                    AssetManager.draw(AssetManager.cross_sprite, tile._posx, tile._posy);
                }
                else if (tile.isSelected()) {
                    AssetManager.draw(AssetManager.crosshair_sprite, tile._posx, tile._posy);
                }
            }
        }

        for (Tile[] list : GameManager.getPlayer().get_grid().get_tiles_aslist()) {
            for (Tile tile : list) {
                if (tile.burning) {
                    AssetManager.draw(AssetManager.fire_sprite, tile._posx, tile._posy + 7);
                }
                else if (tile.isHit()) {
                    AssetManager.draw(AssetManager.cross_sprite, tile._posx, tile._posy);
                }
            }
        }

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

            for (Tile[] list : GameManager.getOpponent().get_grid().get_tiles_aslist()) {
                for (Tile tile : list) {
                    if (touch_rectangle.overlaps(tile.get_rectangle()) && !tile.isHit()) {
                        selected_tile = tile;
                        selection = true;
                        break;
                    }
                }
            }

            for (Tile[] list : GameManager.getOpponent().get_grid().get_tiles_aslist()) {
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
            for (Boat boat: GameManager.getOpponent().getBoatConfig().boats) {
                for (Tile tile : boat.getTiles()) {
                    if (selected_tile == tile) {
                        boat.hit(tile);
                        hit = true;
                        if (boat.isSunk()) {
                            boat.show();
                        }
                        GameManager.getOpponent().checkDefeat();
                        debug();
                    }
                }
            }
            if (hit) {
                System.out.println("You hit a ship!");
            }
            else {
                System.out.println("You hit nothing");
            }
            selected_tile.hit();

        }
        }
        if (GameManager.getOpponent().allShipsSunk()) {
            GameManager.setState(State.gameEnd);
        }
    }


}

