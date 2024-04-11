package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.Action;

public class AccountButton extends Button{

    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 75;

    private String mail;
    private String password;

    public void updateCredentials(String mail, String password)
    {
        this.mail = mail;
        this.password = password;
    }

    public AccountButton(ShapeRenderer shape, int x, int y, String tekst, Action action) {
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
                    case sign_in:
                        signIn();
                        break;
                    case log_in:
                        logIn();
                        break;
                    case skip:
                        System.exit(0);
                        break;
                    default:
                        throw new RuntimeException("Kunne ikke finne action-type");
                }
            }
        }
    }

    private void logIn() {
        System.out.println("Hosting game...");
    }

    public void signIn() {
        System.out.println("Joining game...");
    }

    @Override
    public void dispose() {

    }
}