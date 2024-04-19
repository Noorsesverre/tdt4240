package com.mygdx.group17.shipocalypse.singletons;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import com.mygdx.group17.shipocalypse.models.*;
import com.mygdx.group17.shipocalypse.controllers.*;
import com.mygdx.group17.shipocalypse.Shipocalypse;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GameManager {
    private static GameManager single_instance = null;
    private static GameState playState;
    private static GameConfig configuration;
    private static ArrayList<Player> players;
    private static Player active_player; // The players that is has their turn
    private static boolean touching = false; // Used to avoid the same touch triggering twice.
    private GameManager(Shipocalypse _shipocalypse) {
        AssetManager.getInstance();
        this.players = new ArrayList<Player>() {};
    }
    public static void addPlayer(Player new_player) { players.add(new_player); }
    public static Player getActive_player() { return active_player; };
    public static void init(Shipocalypse _shipocalypse) {
        if (single_instance != null) {
            throw new RuntimeException("GameManager already initialized");
        }
        single_instance = new GameManager(_shipocalypse);
    }

    public static synchronized GameManager getInstance() {
        if (single_instance == null) {
            throw new RuntimeException("GameManager not initialized");
        }
        return single_instance;
    }

    public static void setState(State state) {
        switch (state) {
            case menu:
                playState = new MenuState();
                break;
            case host:
                System.out.println("Hosting game...");
                playState = new HostState();
                break;
            case join:
                System.out.println("Joining game...");
                playState = new JoinState();
                break;
            case play:
                playState = new PlayState();
                break;
            case gameEnd:
                playState = new GameEndState(AssetManager.title);
                break;
            default:
                throw new RuntimeException("Couldn't find action-type");
        }
    }

    public static void createGame(int gridX, int gridY, Map<Integer, Integer> boats) {
        GameConfig config = new GameConfig(gridX, gridY, boats);
        Player player = new Player(config.getGrid_x(), config.getGrid_y(), new BoatConfiguration(), "1");
        GameManager.addPlayer(player);

        // TODO: Implement opponent logic instead of skipable character
        Player opponent = new Player(config.getGrid_x(), config.getGrid_y(), new BoatConfiguration(), "2", true);
        GameManager.addPlayer(opponent);

        active_player = getPlayer("1");
        playState = new ConfigureState(config);
    }

    public static void handleInput() { playState.handleInput(); }

    public GameState getState() { return playState; }
    public static void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        AssetManager.drawBackground();

        // Print debug info to screen
        AssetManager.write("Mouse location: {" + (Gdx.input.getX()) + ", " + (Options.GAME_HEIGHT - Gdx.input.getY()) + "}", 15,20);
        AssetManager.write("Touching: " + Boolean.toString(touching), 15,40);

        // Ensures that a continuous touch will not be registered multiple times.
        if (Gdx.input.isTouched()) {
            touching = true;
        }
        else {
            touching = false;
        }

        if (Gdx.input.isTouched()) {
            Vector3 input_vector = AssetManager.unprojectInput(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            debugCursor(input_vector.x, input_vector.y, Color.GREEN);
        }

        playState.render();
    }

    public static void debugCursor(float x, float y, Color color) {
        AssetManager.shape.begin(ShapeRenderer.ShapeType.Filled);
        AssetManager.shape.setColor(color);
        AssetManager.shape.rect(x-5, y-5, 10, 10);
        AssetManager.shape.end();
    }

    public static boolean isTouching() {
        // Used throughout application to check for new touch.
        return touching;
    }

    public static void setConfig(GameConfig config) {
        configuration = config;
    }

    public static Player getPlayer(String id) {
        for (Player player : players ){
            if (player.getPlayer_id().equals(id)) {
                return player;
            }
        }
        throw new RuntimeException("Kunne ikke finne en spiller med id " + id + " i listen " + players);
    }
    public static GameConfig getConfig() { return configuration; }

    public static void endTurn() {
        // Find the non-playing player
        Player non_playing_player = null;
        for (Player player : players) {
            if (active_player.getPlayer_id() != player.getPlayer_id()) {
                non_playing_player = player;
            }
        }

        active_player = non_playing_player;
    }



}
