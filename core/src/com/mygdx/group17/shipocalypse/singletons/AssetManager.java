package com.mygdx.group17.shipocalypse.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetManager {
    public static SpriteBatch batch;
    public static Texture ship;
    public static Texture sea;
    public static BitmapFont bf;
    public static ShapeRenderer shape;
    public static Sprite sea_sprite;
    public static Skin skin;
    public static TextureAtlas atlas;
    private static AssetManager single_instance = null;

    private AssetManager() {
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        ship = new Texture("ship4.png");
        sea = new Texture("sea.png");
        bf = new BitmapFont();
        sea_sprite = new Sprite(sea);
        atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("skin.json"), atlas);
    }

    public static synchronized AssetManager getInstance() {
        if (single_instance == null) {
            single_instance = new AssetManager();
        }
        return single_instance;
    }

    public static void dispose() {
        shape.dispose();
        batch.dispose();
        ship.dispose();
        sea.dispose();
        bf.dispose();
    }

}
