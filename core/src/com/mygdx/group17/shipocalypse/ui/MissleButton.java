package com.mygdx.group17.shipocalypse.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.group17.shipocalypse.models.Action;
import com.mygdx.group17.shipocalypse.singletons.AssetManager;

public class MissleButton extends Button {

    private MissleType missleType;
    private int ammunition;

    public MissleButton(ShapeRenderer shape, int x, int y, int width, MissleType missleType) {
        super(shape, x, y, missleType.name(), width);
        this.missleType = missleType;

        switch (missleType) {
            case Normal:
                ammunition = 99999; // Just a really big number
                break;
            case Healing:
            case Vision:
            case TripleRandom:
            default:
                ammunition = 1;
                break;
        }
    }

    public MissleType getMissleType() {
        return missleType;
    }
    public boolean can_fire () {
        // can fire if ammunition left
        return ammunition > 0;
    }

    public void use_ammunition() {
        ammunition -= 1;
    }
    @Override
    public void render() {
        super.render();

//        AssetManager.write("ammo: " + ammunition, (int)this.get_position().x - 25, (int)this.get_position().y - 25);
    }

    @Override
    public void dispose() {

    }
}

