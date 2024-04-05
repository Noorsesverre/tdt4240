package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.models.Options;
import com.badlogic.gdx.graphics.Color;

import com.mygdx.group17.shipocalypse.singletons.GameManager;
import com.mygdx.group17.shipocalypse.models.State;

public class MenuButton extends Button{

    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 75;
    private Object value;
    private Color color = Color.RED;

    public MenuButton(ShapeRenderer shape, int x, int y, String text, Action action) {
        super(shape, x, y, text, action);
    }

    public MenuButton(ShapeRenderer shape, int x, int y, String tekst, Action action, Object v) {
        super(shape, x, y, tekst, action);
        this.value = v;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean handleInput() {

        if (Gdx.input.isTouched() && !GameManager.isTouching()) {
            int input_x = Gdx.input.getX();
            int input_y = Options.GAME_HEIGHT - Gdx.input.getY();

            Rectangle touch_rectangle = new Rectangle(input_x - 2, input_y - 2, 4, 4);

            if (touch_rectangle.overlaps(this.get_rectangle())) {
                switch (this.get_action()) {
                    case hostGame:
                        GameManager.setState(State.host);
                        break;
                    case joinGame:
                        GameManager.setState(State.join);
                        break;
                    case exit:
                        System.exit(0);
                        break;
                    case selectOption:
                        System.out.println("selected option");
                        break;
                    case test:
                        System.out.println("This is a test action");
                        break;
                    case createGame:
                        System.out.println("Game is being created");
                        break;
                    case readyGame:
                        System.out.println("Game is being started");
                        break;
                    default:
                        throw new RuntimeException("Kunne ikke finne action-type");
                }
                return true;
            }
        }
        return false;
    }
    public void select() {
        this.color = Color.BLUE;
    }

    public void deselect() {
        this.color = Color.RED;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public void dispose() {

    }
}
