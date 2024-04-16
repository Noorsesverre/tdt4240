package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
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
import java.util.Random;

public class ConfigureState extends GameState {

    private final GameConfig gameconfig;
    // These properties are only needed for this game state
    private boolean ghost = false;
    private ArrayList<ArrayList<Tile>> ghost_tiles; // Used to see how boats may be rotated
    private Boat ghost_boat;
    private List<BoatButton> boatButtonList;
    private Boat hoverBoat;
    private long timeOfLastTouch; // Used to figure out if a user is dobble tapping the screen
    private boolean touching; // Used to avoid triggering a clause too often.

    private final MenuButton ready_button;

    public ConfigureState(GameConfig configuration) {
        gameconfig = configuration;


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
                    43+75 * entry.getKey(),
                    167  ,
                    entry.getKey(),
                    entry.getValue())
            );
        }
    }

    @Override
    public void render() {
        for (Tile[] list : GameManager.getPlayer("1").get_grid().get_tiles()) {
            for (Tile tile : list) {
                if (hoverBoat != null && tile.getCenter().overlaps(hoverBoat.get_rectangle())) {
                    AssetManager.shape.begin(ShapeRenderer.ShapeType.Filled);
                    AssetManager.shape.setColor(Color.BLUE);
                    for (Boat boat : GameManager.getPlayer("1").getBoatConfig().boats) {
                        if (boat.get_rectangle().overlaps(tile.get_rectangle())) {
                            AssetManager.shape.setColor(Color.RED);
                        }
                    }
                } else if (ghost && ghost_tiles.get(0).contains(tile) && Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    for (Tile ghost_tile : ghost_boat.getTiles()) {
                        ghost_tile.unAssign();
                    }
                    AssetManager.shape.begin(ShapeRenderer.ShapeType.Filled);
                    if (checkIfTilesAvailable(ghost_tiles) && ghost_tiles.get(0).size() == ghost_boat.getSize()) {
                        AssetManager.shape.setColor(Color.BLUE);
                    } else {
                        AssetManager.shape.setColor(Color.RED);
                    }
                    for (Tile ghost_tile : ghost_boat.getTiles()) {
                        ghost_tile.assign();
                    }
                } else {
                        AssetManager.shape.begin(ShapeRenderer.ShapeType.Line);
                        AssetManager.shape.setColor(Color.DARK_GRAY);
                }

                AssetManager.shape.rect(tile._posx, tile._posy, Tile.TILE_SIZE, Tile.TILE_SIZE);
                AssetManager.shape.end();
            }
        }

        ghost = false;

        for (BoatButton boatBtn : boatButtonList) {
            boatBtn.render();
        }


        if (hoverBoat != null) {
            hoverBoat.render(AssetManager.batch);
        }

        for (Boat boat : GameManager.getPlayer("1").getBoatConfig().boats) {
            boat.render(AssetManager.batch);
        }


        if (gameconfig.allBoatsPlaced()) {
            ready_button.render();
        }

    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleInput() {
        // Use viewport/AssetManager to unproject input coordinates to game world coordinates.
        Vector3 input_vector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 projected_vector = AssetManager.unprojectInput(input_vector);
        Rectangle touch_rectangle = new Rectangle(projected_vector.x - 2, projected_vector.y - 2, 2, 2);

        int android_correction = 0;
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            android_correction = android_correction + 100;
        }
        if (Gdx.input.isTouched() ) {

            // Just update the hoverboat position
            if (hoverBoat != null) {
                int adjusted_x = (int)projected_vector.x - hoverBoat._texture.getWidth() / 2;
                int adjusted_y = (int)projected_vector.y - hoverBoat._texture.getHeight() / 2;
                hoverBoat._posx = adjusted_x;
                hoverBoat._posy = adjusted_y + android_correction;
            }
            // This creates a hoverboat
            // for every button
            //      if button is pressed
            //          update the hoverboat
            for (BoatButton boatBtn : boatButtonList) {
                if (touch_rectangle.overlaps(boatBtn.get_rectangle()) && !touching && boatBtn.getAllowedBoats() > 0) {
                    touching = true;
                    int adjusted_x = (int)projected_vector.x - boatBtn.getTexture().getWidth() / 2;
                    int adjusted_y = (int)projected_vector.y - boatBtn.getTexture().getHeight() / 2;
                    hoverBoat = new Boat(adjusted_x, adjusted_y + android_correction, boatBtn.getSize());
                }
            }

            // Double click deletes boat
            if (System.currentTimeMillis() - timeOfLastTouch > 50 && System.currentTimeMillis() - timeOfLastTouch < 200) {
                for (Boat boat : GameManager.getPlayer("1").getBoatConfig().boats) {
                    if (touch_rectangle.overlaps(boat.get_rectangle())) {
                        touching = true;
                        for (Boat opp_boat : GameManager.getPlayer("2").getBoatConfig().boats) {
                            if (opp_boat.getSize() == boat.getSize()) {
                                GameManager.getPlayer("2").getBoatConfig().RemoveBoat(opp_boat);
                                break;
                            }
                        }
                        GameManager.getPlayer("1").getBoatConfig().RemoveBoat(boat);
                        gameconfig.increase_allowed_boat_type(boat._boatSize);
                        SetBoatButtons();
                        return;
                    }
                }
            } else {
                // One touch means rotating the boat
                for (Boat boat : GameManager.getPlayer("1").getBoatConfig().boats) {
                    if (touch_rectangle.overlaps(boat.get_rectangle()) && !touching && hoverBoat == null) {
                        Tile selected_tile = new Tile(0,0,0,0);
                        for (Tile[] list : GameManager.getPlayer("1").get_grid().get_tiles()) {
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
                        ArrayList<ArrayList<Tile>> placement_tiles = getPlacementTiles(boat.get_rectangle(selected_tile._posx, selected_tile._posy, true, origin), GameManager.getPlayer("1"));
                        if (checkIfTilesAvailable(placement_tiles) && placement_tiles.size() == boat.getSize()) {
                            GameManager.getPlayer("1").getBoatConfig().RemoveBoat(boat);
                            placeBoat(boat, placement_tiles.get(0), true, GameManager.getPlayer("1"));
                            boat.addTiles(placement_tiles.get(0));
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

            for (Boat boat : GameManager.getPlayer("1").getBoatConfig().boats) {
                if (touch_rectangle.overlaps(boat.get_rectangle())) {
                    ghost = true;
                    Tile possibly_selected_tile = new Tile(0,0,0,0);
                    for (Tile[] list : GameManager.getPlayer("1").get_grid().get_tiles()) {
                        for (Tile tile : list) {
                            if (touch_rectangle.overlaps(tile.get_rectangle())) {
                                possibly_selected_tile = tile;
                                break;
                            }
                        }
                    }
                    int origin = 0;
                    for (Tile tile : boat.getTiles()) {
                        if (tile == possibly_selected_tile) {
                            origin = boat.getTiles().indexOf(tile);
                        }
                    }
                    ghost_tiles = getPlacementTiles(boat.get_rectangle(possibly_selected_tile._posx, possibly_selected_tile._posy, true, origin), GameManager.getPlayer("1"));
                    ghost_boat = boat;
                    break;
                }
                ghost = false;
            }

            // If there is a hoverboat when touch is released, then place the boat on the last matching tile
            if (hoverBoat != null) {
                ArrayList<ArrayList<Tile>> placement_tiles = getPlacementTiles(hoverBoat.get_rectangle(), GameManager.getPlayer("1"));
                if (checkIfPlacementIsValid(hoverBoat, placement_tiles) && gameconfig.subtract_allowed_boat_type(hoverBoat.getSize())) {
                    placeBoat(hoverBoat, placement_tiles.get(0), false, GameManager.getPlayer("1"));
                    SetBoatButtons();
                    placeRandomOpponent(new Boat(hoverBoat._posx, hoverBoat._posy, hoverBoat._boatSize));
                }
                hoverBoat = null;
            }
        }
        if (gameconfig.allBoatsPlaced()) {
            if (ready_button.handleInput()) {
                GameManager.setConfig(gameconfig);
                GameManager.setState(State.play);
            }
        }
    }

    public ArrayList<ArrayList<Tile>> getPlacementTiles(Rectangle boat_rectangle, Player pl) {
        ArrayList<ArrayList<Tile>> placement_tiles = new ArrayList<ArrayList<Tile>>();
        placement_tiles.add(new ArrayList<Tile>());
        placement_tiles.add(new ArrayList<Tile>());
        for (Tile[] tile_list : pl.get_grid().get_tiles()) {
            for (Tile tile: tile_list) {
                if (tile.getCenter().overlaps(boat_rectangle)) {
                    placement_tiles.get(0).add(tile);
                    for (Tile adjacent_tile : tile.getAdjacentTiles()) {
                        placement_tiles.get(1).add(adjacent_tile);
                    }
                }
            }
        }
        return placement_tiles;
    }

    private void placeBoat(Boat boat, ArrayList<Tile> placement_tiles, boolean vertical, Player pl) {
        boat.setPosition(placement_tiles.get(0)._posx, placement_tiles.get(0)._posy);
        if (vertical) {
            boat.turn_boat();
        }
        pl.getBoatConfig().AddBoat(boat, placement_tiles);
        for (Tile tile : placement_tiles) {
            tile.assign();
        }
    }

    private void placeOpponent(Boat boat, ArrayList<Tile> placement_tiles, Player pl) {
        boat.setPosition(placement_tiles.get(0)._posx, placement_tiles.get(0)._posy);
        pl.getBoatConfig().AddBoat(boat, placement_tiles);
        for (Tile tile : placement_tiles) {
            tile.assign();
        }
    }
    // Temp method to create opponent boards
    private void placeRandomOpponent(Boat boat) {
        Random random = new Random();
        boolean found_place = false;
        while (!found_place) {
            boat.move();
            System.out.println(boat._posx + "  " + boat._posy);
            ArrayList<ArrayList<Tile>> placement_tiles = getPlacementTiles(boat.get_rectangle(), GameManager.getPlayer("2"));
            if (checkIfPlacementIsValid(boat, placement_tiles)) {
                placeOpponent(boat, placement_tiles.get(0), GameManager.getPlayer("2"));
                found_place = true;
            }
        }
    }

    private boolean checkIfPlacementIsValid(Boat hoverBoat, ArrayList<ArrayList<Tile>> placement_tiles) {
        boolean placement_valid = true;
        if (placement_tiles.get(0).size() != hoverBoat.getSize()) {
            placement_valid = false;
        }
        for (ArrayList<Tile> list : placement_tiles) {
            for (Tile tile : list) {
                if (tile.isOccupied()) {
                    System.out.println("Tile at " + tile._index_x + ":" + tile._index_y + "is occupied");
                    placement_valid = false;
                }
            }
        }
        return  placement_valid;
    }

    private boolean checkIfTilesAvailable(ArrayList<ArrayList<Tile>> placement_tiles) {
        boolean tiles_available = true;
        for (ArrayList<Tile> list : placement_tiles) {
            for (Tile tile : list) {
                if (tile.isOccupied()) {
                    System.out.println("Tile at " + tile._index_x + ":" + tile._index_y + " is occupied");
                    tiles_available = false;
                }
            }
        }
        return  tiles_available;
    }
}


