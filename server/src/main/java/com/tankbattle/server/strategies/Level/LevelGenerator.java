package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;

public abstract class LevelGenerator {
    protected int width;
    protected int height;

    public LevelGenerator() {
        this.width = 10;
        this.height = 10;
    }

    public LevelGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // Template method
    public final Level generateLevel(int width, int height, TileFactory groundFactory, TileFactory destructibleFactory,
            TileFactory indestructibleFactory, TileFactory liquidFactory) {
        this.width = width;
        this.height = height;

        return generateLevel(groundFactory, destructibleFactory, indestructibleFactory, liquidFactory);
    }

    // Template method
    public final Level generateLevel(TileFactory groundFactory, TileFactory destructibleFactory,
            TileFactory indestructibleFactory, TileFactory liquidFactory) {
        Level level = new Level(width, height);

        initializeGround(level, groundFactory);

        if (shouldAddObstacles()) {
            addObstacles(level, destructibleFactory, indestructibleFactory);
        }

        if (shouldAddLiquids()) {
            addLiquids(level, liquidFactory);
        }

        addPowerUps(level);

        addSpawnPoints(level);

        return level;
    }

    private void initializeGround(Level level, TileFactory groundFactory) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                level.setTile(x, y, groundFactory.createTile());
            }
        }
    }

    protected abstract boolean shouldAddObstacles();

    protected abstract void addObstacles(Level level, TileFactory destructibleFactory, TileFactory indestructibleFactory);

    protected abstract boolean shouldAddLiquids();

    protected abstract void addLiquids(Level level, TileFactory liquidFactory);

    protected abstract void addPowerUps(Level level);

    protected abstract void addSpawnPoints(Level level);
}
