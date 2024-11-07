package com.tankbattle.server.factories;

import com.tankbattle.server.models.tiles.DestructibleTile;
import com.tankbattle.server.models.tiles.Tile;

public class DestructibleTileFactory implements TileFactory {
    @Override
    public Tile createTile() {
        return new DestructibleTile();
    }

    @Override
    public Tile createTile(String type) {
        return createTile();
    }

}
