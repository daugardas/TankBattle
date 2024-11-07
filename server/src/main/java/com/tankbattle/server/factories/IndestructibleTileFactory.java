package com.tankbattle.server.factories;

import com.tankbattle.server.models.tiles.IndestructibleTile;
import com.tankbattle.server.models.tiles.Tile;

public class IndestructibleTileFactory implements TileFactory {
    @Override
    public Tile createTile() {
        return new IndestructibleTile();
    }

    @Override
    public Tile createTile(String type) {
        return createTile();
    }
}
