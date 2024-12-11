package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;

public class RiverLevelGenerator extends LevelGenerator {
    public RiverLevelGenerator() {
        super();
    }

    public RiverLevelGenerator(int width, int height) {
        super(width, height);
    }

    @Override
    protected boolean shouldAddObstacles() {
        return false;
    }

    @Override
    protected void addObstacles(Level level, TileFactory destructibleFactory, TileFactory indestructibleFactory) {
        return;
    }

    @Override
    protected boolean shouldAddLiquids() {
        return true;
    }

    @Override
    protected void addLiquids(Level level, TileFactory liquidFactory) {
        // make a river cross in the middle of the level, so that none of the tanks can reach each other,
        // but it is possible to shoot each other

        for (int x = 0; x < width; x++) {
            level.setTile(x, height / 2, liquidFactory.createTile());
        }

        for (int y = 0; y < height; y++) {
            level.setTile(width / 2, y, liquidFactory.createTile());
        }
    }

    @Override
    protected void addPowerUps(Level level) {
        // todo: implement

        return;
    }

    @Override
    protected void addSpawnPoints(Level level) {
        // todo: implement

        return;
    }
}
