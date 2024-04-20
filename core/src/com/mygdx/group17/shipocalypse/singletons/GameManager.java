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

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GameManager {

    public static boolean single_player = false;

    private static String current_game_id;
    private static Preferences saved; // used for storing an ID on the device
    private static String user_id; // fetched from firebase
    private static FirebaseInterface firebase;
    private static GameManager single_instance = null;
    private static GameState playState;
    private static GameConfig configuration;
    private static int setupMode;
    private static ArrayList<Player> players;
    private static Player active_player; // The players that is has their turn
    private static boolean touching = false; // Used to avoid the same touch triggering twice.
    private GameManager(Shipocalypse _shipocalypse) {
        AssetManager.getInstance();
        this.players = new ArrayList<Player>() {};
        saved = Gdx.app.getPreferences("saved");
        setupMode = 1; // 1 is hosting
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
    public static void playerReady() {
        firebase.playerReady(user_id, current_game_id);
        System.out.println(getPlayer("1").getBoatConfig().debug());
        firebase.postGridInfo(current_game_id, user_id, encodeInfo(getPlayer("1").getBoatConfig()));
    }

    public static boolean bothPlayersReady() {
        return firebase.bothPlayersReady(current_game_id);

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
    public static void createGame(HashMap<String, Object> options) {
        int grids = (int)options.get("grids");
        Map<Integer, Integer> boats = (Map)options.get("boats");
        setupMode = 1;

        GameConfig config = new GameConfig(grids, grids, boats);
        Player player = new Player(config.getGrid_x(), config.getGrid_y(), new BoatConfiguration(), "1");
        GameManager.addPlayer(player);

        current_game_id = firebase.createGame(options); // Create a joinable game in firebase, return a pseudo-unique ID.

        // TODO: Implement opponent logic instead of skipable character
        Player opponent = new Player(config.getGrid_x(), config.getGrid_y(), new BoatConfiguration(), "2", true);
        addPlayer(opponent);

        active_player = getPlayer("1");
        playState = new ConfigureState(config);
    }
    public static int getSetupMode() { return setupMode; }

    public static void joinGame(String game_id, HashMap<String, Object> options) {
        int grids = (int)options.get("grids");
        Map<Integer, Integer> boats = (Map)options.get("boats");

        firebase.joinGame(game_id, user_id);

        setupMode = 2; // 2 is joining

        GameConfig config = new GameConfig(grids, grids, boats);
        Player player = new Player(config.getGrid_x(), config.getGrid_y(), new BoatConfiguration(), "1");
        GameManager.addPlayer(player);
        current_game_id = game_id;

        // TODO: Implement opponent logic instead of skipable character
        Player opponent = new Player(config.getGrid_x(), config.getGrid_y(), new BoatConfiguration(), "2", true);
        addPlayer(opponent);

        active_player = getPlayer("1");
        playState = new ConfigureState(config);

    }

    public static boolean checkTurn() {
        return firebase.checkTurn(current_game_id, user_id);
    }


    public static void handleInput() { playState.handleInput(); }

    public GameState getState() { return playState; }
    public static void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        AssetManager.drawBackground();

        // Print debug info to screen
        AssetManager.write("Mouse location: {" + (Gdx.input.getX()) + ", " + (Options.GAME_HEIGHT - Gdx.input.getY()) + "}", 15,20);
        AssetManager.write("GAME ID: " + current_game_id, 15,40);

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
        Player opponent = opponentFromEncodedInfo(firebase.getOpponentInfo(current_game_id), config.getGrid_x());
        addPlayer(opponent);
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

    public static void endTurn(HashMap<Object, Object> turn_info) {
        firebase.postTurnInfo(current_game_id, user_id, turn_info);
        // Find the non-playing player
        Player non_playing_player = null;
        for (Player player : players) {
            if (active_player.getPlayer_id() != player.getPlayer_id()) {
                non_playing_player = player;
            }
        }

        active_player = non_playing_player;
    }

    public static HashMap<String, Object> getLastTurn() {
        return firebase.getTurnInfo(current_game_id);
    }

    public static void setFirebase(FirebaseInterface _firebase) {
        firebase = _firebase;
        String uniqueID;
        if (!saved.contains("ID")) {
            uniqueID = UUID.randomUUID().toString(); // Generate a random unique ID
            saved.putString("ID", uniqueID); // Save it for later
            saved.flush();
        } else {
            uniqueID = saved.getString("ID");
        }
        firebase.addUser(uniqueID); // Will only add user if user does not already exist in firebase
        user_id = uniqueID;
    }

    public static String getUserId() { return user_id; }

    public static String getCurrentGame() { return current_game_id; }

    public static String encodeInfo(BoatConfiguration boat_config) {
        String info = "";
        for (Boat boat : boat_config.boats) {
            info = info + String.valueOf(boat.getSize()) + ":";
            for (Tile tile : boat.getTiles()) {
                info = info + String.valueOf(tile._index_x) + "@" + String.valueOf(tile._index_y) + ",";
            }
            info = info + "&";
        }
        info = info.substring(0, info.length() - 1);
        return info;
    }

    public static HashMap<String, HashMap<String, Object>> getOpenGames() {
        return firebase.getOpenGames();
    }

    public static Player opponentFromEncodedInfo(String info, int grid_size) {
        Player new_player = new Player(grid_size, grid_size);
        Tile[][] tiles = new_player.get_grid().get_tiles();
        String[] boats = info.split("&");
        int posx = 0;
        int posy = 0;
        BoatConfiguration new_boatconfig = new BoatConfiguration();
        for (String boat : boats) {
            boolean first_tile = true;
            int size = Integer.valueOf(boat.split(":")[0]);
            String[] boat_tiles = boat.split(":")[1].split(",");
            ArrayList<Tile> new_tiles = new ArrayList<Tile>();
            for (String boat_tile : boat_tiles) {
                int index_x = Integer.parseInt(boat_tile.split("@")[0]);
                int index_y = Integer.parseInt(boat_tile.split("@")[1]);
                for (Tile[] tileset : tiles) {
                    for (Tile tile : tileset) {
                        if (index_x == tile._index_x && index_y == tile._index_y) {
                            new_tiles.add(tile);
                            if (first_tile) {
                                posx = tile._posx;
                                posy = tile._posy;
                                first_tile = false;
                            }
                        }
                    }
                }
            }
            Boat new_boat = new Boat(posx, posy, size);
            new_boatconfig.AddBoat(new_boat, new_tiles);
        }
        new_player.setBoatConfig(new_boatconfig);
        new_player.setId("0");
        return new_player;
    }

}
