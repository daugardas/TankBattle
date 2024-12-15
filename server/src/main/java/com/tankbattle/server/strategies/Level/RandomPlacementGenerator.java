package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.tiles.Tile;

import java.util.Random;

public class RandomPlacementGenerator extends LevelGenerator {
    private final Random random = new Random();
    private final int wallDensity; // percentage of level occupied by walls
    private final int indestructibleWallChance;

    public RandomPlacementGenerator() {
        super(10, 10);
        this.wallDensity = 40;
        this.indestructibleWallChance = 10;
    }

    public RandomPlacementGenerator(int wallDensity, int indestructibleWallChance) {
        super(10, 10);
        this.wallDensity = wallDensity;
        this.indestructibleWallChance = indestructibleWallChance;
    }

    @Override
    protected boolean shouldAddObstacles() {
        return true;
    }

    @Override
    protected void addObstacles(Level level, TileFactory destructibleFactory, TileFactory indestructibleFactory) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // skip corner tiles for spawnpoints
                if ((x == 0 && y == 0) || (x == width - 1 && y == 0) || (x == 0 && y == height - 1)
                        || (x == width - 1 && y == height - 1)) {
                    continue;
                }

                Tile tile = level.getTile(x, y);
                if (random.nextInt(100) < wallDensity) {
                    tile = random.nextInt(100) < indestructibleWallChance ? indestructibleFactory.createTile()
                            : destructibleFactory.createTile();
                    // tile = destructibleFactory.createTile();
                }

                level.setTile(x, y, tile);
            }
        }
    }

    @Override
    protected boolean shouldAddLiquids() {
        return true;
    }

    @Override
    // no liquids in this generator
    protected void addLiquids(Level level, TileFactory liquidFactory) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // skip corner tiles for spawnpoints
                if ((x == 0 && y == 0) || (x == width - 1 && y == 0) || (x == 0 && y == height - 1)
                        || (x == width - 1 && y == height - 1)) {
                    continue;
                }

                Tile tile = level.getTile(x, y);
                if (random.nextInt(100) < 20) {
                    tile = liquidFactory.createTile();
                }

                level.setTile(x, y, tile);
            }
        }
        return;
    }

    @Override
    protected void addPowerUps(Level level) {
        return;
    }

    @Override
    protected void addSpawnPoints(Level level) {
        return;
    }
}
