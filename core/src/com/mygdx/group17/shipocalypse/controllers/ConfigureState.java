package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.Input;
import com.mygdx.group17.shipocalypse.models.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.models.*;
import com.mygdx.group17.shipocalypse.singletons.GameManager;
import com.mygdx.group17.shipocalypse.ui.BoatButton;
import com.mygdx.group17.shipocalypse.ui.MenuButton;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigureState extends GameState {

    // These will vary between game states and players
    private Player player;
    private BoatConfiguration boatConfiguration;
    private final GameConfig gameconfig;

    // These properties are only needed for this game state
    private List<BoatButton> boatButtonList;
    private Boat hoverBoat;
    private long timeOfLastTouch; // Used to figure out if a user is dobble tapping the screen
    private boolean touching; // Used to avoid triggering a clause too often.

    private final MenuButton ready_button;

    public ConfigureState(GameConfig configuration) {
        gameconfig = configuration;
        player = new Player(gameconfig.getGrid_x(), gameconfig.getGrid_y());
        boatConfiguration = new BoatConfiguration();
        float buttonGameCenter = Options.GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;
        SetBoatButtons();
        this.ready_button = new MenuButton(AssetManager.shape, (int)buttonGameCenter, 100, "ready", Action.readyGame);
    }

    private void SetBoatButtons() {
        // Get rid of previous buttons
        boatButtonList = new ArrayList<>();
        // Add buttons
        for (Map.Entry<Integer, Integer> entry : gameconfig.getAllowed_boats().entrySet()) {
            boatButtonList.add(new BoatButton(
                    AssetManager.shape,
                    150 * entry.getKey(),
                    50  ,
                    entry.getKey(),
                    entry.getValue())
            );
        }
    }

    @Override
    public void render() {
        for (Tile[] list : player.get_grid().get_tiles()) {
            for (Tile tile : list) {
                if (hoverBoat != null && tile.getCenter().overlaps(hoverBoat.get_rectangle())) {
                    AssetManager.shape.begin(ShapeRenderer.ShapeType.Filled);
                    AssetManager.shape.setColor(Color.BLUE);
                    for (Boat boat : boatConfiguration.boats) {
                        if (boat.get_rectangle().overlaps(tile.get_rectangle())) {
                            AssetManager.shape.setColor(Color.RED);
                        }
                    }
                } else {
                    AssetManager.shape.begin(ShapeRenderer.ShapeType.Line);
                    AssetManager.shape.setColor(Color.DARK_GRAY);
                }

                AssetManager.shape.rect(tile._posx, tile._posy, Tile.TILE_SIZE, Tile.TILE_SIZE);
                AssetManager.shape.end();
            }
        }

        AssetManager.batch.begin();

        for (BoatButton boatBtn : boatButtonList) {
            boatBtn.render();
        }

        if (hoverBoat != null) {
            hoverBoat.render(AssetManager.batch);
        }

        for (Boat boat : boatConfiguration.boats) {
            boat.render(AssetManager.batch);
        }

        AssetManager.batch.end();

        if (gameconfig.allBoatsPlaced()) {
            ready_button.render(AssetManager.batch);
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
                if (touch_rectangle.overlaps(boatBtn.get_rectangle()) && !touching && boatBtn.getAllowedBoats() > 0) {
                    touching = true;
                    int adjusted_x = input_x - boatBtn.getTexture().getWidth() / 2;
                    int adjusted_y = input_y - boatBtn.getTexture().getHeight() / 2;
                    hoverBoat = new Boat(adjusted_x, adjusted_y, boatBtn.getSize());
                    hoverBoat._boatSize = boatBtn.getSize();
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
                        Tile selected_tile = new Tile(0,0,0,0);
                        for (Tile[] list : player.get_grid().get_tiles()) {
                            for (Tile tile : list) {
                                if (touch_rectangle.overlaps(tile.get_rectangle())) {
                                    selected_tile = tile;
                                    break;
                                }
                            }
                        }
                        touching = true;
                        int origin = 0;
                        for (Tile tile : boat.getTiles()) {
                            if (tile == selected_tile) {
                                origin = boat.getTiles().indexOf(tile);
                            }
                            tile.unAssign();
                        }
                        ArrayList<Tile> placement_tiles = getPlacementTiles(boat.get_rectangle(selected_tile._posx, selected_tile._posy, true, origin));
                        for (Tile tile : placement_tiles) {
                            System.out.println("TILE:");
                            System.out.println(tile._index_x);
                            System.out.println(tile._index_y);
                        }
                        if (checkIfTilesAvailable(placement_tiles) && placement_tiles.size() == boat.getSize()) {
                            boatConfiguration.RemoveBoat(boat);
                            placeBoat(boat, placement_tiles, true);
                            boat.addTiles(placement_tiles);
                        } else {
                            for (Tile tile: boat.getTiles()) {
                                tile.assign();
                            }
                        }
                        return;
                    }
                }
            }

            timeOfLastTouch = System.currentTimeMillis();
        } else {
            touching = false;

            // If there is a hoverboat when touch is released, then place the boat on the last matching tile
            if (hoverBoat != null) {
                ArrayList<Tile> placement_tiles = getPlacementTiles(hoverBoat.get_rectangle());
                if (checkIfPlacementIsValid(hoverBoat, placement_tiles) && gameconfig.subtract_allowed_boat_type(hoverBoat.getSize())) {
                    placeBoat(hoverBoat, placement_tiles, false);
                    SetBoatButtons();
                }
                hoverBoat = null;
            }
        }
        if (gameconfig.allBoatsPlaced()) {
            if (ready_button.handleInput()) {
                for (Boat boat : boatConfiguration.boats) {
                }
                boatConfiguration.debug();
                GameManager.setConfig(gameconfig, player, boatConfiguration);
                GameManager.setState(State.play);
            }
        }
    }

    public ArrayList<Tile> getPlacementTiles(Rectangle boat_rectangle) {
        ArrayList<Tile> placement_tiles = new ArrayList<Tile>();
        for (Tile[] tile_list : player.get_grid().get_tiles()) {
            for (Tile tile: tile_list) {
                if (tile.getCenter().overlaps(boat_rectangle)) {
                    placement_tiles.add(tile);
                }
            }
        }
        return placement_tiles;
    }

    private void placeBoat(Boat boat, ArrayList<Tile> placement_tiles, boolean vertical) {
        boat.setPosition(placement_tiles.get(0)._posx, placement_tiles.get(0)._posy);
        if (vertical) {
            boat.turn_boat();
        }
        boatConfiguration.AddBoat(boat, placement_tiles);
        for (Tile tile : placement_tiles) {
            tile.assign();
        }
    }

    private boolean checkIfPlacementIsValid(Boat hoverBoat, ArrayList<Tile> placement_tiles) {
        boolean placement_valid = true;
        if (placement_tiles.size() != hoverBoat.getSize()) {
            placement_valid = false;
        }
        for (Tile tile : placement_tiles) {
            if (tile.isOccupied()) {
                System.out.println("some tile is occupied");
                placement_valid = false;
            }
        }
        return  placement_valid;
    }

    private boolean checkIfTilesAvailable(ArrayList<Tile> placement_tiles) {
        boolean tiles_available = true;
        for (Tile tile : placement_tiles) {
            if (tile.isOccupied()) {
                System.out.println("some tile is occupied");
                tiles_available = false;
            }
        }
        return  tiles_available;
    }
}


