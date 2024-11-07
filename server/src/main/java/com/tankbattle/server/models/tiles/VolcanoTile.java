package com.tankbattle.server.models.tiles;

import com.tankbattle.server.strategies.Destructibility.Indestructible;
import com.tankbattle.server.strategies.Passability.Impassable;
import com.tankbattle.server.strategies.ProjectilePassability.ProjectileImpassable;

public class VolcanoTile extends Tile {
    public VolcanoTile() {
        super(new Impassable(), new ProjectileImpassable(), new Indestructible());
    }

    /**
     *
     */
    @Override
    public void interact() {

    }

    /**
     * @return
     */
    @Override
    public String getSymbol() {
        return "V";
    }
}
