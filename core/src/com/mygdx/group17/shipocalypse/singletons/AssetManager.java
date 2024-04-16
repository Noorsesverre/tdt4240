package com.mygdx.group17.shipocalypse.singletons;

import java.util.List;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.group17.shipocalypse.models.Options;

public class AssetManager {
    public static FitViewport viewport;
    public static SpriteBatch batch;
    public static ArrayList<Texture> ship_textures;
    public static ArrayList<Sprite> ship_sprites;
    public static Texture title;
    public static BitmapFont bf;
    public static ShapeRenderer shape;
    public static Texture fire_texture;
    public static Sprite fire_sprite;
    public static Texture cross_texture;
    public static Sprite cross_sprite;
    public static Texture crosshair_texture;
    public static Sprite crosshair_sprite;
    public static Texture exclamation_point_texture;
    public static Sprite exclamation_point_sprite;
    public static Skin skin;
    public static TextureAtlas atlas;
    private static AssetManager single_instance = null;

    private AssetManager() {
        fire_texture = new Texture("fire.png");
        fire_sprite = new Sprite(fire_texture);
        cross_texture = new Texture("cross.png");
        cross_sprite = new Sprite(cross_texture);
        crosshair_texture = new Texture("crosshair.png");
        crosshair_sprite = new Sprite(crosshair_texture);
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        title = new Texture("title.png");
        bf = new BitmapFont();
        atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("skin.json"), atlas);
        ship_textures = new ArrayList(List.of(new Texture("ship1_small.png"), new Texture("ship2_small.png"), new Texture("ship3_small.png"), new Texture("ship4_small.png")));
        ship_sprites = new ArrayList();
        exclamation_point_texture = new Texture("exclamation_point.png");
        exclamation_point_sprite = new Sprite(exclamation_point_texture);
        for (Texture ship : ship_textures) {
            ship_sprites.add(new Sprite(ship));
        }
    }


    public static synchronized AssetManager getInstance() {
        if (single_instance == null) {
            single_instance = new AssetManager();
        }
        return single_instance;
    }

    public static void drawBackground() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.rect(0,0, Options.GAME_WIDTH, Options.GAME_HEIGHT, Options.BG_COLOR[0], Options.BG_COLOR[0], Options.BG_COLOR[1], Options.BG_COLOR[1]);
        shape.end();
    }

    public static void draw(Texture drawable, int x, int y) {
        batch.begin();
        batch.draw(drawable, x, Options.GAME_HEIGHT - y);
        batch.end();
    }

    public static void draw(Sprite drawable) {
        batch.begin();
        drawable.draw(batch);
        batch.end();
    }
    public static void draw(Sprite drawable, int x, int y) {
        batch.begin();
        batch.draw(drawable, x, y);
        batch.end();
    }
    public static void write(String text, int x, int y) {
        batch.begin();
        bf.draw(batch, text, x, y);
        batch.end();
    }

    public static void dispose() {
        shape.dispose();
        batch.dispose();
        for (Texture ship : ship_textures) {
            ship.dispose();
        }
        bf.dispose();
    }

    public static void setViewport(FitViewport _viewport) {
        viewport = _viewport;
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shape.setProjectionMatrix(viewport.getCamera().combined);
    }


    public static Vector3 unprojectInput(Vector3 vector) {
        // Method for projecting input to correct screen coordinates for the viewport.
        viewport.getCamera().unproject(vector, AssetManager.viewport.getScreenX(), AssetManager.viewport.getScreenY(), AssetManager.viewport.getScreenWidth(), AssetManager.viewport.getScreenHeight());
        return new Vector3(vector.x, vector.y, 0);

    }

    public static void text(String text, float pos_x, float pos_y) {
        BitmapFont bf = new BitmapFont();
        batch.begin();
        bf.draw(batch, text, pos_x, pos_y);
        batch.end();
    }
}
