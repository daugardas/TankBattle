package com.tankbattle.server.factories;

import com.tankbattle.server.models.tiles.IceTile;
import com.tankbattle.server.models.tiles.LiquidTile;
import com.tankbattle.server.models.tiles.Tile;

public class LiquidTileFactory implements TileFactory {
    private final String liquidType;

    public LiquidTileFactory() {
        this.liquidType = "default";
    }

    public LiquidTileFactory(String liquidType) {
        this.liquidType = liquidType;
    }

    @Override
    public Tile createTile() {
        return createTile(liquidType);
    }

    @Override
    public Tile createTile(String type) {
        // todo: add lava tiles

        switch (type) {
            case "ice":
                return new IceTile();
            case "lava":
                System.out.println("Lava tiles are not implemented. Returning default tile");
                return new LiquidTile();
            default:
                return new LiquidTile();
        }
    }

}
