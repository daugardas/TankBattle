package com.tankbattle.server.models.tiles;

import com.tankbattle.server.strategies.Destructibility.Indestructible;
import com.tankbattle.server.strategies.Passability.Passable;
import com.tankbattle.server.strategies.ProjectilePassability.ProjectilePassable;

public class PassableGroundTile extends Tile {
    public PassableGroundTile() {
        super(new Passable(), new ProjectilePassable(), new Indestructible());
    }

    @Override
    public void interact() {
        System.out.println("PassableGroundTile: Tanks and shots can pass");
    }

    @Override
    public String getSymbol() {
        return "G"; // return empty string to signify passable ground tile
    }
}
