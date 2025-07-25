package com.tankbattle.server.builders;

import com.tankbattle.server.factories.*;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.tiles.LiquidTile;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.strategies.Level.LevelGenerator;

public class WinterLevelBuilder extends ILevelBuilder {
    public WinterLevelBuilder(LevelGenerator generator) {
        super(generator);
    }

    /**
     * @return
     */
    @Override
    public ILevelBuilder generateLevel() {
        this.level = generator.generateLevel(this.levelWidth, this.levelHeight, new PassableGroundTileFactory(), new DestructibleTileFactory(), new IndestructibleTileFactory(), new LiquidTileFactory());
        return this;
    }

    /**
     * @return
     */
    public ILevelBuilder addIceTiles() {
        TileFactory liquidTileFactory = new LiquidTileFactory();

        for (int x = 0; x < this.levelWidth; x++) {
            for (int y = 0; y < this.levelHeight; y++) {
                Tile levelTile = this.level.getTile(x, y);

                if (levelTile instanceof LiquidTile) {
                    this.level.setTile(x, y, liquidTileFactory.createTile("ice"));
                }
            }
        }
        return this;
    }

    /**
     * @param count
     * @return
     */
    @Override
    public ILevelBuilder addPowerUps(int count) {
        return this;
    }

    /**
     * @return
     */
    @Override
    public Level build() {
        return this.level;
    }
}
