package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.models.Boat;
import com.mygdx.group17.shipocalypse.models.Grid;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.models.State;
import com.mygdx.group17.shipocalypse.models.Tile;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.singletons.GameManager;
import com.mygdx.group17.shipocalypse.ui.MenuButton;
import com.mygdx.group17.shipocalypse.ui.MissileButton;
import com.mygdx.group17.shipocalypse.ui.MissileType;
import com.sun.tools.javac.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayState extends GameState {
    public static boolean selection = false;
    Tile selected_tile;
    public MenuButton fire_button;

    public ArrayList<MissileButton> missile_buttons;
    public MissileButton selected_missile_button;
    private String selected_player_id;

    public PlayState() {
        missile_buttons = new ArrayList<MissileButton>() {};
        missile_buttons.add(new MissileButton(AssetManager.shape, 50, 100, 75, MissileType.Normal));
        missile_buttons.add(new MissileButton(AssetManager.shape, 150, 100, 75, MissileType.Healing));
        missile_buttons.add(new MissileButton(AssetManager.shape, 250, 100, 75, MissileType.TripleRandom));
        missile_buttons.add(new MissileButton(AssetManager.shape, 350, 100, 75, MissileType.Vision));

        this.fire_button = new MenuButton(AssetManager.shape, Options.GAME_WIDTH-75-10, 100, "Fire!", 75);
        this.selected_tile = new Tile(0, 0, 0, 0);
        debug();
        // Change the y coordinate of player/opponent tiles to display both grids.
        for (Boat boat : GameManager.getPlayer("2").getBoatConfig().boats) {
            boat.shiftUp();
        }
        for (Boat boat : GameManager.getPlayer("1").getBoatConfig().boats) {
            boat.shiftDown();
        }
        for (Tile[] list : GameManager.getPlayer("1").get_grid().get_tiles()) {
            for (Tile tile : list) {
                tile.shiftDown();
            }
        }
        for (Tile[] list : GameManager.getPlayer("2").get_grid().get_tiles()) {
            for (Tile tile : list) {
                tile.shiftUp();
            }
        }
    }

    private void debug() {

        System.out.println("Opponent boats:");
        for (Boat boat : GameManager.getPlayer("2").getBoatConfig().boats) {
            System.out.println("- Boat with tiles: ");
            for (Tile tile : boat.getTiles()) {
                System.out.println("  - Tile at X: " + tile._index_x + ", Y: " + tile._index_y);
            }
            System.out.println(" - Boat hit flags:");
            for (boolean hit : boat.getHits()) {
                System.out.println("  - " + hit);
            }
        }

    }

    @Override
    public void render() {

        for (Tile[] list : GameManager.getPlayer("2").get_grid().get_tiles()) {
            for (Tile tile : list) {
                AssetManager.shape.begin(ShapeRenderer.ShapeType.Line);
                AssetManager.shape.setColor(Color.DARK_GRAY);
                AssetManager.shape.rect(tile._posx, tile._posy , Tile.TILE_SIZE, Tile.TILE_SIZE);
                AssetManager.shape.end();
            }
        }

        for (Tile[] list : GameManager.getPlayer("1").get_grid().get_tiles()) {
            for (Tile tile : list) {
                AssetManager.shape.begin(ShapeRenderer.ShapeType.Line);
                AssetManager.shape.setColor(Color.DARK_GRAY);
                AssetManager.shape.rect(tile._posx, tile._posy , Tile.TILE_SIZE, Tile.TILE_SIZE);
                AssetManager.shape.end();
            }
        }

        for (Boat boat : GameManager.getPlayer("2").getBoatConfig().boats) {
            if (boat.isSunk()) {
                boat.render(AssetManager.batch);
            }
        }

        for (Boat boat : GameManager.getPlayer("1").getBoatConfig().boats) {
            boat.render(AssetManager.batch);
        }

        for (Tile[] list : GameManager.getPlayer("2").get_grid().get_tiles()) {
            for (Tile tile : list) {
                if (tile.burning) {
                    AssetManager.draw(AssetManager.fire_sprite, tile._posx - 10, tile._posy);
                } else if (tile.isSelected()) {
                    AssetManager.draw(AssetManager.crosshair_sprite, tile._posx, tile._posy);
                } else if (tile.isExposed()) {
                    AssetManager.draw(AssetManager.exclamation_point_sprite, tile._posx, tile._posy);
                } else if (tile.isHit()) {
                    AssetManager.draw(AssetManager.cross_sprite, tile._posx, tile._posy);
                }
            }
        }

        for (Tile[] list : GameManager.getPlayer("1").get_grid().get_tiles()) {
            for (Tile tile : list) {
                if (tile.burning) {
                    AssetManager.draw(AssetManager.fire_sprite, tile._posx, tile._posy);
                } else if (tile.isSelected()) {
                    AssetManager.draw(AssetManager.crosshair_sprite, tile._posx, tile._posy);
                } else if (tile.isHit()) {
                    AssetManager.draw(AssetManager.cross_sprite, tile._posx, tile._posy);
                }
            }
        }

        if (selected_missile_button != null) {
            if (selection || selected_missile_button.getMissileType() == MissileType.TripleRandom) {
                fire_button.render();
            }
        }

        for (MissileButton missileButton : missile_buttons) {
            if (selected_missile_button == missileButton) {
                missileButton.render(Color.BLUE);
            } else if (!missileButton.can_fire()) {
                missileButton.render(Color.LIGHT_GRAY);
            } else {
                missileButton.render(Color.RED);
            }
        }

    }


    @Override
    public void dispose() {
    }

    @Override
    public void handleInput() {
        // TODO: Make this waiting until other player is done
        while (GameManager.getActive_player().is_skipable_player()) {
            GameManager.getPlayer("1").hit_random_tile();
            GameManager.getPlayer("1").checkDefeat();
            if (GameManager.getPlayer(("1")).allShipsSunk()) {
                GameManager.setState(State.gameEnd);
            }
            GameManager.endTurn();
        }

        for (MissileButton missileButton : missile_buttons) {
            if (missileButton.handleInput()) {
                selected_missile_button = missileButton;
                if (!selected_missile_button.can_fire()) {
                    selected_missile_button = null;
                }
            }
        }

        if (Gdx.input.isTouched()) {
            // player should not be able to select target tile on triple *random*
            if(selected_missile_button != null && selected_missile_button.getMissileType() != MissileType.TripleRandom) {
                int input_x = Gdx.input.getX();
                int input_y = Options.GAME_HEIGHT - Gdx.input.getY();
                Rectangle touch_rectangle = new Rectangle(input_x - 2, input_y - 2, 4, 4);

                Grid grid = selected_missile_button.getMissileType() == MissileType.Healing ?
                        GameManager.getPlayer("1").get_grid()
                        :
                        GameManager.getPlayer("2").get_grid();

                // Check if our currently selected tile is in the grid applicable to the
                // currently selected missile.
                if (selected_tile != null) {
                    boolean tile_found_in_applicable_grid = false;
                    for (Tile[] tiles : grid.get_tiles()) {
                        for (Tile tile : tiles) {
                            if (tile == selected_tile) {
                                tile_found_in_applicable_grid = true;
                                break;
                            }
                        }
                    }
                    if (!tile_found_in_applicable_grid) {
                        selected_tile.deselect();
                        selection = false;
                        selected_tile = null;
                    }
                }

                for (Tile[] list : grid.get_tiles()) {
                    for (Tile tile : list) {
                        if (touch_rectangle.overlaps(tile.get_rectangle())) {
                            if (selected_tile != null) {
                                selected_tile.deselect();
                            }
                            if (selected_missile_button.getMissileType() != MissileType.Healing && !tile.isHit()) {
                                tile.select();
                                selected_tile = tile;
                                selection = true;
                            } else if (selected_missile_button.getMissileType() == MissileType.Healing && tile.isHit()) {
                                for (Boat boat:  GameManager.getPlayer("1").getBoatConfig().boats) {
                                    if (boat.containsTile(tile)) {
                                        tile.select();
                                        selected_tile = tile;
                                        selection = true;
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            } else if (selected_missile_button != null && selected_missile_button.getMissileType() == MissileType.TripleRandom) {
                if (selected_tile != null) {
                    selected_tile.deselect();
                }
                selected_tile = null;
                selection = false;
            }

            for (Tile[] list : GameManager.getPlayer("2").get_grid().get_tiles()) {
                for (Tile tile : list) {
                    if (tile == selected_tile) {
                        tile.select();
                    }
                    else {
                        tile.deselect();
                    }
                }
            }

            if (fire_button.handleInput() && selected_missile_button != null  && selected_missile_button.can_fire()) {
                // end_turn: A boolean for ensuring that a turn doesn't end unless a shot is confirmed.
                // Used for cases like healing missile where missing is not permitted, such that the turn doesnt end.
                boolean end_turn = false;

                if (selection) {
                    System.out.println("Fired " + selected_missile_button.getMissileType().name() + " at tile " + String.valueOf(selected_tile.getIndex()[0]) + " : " + String.valueOf(selected_tile.getIndex()[1]));
                    if (selected_missile_button.getMissileType() == MissileType.Normal) {
                        end_turn = true;
                        for (Boat boat: GameManager.getPlayer("2").getBoatConfig().boats) {
                            for (Tile tile : boat.getTiles()) {
                                if (selected_tile == tile) {
                                    boat.hit(tile);
                                    if (boat.isSunk()) {
                                        boat.show();
                                    }
                                }
                                selected_tile.hit();
                            }
                        }
                    }

                    if (selected_missile_button.getMissileType() == MissileType.Vision) {
                        end_turn = true;
                        // get all tiles around selected
                        ArrayList<Tile> surrounding_tiles = GameManager.getPlayer("2").get_grid().get_surrounding_tiles(selected_tile);

                        for (Tile target_tile : surrounding_tiles) {
                            for (Boat boat: GameManager.getPlayer("2").getBoatConfig().boats) {
                                for (Tile boat_tile : boat.getTiles()) {
                                    if (boat_tile == target_tile) {
                                        target_tile.exposed();
                                    } /*else {
                                        target_tile.hit();
                                    } causes issues */
                                }
                            }
                        }
                    }

                    if (selected_missile_button.getMissileType() == MissileType.Healing) {
                        for (Boat boat: GameManager.getPlayer("1").getBoatConfig().boats) {
                            for (Tile boat_tile: boat.getTiles()) {
                                if (boat_tile == selected_tile) {
                                    end_turn = true;
                                    boat.heal(boat_tile);
                                }
                            }
                        }
                    }
                    if (end_turn) {
                        selected_tile.deselect();
                        selected_tile = null;
                        selection = false;
                    }
                }

                if (selected_missile_button.getMissileType() == MissileType.TripleRandom) {
                    end_turn = true;
                    // Generate 3 random tiles
                    Tile[] random_tiles = {null, null, null};
                    Grid grid = GameManager.getPlayer("2").get_grid();
                    for (int i = 0; i < 3; i++) {
                        random_tiles[i] = grid.get_random_tile();
                        // Hit here to avoid calling the hit function many times
                        // on each random tile in the later loop.
                        random_tiles[i].hit();
                    }
                    // Check for a hit on any of the random tiles.
                    for (Boat boat: GameManager.getPlayer("2").getBoatConfig().boats) {
                        for (Tile boat_tile: boat.getTiles()) {
                            if (Arrays.asList(random_tiles).contains(boat_tile)) {
                                boat.hit(boat_tile);
                                if (boat.isSunk()) {
                                    boat.show();
                                }
                            }
                        }
                    }
                }
                if (end_turn) {
                    selected_missile_button.use_ammunition();
                    selected_missile_button = null;
                    GameManager.endTurn();
                }
            }
        }
        GameManager.getPlayer("2").checkDefeat();
        if (GameManager.getPlayer(("2")).allShipsSunk()) {
            GameManager.setState(State.gameEnd);
        }
    }
}

