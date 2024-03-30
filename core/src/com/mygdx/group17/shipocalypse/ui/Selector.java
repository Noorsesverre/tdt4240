package com.mygdx.group17.shipocalypse.ui;


import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;

import com.mygdx.group17.shipocalypse.singletons.AssetManager;

public class Selector extends SelectBox {

    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 75;

    public Selector() {
        super(new SelectBoxStyle(AssetManager.bf, Color.BLACK, new SpriteDrawable(AssetManager.sea_sprite), new ScrollPaneStyle(), new ListStyle(AssetManager.bf, Color.BLACK, Color.BLACK, new SpriteDrawable())));
    }

    public void handleInput() {

    }

    public void dispose() {

    }
}
