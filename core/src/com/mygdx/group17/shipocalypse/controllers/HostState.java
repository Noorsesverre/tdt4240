package com.mygdx.group17.shipocalypse.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.BoatConfiguration;
import com.mygdx.group17.shipocalypse.models.Player;
import com.mygdx.group17.shipocalypse.models.State;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.ui.MenuButton;
import com.mygdx.group17.shipocalypse.models.Options;
import com.mygdx.group17.shipocalypse.singletons.GameManager;

public class HostState extends GameState {
    private HashMap<String, Object> selected_options = new HashMap<String, Object>();
    private MenuButton start_button;

    private MenuButton menu_button;

    private static HashMap<String, List<MenuButton>> options = new HashMap<String, List<MenuButton>>();

    public HostState() {
        ShapeRenderer shapeRenderer = AssetManager.getInstance().shape;
        float buttonGameCenter = Options.GAME_WIDTH / 2 - MenuButton.BUTTON_WIDTH / 2;
        int y = 550;

        // Set up all option buttons
        for (String type : Options.options.keySet()) {
            // Find positioning based on number of options
            int num_elements = Options.options.get(type).size();
            int i = -num_elements / 2;
            // Create the list of options for this type
            options.put(type, new ArrayList<>());
            // Iterate through options for this type and create MenuButton
            for (String opt : Options.options.get(type).keySet()) {
                options.get(type).add(new MenuButton(shapeRenderer, (int) buttonGameCenter + i * (MenuButton.BUTTON_WIDTH + 10), y, opt, Options.options.get(type).get(opt)));
                ++i;
            }
            y = y - 100;
        }

        // Select defaults
        for (String s : options.keySet()) {
            for (MenuButton b : options.get(s)) {
                LinkedHashMap<String, LinkedHashMap> default_options = Options.defaults;
                if (default_options.get(s).keySet().contains(b.get_text())) {
                    select(s, b);
                }
            }
        }
        this.start_button = new MenuButton(shapeRenderer, (int)buttonGameCenter + MenuButton.BUTTON_WIDTH/2, y, "create game >", Action.createGame);
        this.menu_button = new MenuButton(shapeRenderer, (int)buttonGameCenter - MenuButton.BUTTON_WIDTH/2 - 10, y, "< main menu", Action.mainMenu);
    }

    public void select(String s, MenuButton b) {
        b.select();
        selected_options.put(s, b.getValue());
    }

    @Override
    public void render() {
        AssetManager.draw(AssetManager.title, Options.GAME_WIDTH / 2 - AssetManager.title.getWidth() / 2, 200);

        //render all option buttons
        for (List<MenuButton> l : options.values()) {
            for (MenuButton b : l) {
                b.render(b.getColor());
            }
        }
        start_button.render();
        menu_button.render();
    }

    @Override
    public void dispose() {
        start_button.dispose();
    }

    @Override
    public void handleInput() {
        //handle input for all option buttons
        for (String s : options.keySet()) {
            for (MenuButton b : options.get(s)) {
                if (b.handleInput()) {
                    for (MenuButton b2 : options.get(s)) {
                        if (b2 != b) {
                            b2.deselect();
                        } else {
                            select(s, b2);
                        }
                    }
                    break;
                }
            }
        }
        if (start_button.handleInput()) {
            System.out.println(selected_options);
            GameManager.createGame(selected_options);
        }
        if (menu_button.handleInput()) {
            GameManager.setState(State.menu);
        }
    }
}

