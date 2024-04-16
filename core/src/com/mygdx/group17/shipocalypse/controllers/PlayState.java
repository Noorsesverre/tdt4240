package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.models.Boat;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.models.State;
import com.mygdx.group17.shipocalypse.models.Tile;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.singletons.GameManager;
import com.mygdx.group17.shipocalypse.ui.MenuButton;
import com.mygdx.group17.shipocalypse.ui.MissleButton;
import com.mygdx.group17.shipocalypse.ui.MissleType;

import java.util.ArrayList;

public class PlayState extends GameState {
    public static boolean selection = false;
    Tile selected_tile;
    public MenuButton fire_button;

    public ArrayList<MissleButton> missle_buttons;
    public MissleButton selected_missle_button;
    private String selected_player_id;

    public PlayState() {
        missle_buttons = new ArrayList<MissleButton>() {};
        missle_buttons.add(new MissleButton(AssetManager.shape, 50, 100, 75, MissleType.Normal));
        missle_buttons.add(new MissleButton(AssetManager.shape, 150, 100, 75, MissleType.Healing));
        missle_buttons.add(new MissleButton(AssetManager.shape, 250, 100, 75, MissleType.TripleRandom));
        missle_buttons.add(new MissleButton(AssetManager.shape, 350, 100, 75, MissleType.Vision));

        this.fire_button = new MenuButton(AssetManager.shape, Options.GAME_WIDTH-75-10, 100, "fire", 75);
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
                }
                else if (tile.isHit()) {
                    AssetManager.draw(AssetManager.cross_sprite, tile._posx, tile._posy);
                }
            }
        }

        if (selection && selected_missle_button != null) {
            fire_button.render();
        }

        for (MissleButton missleButton : missle_buttons) {
            if (selected_missle_button == missleButton) {
                missleButton.render(Color.BLUE);
            } else {
                missleButton.render(Color.RED);

            }
        }

    }


    @Override
    public void dispose() {
    }
    @Override
    public void handleInput() {
        // TODO: Make this waiting until other player is done
        if (GameManager.getActive_player().is_skipable_player()) {
            GameManager.getPlayer("1").hit_random_tile();
            GameManager.getPlayer("1").checkDefeat();
            GameManager.endTurn();
        }

        if (Gdx.input.isTouched() ) {
            int input_x = Gdx.input.getX();
            int input_y = Options.GAME_HEIGHT - Gdx.input.getY();
            Rectangle touch_rectangle = new Rectangle(input_x - 2, input_y - 2, 4, 4);

            for (Tile[] list : GameManager.getPlayer("2").get_grid().get_tiles()) {
                for (Tile tile : list) {
                    if (touch_rectangle.overlaps(tile.get_rectangle()) && !tile.isHit()) {
                        selected_tile = tile;
                        selection = true;
                        break;
                    }
                }
            }

//            // NOTE: expensive code to run 😱
//            for (Tile[] list : GameManager.getPlayer("1").get_grid().get_tiles()) {
//                for (Tile tile : list) {
//                    if (touch_rectangle.overlaps(tile.get_rectangle()) && tile.isHit()) {
//                        for (Boat boat: GameManager.getPlayer("1").getBoatConfig().boats) {
//                            for (Tile boat_tile : boat.getTiles()) {
//                                if (boat_tile == tile) {
//                                    selected_player_id = "1";
//                                    selected_tile = tile;
//                                    selection = true;
//                                    break;
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }

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

            for (MissleButton missleButton : missle_buttons) {
                if (missleButton.handleInput()) {
                    selected_missle_button = missleButton;
                }
            }

            if (fire_button.handleInput() && selection && selected_missle_button != null) {
                System.out.println("Fired " + selected_missle_button.getMissleType().name() + " at tile " + String.valueOf(selected_tile.getIndex()[0]) + " : " + String.valueOf(selected_tile.getIndex()[1]));

                // Logic MissleType.Normal
                if (selected_missle_button.getMissleType() == MissleType.Normal) {
                    for (Boat boat: GameManager.getPlayer("2").getBoatConfig().boats) {
                        for (Tile tile : boat.getTiles()) {
                            if (selected_tile == tile) {
                                boat.hit(tile);
                                if (boat.isSunk()) {
                                    boat.show();
                                }
                                selected_tile.hit();
                                GameManager.getPlayer("2").checkDefeat();
                                debug();
                            }
                        }
                    }
                }

                // Logic MissleType.Vision
                if (selected_missle_button.getMissleType() == MissleType.Vision) {
                    // get all tiles around selected
                    ArrayList<Tile> surrounding_tiles = GameManager.getPlayer("2").get_grid().get_surrounding_tiles(selected_tile);

                    for (Tile target_tile : surrounding_tiles) {
                        for (Boat boat: GameManager.getPlayer("2").getBoatConfig().boats) {
                            for (Tile boat_tile : boat.getTiles()) {
                                if (boat_tile == target_tile) {
                                    target_tile.exposed();
                                    debug();
                                } else {
                                    target_tile.hit();
                                }
                            }
                        }
                    }
                }

                // Logic MissleType.TrippleRandom
                if (selected_missle_button.getMissleType() == MissleType.TripleRandom) {
                    for (int i = 0; i < 3; i++) {
                        GameManager.getPlayer("2").hit_random_tile();
                    }
                }

                // Logic MissleType.Healing
                if (selected_missle_button.getMissleType() == MissleType.Healing) {
                    // should only be able to this to your boats

                    // just an undo of boat hits

                }
                selected_tile = null;

                selected_missle_button = null;
                selection = false;
                GameManager.endTurn();
            }
        }
        if (GameManager.getPlayer("2").allShipsSunk()) {
            GameManager.setState(State.gameEnd);
        }
    }


}

