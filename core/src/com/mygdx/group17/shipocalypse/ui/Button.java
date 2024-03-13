package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.group17.shipocalypse.models.Action;

public abstract class Button {
    private Vector3 _position;
    private int width;
    private int height;
    private ShapeRenderer _shaperenderer;
    private String _text;
    private Action _action;

    public Button(ShapeRenderer shape, int x, int y) {
        width = 150;
        height = 75;
        _position = new Vector3(x, y, 0);
        _shaperenderer = shape;
    }

    public Button(ShapeRenderer shape, int x, int y, String text, Action action) {
        width = 150;
        height = 75;
        _position = new Vector3(x, y, 0);
        _shaperenderer = shape;
        _text = text;
        _action = action;
    }

    public void render(SpriteBatch batch) {
        _shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
        _shaperenderer.rect(_position.x, _position.y, width, height);
        _shaperenderer.setColor(Color.RED);
        _shaperenderer.end();

        BitmapFont bf = new BitmapFont();

        Vector3 text_pos = FindCenterPosition();

        batch.begin();
        bf.draw(batch, _text, _position.x + text_pos.x, _position.y + text_pos.y);
        batch.end();


    }

    private Vector3 FindCenterPosition() {
        int text_length = _text.length() * 8;

        int height_pos = height / 2 + 8;

        int width_pos = (width / 2 ) - (text_length / 2);

        return new Vector3(width_pos, height_pos, 0);
    }

    public Rectangle get_rectangle() {
        return new Rectangle(_position.x, _position.y, width, height);
    }

    public Action get_action () {
        return _action;
    }

    public String get_text () {
        return _text;
    }

    public Vector3 get_position() {
        return _position;
    }
    public abstract void handleInput();

    public abstract void dispose();

}
