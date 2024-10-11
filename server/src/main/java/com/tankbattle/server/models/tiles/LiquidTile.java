package com.tankbattle.server.models.tiles;

import com.tankbattle.server.strategies.Destructibility.Indestructible;
import com.tankbattle.server.strategies.Passability.Impassable;
import com.tankbattle.server.strategies.ProjectilePassability.ProjectilePassable;

public class LiquidTile extends Tile {
    public LiquidTile() {
        super(new Impassable(), new ProjectilePassable(), new Indestructible());
    }

    @Override
    public void interact() {
        System.out.println("LiquidTile: tanks can't pass, projectiles can pass; indestructible");
    }

    @Override
    public String getSymbol() {
        return "L";
    }
}
