package com.mygdx.group17.shipocalypse.models;

import java.util.ArrayList;
import java.util.Arrays;

public class Missile {
    private final MissileType missileType;

    private int ammunition;

    private final int id; // same as missileType, but for firebase as it's easier to save.

    private final MissileTarget target;

    private String missileText;

    public Missile(MissileType missileType) {
        this.missileType = missileType;
        switch (missileType) {
            case Normal:
                ammunition = 99999; // Just a really big number
                id = 1;
                target = MissileTarget.TargetOpponent;
                missileText = "Regular\nShot";
                break;
            case Healing:
                ammunition = 2;
                id = 2;
                target = MissileTarget.TargetSelf;
                missileText = "Healing\nShot (" + ammunition + ")";
                break;
            case Vision:
                ammunition = 2;
                id = 3;
                target = MissileTarget.TargetOpponent;
                missileText = "Vision\nShot (" + ammunition + ")";
                break;
            case TripleRandom:
                ammunition = 3;
                id = 4;
                target = MissileTarget.TargetOpponent;
                missileText = "Triple\nRandom\nShot (" + ammunition + ")";
                break;
            default:
                ammunition = 1;
                id = 5;
                target = MissileTarget.TargetOpponent;
                missileText = "Shot (" + ammunition + ")";
                break;
        }
    }

    // Currently does not allow for TargetBoth, would require some minor refactoring.
    // Returns true if missile successfully fired, otherwise it returns false.
    public void fire(Player player, Player opponent, Tile selected_tile, ArrayList<Tile> turn_tiles) {
        Grid target_grid = null;
        switch (target) {
            case TargetOpponent:
                target_grid = opponent.get_grid();
                System.out.println("Using opponent grid");
                break;
            case TargetSelf:
                target_grid = player.get_grid();
                System.out.println("Using self grid");
                break;
            default:
                target_grid = opponent.get_grid();
                break;
        }

        // Check if our currently selected tile is in the grid applicable to the
        // currently selected missile.
        if (!target_grid.isTileInGrid(selected_tile) && missileType != MissileType.TripleRandom) {
            return;
        }

        switch (missileType) {
            case Normal:
                for (Boat boat: opponent.getBoatConfig().boats) {
                    for (Tile boat_tile : boat.getTiles()) {
                        if (selected_tile == boat_tile) {
                            boat.hit(boat_tile);
                        }
                    }
                }
                System.out.println("normal hit tile");
                selected_tile.hit();
                updateMissileText();
                return;

            case Healing:
                for (Boat boat: player.getBoatConfig().boats) {
                    for (Tile boat_tile: boat.getTiles()) {
                        if (boat_tile == selected_tile) {
                            boat.heal(boat_tile);
                            ammunition -= 1;
                            updateMissileText();
                            return;
                        }
                    }
                }
                break;

            case Vision:
                ArrayList<Tile> surrounding_tiles = opponent.get_grid().get_surrounding_tiles(selected_tile);

                for (Tile target_tile : surrounding_tiles) {
                    for (Boat boat: opponent.getBoatConfig().boats) {
                        for (Tile boat_tile : boat.getTiles()) {
                            if (boat_tile == target_tile) {
                                target_tile.exposed();
                            } /*else {
                                target_tile.hit();
                            } causes issues */
                        }
                    }
                }
                ammunition -= 1;
                updateMissileText();
                return;

            case TripleRandom:
                // Generate 3 random tiles
                Tile[] random_tiles = {null, null, null};
                for (int i = 0; i < 3; i++) {
                    random_tiles[i] = target_grid.get_random_tile();
                    // Hit here to avoid calling the hit function many times
                    // on each random tile in the later loop.
                    random_tiles[i].hit();
                }
                turn_tiles.addAll(Arrays.asList(random_tiles));
                // Check for a hit on any of the random tiles.
                for (Boat boat: opponent.getBoatConfig().boats) {
                    for (Tile boat_tile: boat.getTiles()) {
                        if (Arrays.asList(random_tiles).contains(boat_tile)) {
                            boat.hit(boat_tile);
                            if (boat.isSunk()) {
                                boat.show();
                            }
                        }
                    }
                }
                ammunition -= 1;
                updateMissileText();
                return;
        }
        return;
    }

    public MissileType getMissileType() { return missileType; }

    public String getMissileText() { return missileText; }

    public boolean canSelectATile() {
        switch (missileType) {
            case TripleRandom:
                return false;
            default:
                return true;
        }
    }

    public boolean hasAmmunition() { return ammunition > 0; }

    public boolean canFire(Player player, Player opponent, Tile selected_tile) {
        if (ammunition <= 0) {
            return false;
        }

        Grid target_grid = null;
        switch (target) {
            case TargetOpponent:
                target_grid = opponent.get_grid();
                break;
            case TargetSelf:
                target_grid = player.get_grid();
                break;
            default:
                target_grid = opponent.get_grid();
                break;
        }

        // Check if our currently selected tile is in the grid applicable to the
        // currently selected missile.
        if (!target_grid.isTileInGrid(selected_tile) && missileType != MissileType.TripleRandom) {
            return false;
        }

        switch (missileType) {
            case Healing:
                for (Boat boat: player.getBoatConfig().boats) {
                    for (Tile boat_tile: boat.getTiles()) {
                        if (boat_tile == selected_tile) {
                            if (boat.canHeal(boat_tile)){
                                return true;
                            }
                        }
                    }
                }
                break;

            case Normal:
            case TripleRandom:
            case Vision:
                return true;
        }
        return false;
    }

    public int getId() { return id; }

    private void updateMissileText() {
        switch (missileType) {
            case Normal:
                missileText = "Regular\nShot";
                break;
            case Healing:
                missileText = "Healing\nShot (" + ammunition + ")";
                break;
            case Vision:
                missileText = "Vision\nShot (" + ammunition + ")";
                break;
            case TripleRandom:
                missileText = "Triple\nRandom\nShot (" + ammunition + ")";
                break;
            default:
                missileText = "Shot (" + ammunition + ")";
                break;
        }
    }
}
