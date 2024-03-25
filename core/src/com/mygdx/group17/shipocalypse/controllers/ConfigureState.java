package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.*;
import com.mygdx.group17.shipocalypse.ui.BoatButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigureState extends GameState {

    // These will vary between game states and players
    private Player _player;
    public BoatConfiguration boatConfiguration;
    public Gameconfig gameconfig;

    // These properties are only needed for this game state
    public List<BoatButton> boatButtonList;
    public Boat hoverBoat;
    public long timeOfLastTouch; // Used to figure out if a user is dobble tapping the screen
    public boolean touching; // Used to avoid triggering a clause too often.
    public ShapeRenderer shaperenderer;

    public ConfigureState() {
        shaperenderer = new ShapeRenderer();

        gameconfig = new Gameconfig(10 ,10,
            new HashMap<Integer, Integer>() {
                {put(1, 1); put(2,1); put(3,1); put(4,1); }
            }
        );

        _player = new Player(gameconfig.getGrid_x(), gameconfig.getGrid_y());

        boatConfiguration = new BoatConfiguration();

        boatButtonList = new ArrayList<>();

        SetBoatButtons();
    }

    private void SetBoatButtons() {
        // Get rid of prvious buttons
        boatButtonList = new ArrayList<>();

        // Add buttons
        for (Map.Entry<Integer, Integer> entry : gameconfig.getAllowed_boats().entrySet()) {
            boatButtonList.add(new BoatButton(
                    shaperenderer,
                    150 * entry.getKey(),
                    50  ,
                    entry.getKey(),
                    entry.getValue())
            );
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Tile[] list : _player.get_grid().get_tiles()) {
            for (Tile tile : list) {
                shaperenderer.begin(ShapeRenderer.ShapeType.Line);

                if (hoverBoat != null && tile.get_rectangle().overlaps(new Rectangle(hoverBoat._posx ,hoverBoat._posy + hoverBoat._texture.getHeight() / 2,2,2))) {
                    shaperenderer.setColor(Color.RED);
                } else{
                    shaperenderer.setColor(Color.DARK_GRAY);
                }

                shaperenderer.rect(tile._posx, tile._posy, Tile.TILE_SIZE, Tile.TILE_SIZE);
                shaperenderer.end();
            }
        }

        batch.begin();

        for (BoatButton boatBtn : boatButtonList) {
            boatBtn.render(batch);
        }

        if (hoverBoat != null) {
            hoverBoat.render(batch);
        }

        for (Boat boat : boatConfiguration.boats) {
            boat.render(batch);
        }


        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched() ) {

            int input_x = Gdx.input.getX();
            int input_y = Shipocalypse.GAME_HEIGHT - Gdx.input.getY();

            Rectangle touch_rectangle = new Rectangle(input_x - 2, input_y - 2, 4, 4);

            // Just update the hoverboat position
            if (hoverBoat != null) {
                int adjusted_x = input_x - hoverBoat._texture.getWidth() / 2;
                int adjusted_y = input_y - hoverBoat._texture.getHeight() / 2;
                hoverBoat._posx = adjusted_x;
                hoverBoat._posy = adjusted_y;
            }
            // This creates a hoverboat
            // for every button
            //      if button is pressed
            //          update the hoverboat
            for (BoatButton boatBtn : boatButtonList) {
                if (touch_rectangle.overlaps(boatBtn.get_rectangle()) && !touching) {
                    touching = true;
                    int adjusted_x = input_x - boatBtn.get_texture().getWidth() / 2;
                    int adjusted_y = input_y -boatBtn.get_texture().getHeight() / 2;
                    hoverBoat = new Boat(adjusted_x, adjusted_y, boatBtn.boat_size);
                    hoverBoat._boatSize = boatBtn.boat_size;
                    hoverBoat._isVertical = boatBtn.getNextOrientation();
                }
            }

            // Double click deletes boat
            if (System.currentTimeMillis() - timeOfLastTouch > 50 && System.currentTimeMillis() - timeOfLastTouch < 200) {
                for (Boat boat : boatConfiguration.boats) {
                    if (touch_rectangle.overlaps(boat.get_rectangle())) {
                        touching = true;
                        boatConfiguration.RemoveBoat(boat);
                        gameconfig.increase_allowed_boat_type(boat._boatSize);
                        SetBoatButtons();
                        return;
                    }
                }
            } else {
                // One touch means rotating the boat
                for (Boat boat : boatConfiguration.boats) {
                    if (touch_rectangle.overlaps(boat.get_rectangle()) && !touching && hoverBoat == null) {
                        touching = true;
                        // TODO: Avoid ratating boat into other boats
                        boatConfiguration.RotateBoat(boat);
                        return;
                    }
                }
            }

            timeOfLastTouch = System.currentTimeMillis();
        } else {
            touching = false;

            // If there is a hoverboat when touch is released, then place the boat on the last matching tile
            if (hoverBoat != null) {
                for (Tile[] list : _player.get_grid().get_tiles()) {
                    for (Tile tile : list) {
                        Rectangle hoverboat_adjusted_rect = new Rectangle(hoverBoat._posx, hoverBoat._posy + hoverBoat._texture.getHeight() / 2, 2, 2);
                        if (tile.get_rectangle().overlaps(hoverboat_adjusted_rect) && checkIfPlacementIsValid(hoverBoat, tile)) {
                            if (gameconfig.subtract_allowed_boat_type(hoverBoat._boatSize)) {
                                boatConfiguration.AddBoat(tile._posx, tile._posy, hoverBoat._boatSize);
                                SetBoatButtons();
                            }
                        }
                    }
                }

                hoverBoat = null;
            }
        }
    }

    private boolean checkIfPlacementIsValid(Boat hoverBoat, Tile tile) {
        boolean placement_is_valid = true;
        // overlaps with other boat
        Rectangle hoverBoat_rectangle = new Rectangle(tile._posx, tile._posy, hoverBoat._texture.getWidth(), hoverBoat._texture.getHeight());
        for (Boat boat : boatConfiguration.boats) {
            if (hoverBoat_rectangle.overlaps(boat.get_rectangle())) {
                placement_is_valid = false;
            }
        }
        // isn't sticking out of board
        // check horizontally
        if (tile._index_x >= gameconfig.getGrid_x() - hoverBoat._boatSize + 1 ) {
            placement_is_valid = false;
        }
        // check vertically
        if (tile._index_x >= gameconfig.getGrid_y() - hoverBoat._boatSize + 1) {
            placement_is_valid = false;
        }
        return  placement_is_valid;

    }
}
