package com.mygdx.group17.shipocalypse.ui;

import static java.lang.System.exit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.Action;

public class MenuButton extends Button{

    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 75;

    public MenuButton(ShapeRenderer shape, int x, int y, String tekst, Action action) {
        super(shape, x, y, tekst, action);
    }

    @Override
    public void handleInput() {

        if (Gdx.input.isTouched()) {
            int input_x = Gdx.input.getX();
            int input_y = Shipocalypse.GAME_HEIGHT - Gdx.input.getY();

            Rectangle touch_rectangle = new Rectangle(input_x - 2, input_y - 2, 4, 4);

            if (touch_rectangle.overlaps(this.get_rectangle())) {

                switch (this.get_action()) {
                    case joinGame:
                        join_game();
                        break;
                    case hostGame:
                        host_game();
                        break;
                    case exit:
                        System.exit(0);
                        break;
                    default:
                        throw new RuntimeException("Kunne ikke finne action-type");
                }
            }
        }
    }

    private void host_game() {
        System.out.println("Hosting game...");
    }

    public void join_game() {
        System.out.println("Joining game...");
    }

    @Override
    public void dispose() {

    }
}
