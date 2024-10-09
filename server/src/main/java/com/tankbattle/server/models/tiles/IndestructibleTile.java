package com.tankbattle.server.models.tiles;

import com.tankbattle.server.strategies.Destructibility.Indestructible;
import com.tankbattle.server.strategies.Passability.Impassable;
import com.tankbattle.server.strategies.ProjectilePassability.ProjectileImpassable;

public class IndestructibleTile extends Tile {
    public IndestructibleTile() {
        super(new Impassable(), new ProjectileImpassable(), new Indestructible());
    }

    @Override
    public void interact() {
        System.out.println("IndestructibleTile: Tanks and bullets can't pass; indestructible");
    }

    @Override
    public String getSymbol() {
        return "#";
    }
}
