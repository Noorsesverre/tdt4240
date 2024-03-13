package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.Boat;

public class BoatButton extends Button {

    private Texture _texture;

    private Boat hoverBoat;

    public BoatButton(ShapeRenderer shape, int x, int y, Texture texture) {
        super(shape, x, y);
        _texture = texture;
    }

    public void render(SpriteBatch batch) {

        Sprite icon = new Sprite(_texture);

        icon.setScale(0.5f, 0.5f);

        icon.setPosition(get_position().x, get_position().y);

        icon.draw(batch);

        BitmapFont bf = new BitmapFont();

        bf.draw(batch, "0", get_position().x + _texture.getWidth() / 2, get_position().y);

        if (hoverBoat != null) {
            hoverBoat.render(batch);
        }


    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched()) {

            int input_x = Gdx.input.getX();
            int input_y = Shipocalypse.GAME_HEIGHT - Gdx.input.getY();

            Rectangle touch_rectangle = new Rectangle(input_x - 2, input_y - 2, 4, 4);

            if (touch_rectangle.overlaps(this.get_rectangle())) {
                System.out.print("click");
                hoverBoat = new Boat(input_x, input_y, 1);
            }
        } else {
            hoverBoat = null;
        }
    }

    @Override
    public void dispose() {

    }
}
