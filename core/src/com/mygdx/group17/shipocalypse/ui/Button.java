package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;
import com.mygdx.group17.shipocalypse.singletons.GameManager;

public abstract class Button {
    public static final int BUTTON_WIDTH = 150;

    private final Vector3 _position;
    private int width;
    private final int height;
    private final ShapeRenderer _shaperenderer;
    private String _text;

    private int manual_text_offset_x = 0;

    private int manual_text_offset_y = 0;

    Color color = Color.RED;

    public Button(ShapeRenderer shape, int x, int y, String text, int width) {
        this(shape, x, y, text);
        this.width = width;

    }
    public Button(ShapeRenderer shape, int x, int y) {
        width = BUTTON_WIDTH;
        height = 75;
        _position = new Vector3(x, y, 0);
        _shaperenderer = shape;
        this.color = Color.RED;
    }

    public Button(ShapeRenderer shape, int x, int y, String text) {
        width = BUTTON_WIDTH;
        height = 75;
        _position = new Vector3(x, y, 0);
        _shaperenderer = shape;
        _text = text;
    }

    public void render() {
        _shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
        _shaperenderer.setColor(color);
        _shaperenderer.rect(_position.x, _position.y, width, height);
        _shaperenderer.end();

        Vector3 text_pos = FindCenterPosition();

        AssetManager.write(_text, (int)_position.x + (int)text_pos.x + manual_text_offset_x, (int)_position.y + (int)text_pos.y + manual_text_offset_y);
    }
    public void render(Color c) {
        _shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
        _shaperenderer.setColor(c);
        _shaperenderer.rect(_position.x, _position.y, width, height);
        _shaperenderer.end();

        Vector3 text_pos = FindCenterPosition();

        AssetManager.write(_text, (int)_position.x + (int)text_pos.x, (int)_position.y + (int)text_pos.y);
    }
    private Vector3 FindCenterPosition() {
        int lines = _text.length() - _text.replace("\n", "").length() + 1;
        int text_length = _text.length() * 8 / lines;

        int height_pos = height / 2 + 8 * lines;

        int width_pos = (width / 2 ) - (text_length / 2);

        return new Vector3(width_pos, height_pos, 0);
    }

    public Rectangle get_rectangle() {
        return new Rectangle(_position.x, _position.y, width, height);
    }


    public String get_text () {
        return _text;
    }

    public void set_text(String text) {
        _text = text;
    }

    public void set_manual_text_offset(int x, int y) {
        manual_text_offset_x = x;
        manual_text_offset_y = y;
    }

    public Vector3 get_position() {
        return _position;
    }

    public boolean handleInput() {
        if (Gdx.input.isTouched() && !GameManager.isTouching()) {

            // Use viewport/AssetManager to unproject input coordinates to game world coordinates.
            Vector3 input_vector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 projected_vector = AssetManager.unprojectInput(input_vector);
            Rectangle touch_rectangle = new Rectangle(projected_vector.x - 2, projected_vector.y - 2, 4, 4);

            return touch_rectangle.overlaps(this.get_rectangle());
        }
        return false;
    }

    public abstract void dispose();

}
