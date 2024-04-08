package com.mygdx.group17.shipocalypse.singletons;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import com.mygdx.group17.shipocalypse.models.*;
import com.mygdx.group17.shipocalypse.controllers.*;
import com.mygdx.group17.shipocalypse.Shipocalypse;

public class GameManager {
    private static GameManager single_instance = null;
    private static GameState playState;
    private static GameConfig configuration;
    private static Player player;
    private static Player opponent;
    private static boolean touching = false; // Used to avoid the same touch triggering twice.

    private GameManager(Shipocalypse _shipocalypse) {
        AssetManager.getInstance();
    }

    public void setPlayer(Player _player) {
        player = _player;
    }
    public void setOpponent(Player _opponent) { opponent = _opponent; }

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
            default:
                throw new RuntimeException("Couldn't find action-type");
        }
    }

    public static void createGame(int gridX, int gridY, Map<Integer, Integer> boats) {
        GameConfig config = new GameConfig(gridX, gridY, boats);
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

    public static void setConfig(GameConfig config, Player pl, Player opp) {
        player = pl;
        opponent = opp;
        configuration = config;
    }

    public static Player getOpponent() { return opponent; }

    public static Player getPlayer() { return player; }
    public static GameConfig getConfig() { return configuration; }


}
