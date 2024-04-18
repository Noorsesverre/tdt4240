package com.mygdx.group17.shipocalypse.singletons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import com.mygdx.group17.shipocalypse.FirebaseInterface;
import com.mygdx.group17.shipocalypse.models.*;
import com.mygdx.group17.shipocalypse.controllers.*;
import com.mygdx.group17.shipocalypse.Shipocalypse;

public class GameManager {
    private static GameManager single_instance = null;
    private static GameState playState;
    private static GameConfig configuration;
    private static Player player;
    private static Player opponent;
    private static Preferences saved; // used for storing an ID on the device
    private static Game game_to_configure;
    private static String user_id; // fetched from firebase
    private static ArrayList<String> game_id; // fetched from firebase
    private static boolean touching = false; // Used to avoid the same touch triggering twice.

    private static FirebaseInterface firebase;

    private GameManager(Shipocalypse _shipocalypse) {
        AssetManager.getInstance();
        saved = Gdx.app.getPreferences("saved");
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
            case gameEnd:
                playState = new GameEndState(AssetManager.title);
                break;
            default:
                throw new RuntimeException("Couldn't find action-type");
        }
    }

    public static void createGame(int gridX, int gridY, Map<Integer, Integer> boats) {
        GameConfig config = new GameConfig(gridX, gridY, boats);
        String game_id = UUID.randomUUID().toString().substring(0,8); // pseudo-random gameID
        game_to_configure = new Game(user_id, game_id);
        firebase.hostGame(game_to_configure);
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
            firebase.writeToDatabase("kaitest", new HashMap<String, Object>());
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
        game_to_configure.addPlayer(player, 0);
        firebase.updateGame(game_to_configure);
        opponent = opp;
        configuration = config;
    }

    public static Player getOpponent() { return opponent; }

    public static Player getPlayer() { return player; }
    public static GameConfig getConfig() { return configuration; }

    public static void setFirebase(FirebaseInterface firebase_interface) {
        firebase = firebase_interface;
        String uniqueID;
        if (!saved.contains("ID")) {
            uniqueID = UUID.randomUUID().toString(); // Generate a random ID
            saved.putString("ID", uniqueID); // Save it for later
            saved.flush();
        } else {
            uniqueID = saved.getString("ID");
        }
        firebase.addUser(uniqueID); // Will only add user if user does not already exist in firebase
        user_id = uniqueID;
    }


}
