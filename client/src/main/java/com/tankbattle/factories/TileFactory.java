package com.tankbattle.factories;

import com.tankbattle.models.tiles.Tile;
import com.tankbattle.models.tiles.defaultTheme.DestructibleTile;
import com.tankbattle.models.tiles.defaultTheme.IndestructibleTile;
import com.tankbattle.models.tiles.defaultTheme.LiquidTile;
import com.tankbattle.models.tiles.defaultTheme.PassableGroundTile;

public class TileFactory {
    public TileFactory() {
    }

    public Tile createTile(char tileType) throws IllegalArgumentException {
        switch (tileType) {
            case 'D':
                return new DestructibleTile();
            case 'L':
                return new LiquidTile();
            case 'I':
                return new IndestructibleTile();
            case 'G':
                return new PassableGroundTile();
            default:
                throw new IllegalArgumentException("Invalid tile type: " + tileType);
        }
    }
}