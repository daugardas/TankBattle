package com.tankbattle.server.models.tiles;

import com.tankbattle.server.strategies.Destructibility.Destructible;
import com.tankbattle.server.strategies.Passability.Impassable;
import com.tankbattle.server.strategies.ProjectilePassability.ProjectileImpassable;

public class DestructibleTile extends Tile {
    public DestructibleTile() {
        super(new Impassable(), new ProjectileImpassable(), new Destructible());
    }

    @Override
    public void interact() {
        System.out.println("DestructibleTile: can be destroyed, blocks tanks and shots");
    }

    @Override
    public String getSymbol() {
        return "D";
    }
}
