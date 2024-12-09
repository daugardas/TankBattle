package com.tankbattle.factories;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.models.tiles.TileType;
import com.tankbattle.models.tiles.TileTypeFlyweightFactory;

public class TileFactory {
    private final TileTypeFlyweightFactory tileTypeFactory;
    public TileFactory(ResourceManager resourceManager) {
        tileTypeFactory = new TileTypeFlyweightFactory(resourceManager);
    }

    public Tile createTile(char tileType) throws IllegalArgumentException {
        String fullTypeName = switch (tileType) {
            case 'D' -> "DestructibleTile";
            case 'L' -> "LiquidTile";
            case 'I' -> "IndestructibleTile";
            case 'G' -> "PassableGroundTile";
            default -> throw new IllegalArgumentException("Invalid tile type: " + tileType);
        };

        TileType tileTypeFlyweight = tileTypeFactory.createTileType(fullTypeName);
        return new Tile(tileTypeFlyweight);
    }
}