package com.mygdx.group17.shipocalypse.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.group17.shipocalypse.models.Boat;
import com.mygdx.group17.shipocalypse.models.Grid;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.models.State;
import com.mygdx.group17.shipocalypse.models.Tile;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.singletons.GameManager;
import com.mygdx.group17.shipocalypse.ui.Board;
import com.mygdx.group17.shipocalypse.ui.MenuButton;
import com.mygdx.group17.shipocalypse.ui.MissileButton;
import com.mygdx.group17.shipocalypse.ui.MissileType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PlayState extends GameState {
    public static boolean selection = false;
    Tile selected_tile;
    public MenuButton fire_button;
    private final String opponent_id;
    public ArrayList<MissileButton> missile_buttons;
    public MissileButton selected_missile_button;
    public boolean waiting;
    private long turn_time;
    private long frame_time;
    private long frame_time_last;

    private Board player_board;
    private Board opponent_board;
    private String selected_player_id;

    public final State state;

    private long db_update; // used to not read from db too often
    public PlayState() {
        state = State.play;
        frame_time = System.currentTimeMillis();
        frame_time_last = System.currentTimeMillis();

        turn_time = 60000 * GameManager.turntime + 999;

        if (GameManager.single_player) {
            opponent_id = "2";
            waiting = false;
        } else {
            opponent_id = "0";
            waiting = true;
        }
        missile_buttons = new ArrayList<MissileButton>() {};
        missile_buttons.add(new MissileButton(AssetManager.shape, 50, 100, 75, MissileType.Normal));
        missile_buttons.add(new MissileButton(AssetManager.shape, 150, 100, 75, MissileType.Healing));
        missile_buttons.add(new MissileButton(AssetManager.shape, 250, 100, 75, MissileType.TripleRandom));
        missile_buttons.add(new MissileButton(AssetManager.shape, 350, 100, 75, MissileType.Vision));

        this.fire_button = new MenuButton(AssetManager.shape, Options.GAME_WIDTH-75-10, 100, "Fire!", 75);
        this.selected_tile = new Tile(0, 0, 0, 0);
        debug();
        // Change the y coordinate of player/opponent tiles to display both grids.
        for (Boat boat : GameManager.getPlayer(opponent_id).getBoatConfig().boats) {
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
        for (Tile[] list : GameManager.getPlayer(opponent_id).get_grid().get_tiles()) {
            for (Tile tile : list) {
                tile.shiftUp();
            }
        }
        player_board = new Board(GameManager.getPlayer("1"));
        opponent_board = new Board(GameManager.getPlayer(opponent_id));
        db_update = System.currentTimeMillis();
    }

    private void debug() {

        System.out.println("Opponent boats:");
        for (Boat boat : GameManager.getPlayer(opponent_id).getBoatConfig().boats) {
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
        opponent_board.render();
        player_board.render();


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

        if (waiting) {
            AssetManager.write("WAITING FOR OPPONENT", Options.GAME_WIDTH/2 - 100, Options.GAME_HEIGHT/2);
        } else {
            String minutes = String.valueOf(turn_time/60000);
            String seconds = String.valueOf((turn_time%60000)/1000);
            if (seconds.length() > 2) {
                seconds = seconds.substring(0,1);
            } else if (seconds.length() < 2) {
                seconds = "0" + seconds;
            }
            AssetManager.write("TURN TIME:\n" + minutes + ":" + seconds, 20, Options.GAME_HEIGHT/2);
        }

    }


    @Override
    public void dispose() {
    }

    public void opponentTurn(HashMap<String, Object> turn_info) {
        System.out.println(turn_info);
        if (turn_info.get("missile") != null) {
            int missile_id = (int)turn_info.get("missile");
            ArrayList<Tile> hit_tiles = new ArrayList<>();

            ArrayList<HashMap<String, Integer>> tile_coordinates = (ArrayList) turn_info.get("tile");
            Grid grid = missile_id == 2 ?
                    GameManager.getPlayer(opponent_id).get_grid()
                    :
                    GameManager.getPlayer("1").get_grid();

            for (HashMap<String, Integer> coordinate : tile_coordinates) {
                for (Tile[] tileset : grid.get_tiles()) {
                    for (Tile tile : tileset) {
                        if (tile._index_x == coordinate.get("X") && tile._index_y == coordinate.get("Y")) {
                            hit_tiles.add(tile);
                        }
                    }
                }
            }
            switch (missile_id) {
                case 2:
                    for (Tile tile : hit_tiles) {
                        for (Boat boat : GameManager.getPlayer(opponent_id).getBoatConfig().boats) {
                            if (boat.containsTile(tile)) {
                                boat.heal(tile);
                            }
                        }
                    }
                    break;
                case 3:
                    ArrayList<Tile> surrounding_tiles = GameManager.getPlayer("1").get_grid().get_surrounding_tiles(hit_tiles.get(0));
                    for (Tile target_tile : surrounding_tiles) {
                        for (Boat boat : GameManager.getPlayer(opponent_id).getBoatConfig().boats) {
                            for (Tile boat_tile : boat.getTiles()) {
                                if (boat_tile == target_tile) {
                                    target_tile.exposed();
                                }
                            }
                        }
                    }
                    break;
                default:
                    for (Tile tile : hit_tiles) {
                        tile.hit();
                        for (Boat boat : GameManager.getPlayer("1").getBoatConfig().boats) {
                            if (boat.containsTile(tile)) {
                                boat.hit(tile);
                            }
                        }
                    }

            }
        }
    }

    @Override
    public void handleInput() {
        frame_time = System.currentTimeMillis();
        if (waiting && !GameManager.single_player) {
            long db_update2 = System.currentTimeMillis();
            if ((db_update2 - db_update) % 1000 < 10) { // Only check db every second
                db_update = db_update2;
                if (GameManager.checkTurn()) {
                    opponentTurn(GameManager.getLastTurn());
                    GameManager.getPlayer("1").checkDefeat();
                    if (GameManager.getPlayer("1").allShipsSunk()) {
                        GameManager.loss();
                        GameManager.setState(State.gameEnd);
                    }
                    turn_time = 60000 * GameManager.turntime + 999;
                    frame_time = System.currentTimeMillis();
                    frame_time_last = frame_time;
                    waiting = false;
                }
            }

        } else {
            turn_time = turn_time - (frame_time - frame_time_last);
            frame_time_last = frame_time;
            HashMap<Object, Object> turn_info = new HashMap<>();

            while (GameManager.single_player && GameManager.getActive_player().is_skipable_player()) {
                GameManager.getPlayer("1").hit_random_tile();
                GameManager.getPlayer("1").checkDefeat();
                if (GameManager.getPlayer("1").allShipsSunk()) {
                    GameManager.loss();
                    GameManager.setState(State.gameEnd);
                }
                GameManager.endTurn();
                turn_time = 60000 * GameManager.turntime + 999;

            }

            for (MissileButton missileButton : missile_buttons) {
                if (missileButton.handleInput()) {
                    selected_missile_button = missileButton;
                    if (!selected_missile_button.can_fire()) {
                        selected_missile_button = null;
                    }
                }
            }

            if (turn_time < 0) {
                ArrayList<Tile> random_tiles = new ArrayList<>();
                random_tiles.add(GameManager.getPlayer(opponent_id).hit_random_tile());
                turn_info.put("missile",1);
                turn_info.put("tiles",random_tiles);
                System.out.println(random_tiles);
                GameManager.endTurn(turn_info);
                if (!GameManager.single_player) {
                    waiting = true;
                }
            } else  if (Gdx.input.isTouched()) {
                if (selected_missile_button != null) {
                    turn_info.put("missile", selected_missile_button.id);
                }
                // player should not be able to select target tile on triple *random*
                if(selected_missile_button != null && selected_missile_button.getMissileType() != MissileType.TripleRandom) {

                    Vector3 input_vector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    Vector3 projected_vector = AssetManager.unprojectInput(input_vector);
                    Rectangle touch_rectangle = new Rectangle(projected_vector.x - 2, projected_vector.y - 2, 2, 2);
                    Grid grid = selected_missile_button.getMissileType() == MissileType.Healing ?
                            GameManager.getPlayer("1").get_grid()
                            :
                            GameManager.getPlayer(opponent_id).get_grid();

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

                ArrayList<Tile> turn_tiles = new ArrayList<>();
                if (selected_tile != null) {
                    turn_tiles.add(selected_tile);
                }

                for (Tile[] list : GameManager.getPlayer(opponent_id).get_grid().get_tiles()) {
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
                        System.out.println("Fired " + selected_missile_button.getMissileType().name() + " at tile " + selected_tile.getIndex()[0] + " : " + selected_tile.getIndex()[1]);
                        if (selected_missile_button.getMissileType() == MissileType.Normal) {
                            end_turn = true;
                            for (Boat boat: GameManager.getPlayer(opponent_id).getBoatConfig().boats) {
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
                            ArrayList<Tile> surrounding_tiles = GameManager.getPlayer(opponent_id).get_grid().get_surrounding_tiles(selected_tile);

                            for (Tile target_tile : surrounding_tiles) {
                                for (Boat boat: GameManager.getPlayer(opponent_id).getBoatConfig().boats) {
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
                        Grid grid = GameManager.getPlayer(opponent_id).get_grid();
                        for (int i = 0; i < 3; i++) {
                            random_tiles[i] = grid.get_random_tile();
                            // Hit here to avoid calling the hit function many times
                            // on each random tile in the later loop.
                            random_tiles[i].hit();
                        }
                        turn_tiles.addAll(Arrays.asList(random_tiles));
                        // Check for a hit on any of the random tiles.
                        for (Boat boat: GameManager.getPlayer(opponent_id).getBoatConfig().boats) {
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
                    turn_info.put("tiles", turn_tiles);
                    if (end_turn) {
                        selected_missile_button.use_ammunition();
                        selected_missile_button = null;
                        GameManager.endTurn(turn_info);
                        if (!GameManager.single_player) {
                            waiting = true;
                        }
                    }
                }
            }
            GameManager.getPlayer(opponent_id).checkDefeat();
            if (GameManager.getPlayer((opponent_id)).allShipsSunk()) {
                GameManager.win();
                GameManager.setState(State.gameEnd);
            }
        }
    }
}

