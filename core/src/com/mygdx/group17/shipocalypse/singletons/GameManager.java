package com.mygdx.group17.shipocalypse.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.Gameconfig;
import com.mygdx.group17.shipocalypse.models.State;
import com.mygdx.group17.shipocalypse.controllers.GameState;
import com.mygdx.group17.shipocalypse.controllers.ConfigureState;
import com.mygdx.group17.shipocalypse.controllers.MenuState;
import com.mygdx.group17.shipocalypse.controllers.HostState;
import com.mygdx.group17.shipocalypse.controllers.JoinState;
import com.mygdx.group17.shipocalypse.controllers.PlayState;

import java.util.Map;


public class GameManager {
    private static GameManager single_instance = null;
    private static Shipocalypse shipocalypse;
    private static GameState playState;

    private GameManager(Shipocalypse _shipocalypse) {
        shipocalypse = _shipocalypse;
        AssetManager.getInstance();
    }

    public static void init(Shipocalypse _shipocalypse) {
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
                throw new RuntimeException("Kunne ikke finne action-type");
        }
    }

    public static void createGame(int gridX, int gridY, Map<Integer, Integer> boats) {
        Gameconfig config = new Gameconfig(gridX, gridY, boats);
        playState = new ConfigureState(config);
    }
    public static void handleInput() {
        playState.handleInput();
    }
    public static void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        AssetManager.batch.begin();
        AssetManager.batch.draw(AssetManager.sea, 0, 0);
        AssetManager.bf.draw(AssetManager.batch, "Mouse location: {" + (Gdx.input.getX()) + ", " + (Shipocalypse.GAME_HEIGHT - Gdx.input.getY()) + "}", 15,20);
        AssetManager.batch.end();

        playState.handleInput();

        playState.render();
    }

}
