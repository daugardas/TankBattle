package com.tankbattle.server.factories;

import com.tankbattle.server.models.tiles.PassableGroundTile;
import com.tankbattle.server.models.tiles.Tile;

public class PassableGroundTileFactory implements TileFactory {
    @Override
    public Tile createTile() {
        return new PassableGroundTile();
    }

    @Override
    public Tile createTile(String type) {
        return this.createTile();
    }
}
