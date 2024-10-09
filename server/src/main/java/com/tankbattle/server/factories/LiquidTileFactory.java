package com.tankbattle.server.factories;

import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.models.tiles.LiquidTile;

public class LiquidTileFactory implements TileFactory {
    @Override
    public Tile createTile() {
        return new LiquidTile();
    }
}
