package com.tankbattle.server.models.tiles;

import com.tankbattle.server.strategies.Destructibility.Destructible;
import com.tankbattle.server.strategies.Passability.Passable;
import com.tankbattle.server.strategies.ProjectilePassability.ProjectilePassable;

public class IceTile extends Tile {
    public IceTile() {
        super(new Passable(), new ProjectilePassable(), new Destructible());
    }

    @Override
    public void interact() {
        System.out.println("Ice Tile: tanks can pass, can shoot through, and when driven around the tile may turn into a water tile");
    }

    @Override
    public String getSymbol() {
        return "I";
    }
}
