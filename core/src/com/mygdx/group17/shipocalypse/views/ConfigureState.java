package com.mygdx.group17.shipocalypse.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.group17.shipocalypse.Shipocalypse;
import com.mygdx.group17.shipocalypse.models.*;
import com.mygdx.group17.shipocalypse.ui.BoatButton;

import org.w3c.dom.Text;

public class ConfigureState extends GameState {

    private Player _player;
    public BoatButton _btnBoat1;
    public BoatButton _btnBoat2;
    public BoatButton _btnBoat3;
    public BoatButton _btnBoat4;
    public int CurrentBoatSize;
    public Texture hoverBoat;
    public long timeOfLastTouch;
    public boolean touching;
    public BoatConfiguration boatConfiguration;
    public Vector3 mouseDownPos;

    public ConfigureState() {
        _player = new Player(8, 8);

        boatConfiguration = new BoatConfiguration();
    }

    @Override
    public void render(SpriteBatch batch) {
        ShapeRenderer shaperenderer = new ShapeRenderer();
        for (Tile[] list : _player.get_grid().get_tiles()) {
            for (Tile tile : list) {
                shaperenderer.begin(ShapeRenderer.ShapeType.Line);

                if (hoverBoat != null && tile.get_rectangle().overlaps(new Rectangle(mouseDownPos.x ,mouseDownPos.y,2,2))) {
                    shaperenderer.setColor(Color.RED);
                } else{
                    shaperenderer.setColor(Color.DARK_GRAY);
                }

                shaperenderer.rect(tile._posx, tile._posy, Tile.TILE_SIZE, Tile.TILE_SIZE);
                shaperenderer.end();
            }
        }

        batch.begin();

        _btnBoat1 = new BoatButton(shaperenderer, 50, Shipocalypse.GAME_HEIGHT - 200, new Texture("ship1.png"), 1);
        _btnBoat2 = new BoatButton(shaperenderer, 50, Shipocalypse.GAME_HEIGHT - 300, new Texture("ship2.png"), 2);
        _btnBoat3 = new BoatButton(shaperenderer, 50, Shipocalypse.GAME_HEIGHT - 400, new Texture("ship3.png"), 3);
        _btnBoat4 = new BoatButton(shaperenderer, 50, Shipocalypse.GAME_HEIGHT - 500, new Texture("ship4.png"), 4);
        _btnBoat1.render(batch);
        _btnBoat2.render(batch);
        _btnBoat3.render(batch);
        _btnBoat4.render(batch);

        if (hoverBoat != null) {
            batch.draw(hoverBoat, mouseDownPos.x  - hoverBoat.getWidth() / 2, mouseDownPos.y  - hoverBoat.getHeight() / 2);
        }

        for (Boat boat : boatConfiguration.boats) {
            boat.render(batch);
        }

        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched() ) {

            int input_x = Gdx.input.getX();
            int input_y = Shipocalypse.GAME_HEIGHT - Gdx.input.getY();

            Rectangle touch_rectangle = new Rectangle(input_x - 2, input_y - 2, 4, 4);



            // Just update the hoverboat position
            if (hoverBoat != null) {
                mouseDownPos = new Vector3(input_x, input_y, 0);
            }
            // This creates a hoverboat
            // for every button
            //      if button is pressed
            //          update the hoverboat
            if (touch_rectangle.overlaps(_btnBoat1.get_rectangle())) {
                hoverBoat = _btnBoat1.get_texture();
                mouseDownPos = new Vector3(input_x, input_y, 0);
                CurrentBoatSize = 1;
            }
            if (touch_rectangle.overlaps(_btnBoat2.get_rectangle())) {
                hoverBoat = _btnBoat2.get_texture();
                mouseDownPos = new Vector3(input_x, input_y, 0);
                CurrentBoatSize = 2;
            }
            if (touch_rectangle.overlaps(_btnBoat3.get_rectangle())) {
                hoverBoat = _btnBoat3.get_texture();
                mouseDownPos = new Vector3(input_x, input_y, 0);
                CurrentBoatSize = 3;
            }
            if (touch_rectangle.overlaps(_btnBoat4.get_rectangle())) {
                hoverBoat = _btnBoat4.get_texture();
                mouseDownPos = new Vector3(input_x, input_y, 0);
                CurrentBoatSize = 4;
            }

            // Double click deletes boat
            if (System.currentTimeMillis() - timeOfLastTouch > 50 && System.currentTimeMillis() - timeOfLastTouch < 200) {
                for (Boat boat : boatConfiguration.boats) {
                    if (touch_rectangle.overlaps(boat.get_rectangle())) {
                        touching = true;
                        boatConfiguration.RemoveBoat(boat);
                        return;
                    }
                }
            } else {
                for (Boat boat : boatConfiguration.boats) {
                    if (touch_rectangle.overlaps(boat.get_rectangle()) && touching == false) {
                        touching = true;
                        boatConfiguration.RotateBoat(boat);
                        return;
                    }
                }
            }

            timeOfLastTouch = System.currentTimeMillis();
        } else {
            touching = false;
            if (hoverBoat != null) {
                for (Tile[] list : _player.get_grid().get_tiles()) {
                    for (Tile tile : list) {
                        if (tile.get_rectangle().overlaps(new Rectangle(mouseDownPos.x, mouseDownPos.y, 2, 2))) {
                            boatConfiguration.AddBoat(tile._posx, tile._posy, CurrentBoatSize);
                        }
                    }
                }

                hoverBoat = null;
            }
        }
    }
}
