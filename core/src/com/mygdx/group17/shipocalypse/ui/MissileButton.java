package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;

public class MissileButton extends Button {

    private MissileType MissileType;
    public int id;

    private int ammunition;

    public MissileButton(ShapeRenderer shape, int x, int y, int width, MissileType MissileType) {
        super(shape, x, y, MissileType.name(), width);
        this.MissileType = MissileType;

        switch (MissileType) {
            case Normal:
                ammunition = 99999; // Just a really big number
                id = 1;
                set_text("Regular\nShot");
                break;
            case Healing:
                ammunition = 2;
                id = 2;
                set_text("Healing\nShot (" + ammunition + ")");
                break;
            case Vision:
                ammunition = 2;
                id = 3;
                set_text("Vision\nShot (" + ammunition + ")");
                break;
            case TripleRandom:
                ammunition = 3;
                id = 4;
                set_manual_text_offset(60, 15);
                set_text("Triple\nRandom\nShot (" + ammunition + ")");
                break;
            default:
                ammunition = 1;
                id = 5;
                set_text(MissileType.name() + "\nShot (" + ammunition + ")");
                break;
        }
    }

    public MissileType getMissileType() {
        return MissileType;
    }

    public boolean can_fire() {
        // can fire if ammunition left
        return ammunition > 0;
    }

    public void use_ammunition() {
        ammunition -= 1;
        switch (MissileType) {
            case Normal:
                set_text("Regular\nShot");
                break;
            case Healing:
                set_text("Healing\nShot\n(" + ammunition + ")");
                break;
            case Vision:
                set_text("Vision\nShot\n(" + ammunition + ")");
                break;
            case TripleRandom:
                set_text("Triple\nRandom\nShot\n(" + ammunition + ")");
                break;
            default:
                set_text(MissileType.name() + "\n(" + ammunition + ")");
                break;
        };
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }
}

