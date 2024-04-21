package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.group17.shipocalypse.controllers.ConfigureState;
import com.mygdx.group17.shipocalypse.models.Boat;
import com.mygdx.group17.shipocalypse.models.BoatConfiguration;
import com.mygdx.group17.shipocalypse.models.Grid;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.models.Player;
import com.mygdx.group17.shipocalypse.models.State;
import com.mygdx.group17.shipocalypse.models.Tile;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.singletons.GameManager;

import java.util.ArrayList;
import java.util.Objects;

public class Board {

    private final Grid grid;
    private final BoatConfiguration boat_config;
    private final String id;

    public Board(Player player) {
        grid = player.get_grid();
        boat_config = player.getBoatConfig();
        id = player.getPlayer_id();
    }

    public void render() {
        for (Tile[] list : grid.get_tiles()) {
            for (Tile tile : list) {
                AssetManager.shape.begin(ShapeRenderer.ShapeType.Line);
                AssetManager.shape.setColor(Color.NAVY);
                AssetManager.shape.rect(tile._posx, tile._posy , Tile.TILE_SIZE, Tile.TILE_SIZE);
                AssetManager.shape.end();
            }
        }
        for (Boat boat : boat_config.boats) {
            if (Objects.equals(id, "1") || boat.isSunk()) {
                boat.render(AssetManager.batch);
            }
        }

        for (Tile[] list : grid.get_tiles()) {
            for (Tile tile : list) {
                if (tile.burning) {
                    AssetManager.draw(AssetManager.fire_sprite, tile._posx, tile._posy + 7);
                }
                else if (tile.isHit()) {
                    AssetManager.draw(AssetManager.cross_sprite, tile._posx, tile._posy);
                }
                else if (tile.isSelected()) {
                    AssetManager.draw(AssetManager.crosshair_sprite, tile._posx, tile._posy);
                } else if (tile.isHit()) {
                    AssetManager.draw(AssetManager.cross_sprite, tile._posx, tile._posy);
                } else if (tile.isExposed()) {
                    AssetManager.draw(AssetManager.exclamation_point_sprite, tile._posx, tile._posy);
                }
            }
        }
    }


    public void render(ConfigureState controller) {
        for (Tile[] list : grid.get_tiles()) {
            for (Tile tile : list) {
                if (controller.hoverBoat != null && tile.getCenter().overlaps(controller.hoverBoat.get_rectangle())) {
                    AssetManager.shape.begin(ShapeRenderer.ShapeType.Filled);
                    AssetManager.shape.setColor(Color.BLUE);
                    for (Boat boat : GameManager.getPlayer("1").getBoatConfig().boats) {
                        if (boat.get_rectangle().overlaps(tile.get_rectangle())) {
                            AssetManager.shape.setColor(Color.RED);
                        }
                    }
                } else if (controller.ghost && controller.ghost_tiles.get(0).contains(tile) && Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    for (Tile ghost_tile : controller.ghost_boat.getTiles()) {
                        ghost_tile.unAssign();
                    }
                    AssetManager.shape.begin(ShapeRenderer.ShapeType.Filled);
                    if (controller.checkIfTilesAvailable(controller.ghost_tiles) && controller.ghost_tiles.get(0).size() == controller.ghost_boat.getSize()) {
                        AssetManager.shape.setColor(Color.BLUE);
                    } else {
                        AssetManager.shape.setColor(Color.RED);
                    }
                    for (Tile ghost_tile : controller.ghost_boat.getTiles()) {
                        ghost_tile.assign();
                    }
                } else {
                    AssetManager.shape.begin(ShapeRenderer.ShapeType.Line);
                    AssetManager.shape.setColor(Color.NAVY);
                }
                AssetManager.shape.rect(tile._posx, tile._posy, Tile.TILE_SIZE, Tile.TILE_SIZE);
                AssetManager.shape.end();
            }
        }
    }
}
