package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;

public class RiverLevelGenerator implements LevelGenerator {
    private int width;
    private int height;

    public RiverLevelGenerator() {
        this.width = 10;
        this.height = 10;
    }

    public RiverLevelGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @param width
     */
    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @param height
     */
    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @param width
     * @param height
     * @param groundFactory
     * @param destructibleFactory
     * @param indestructibleFactory
     * @param liquidFactory
     * @return
     */
    @Override
    public Level generateLevel(int width, int height, TileFactory groundFactory, TileFactory destructibleFactory, TileFactory indestructibleFactory, TileFactory liquidFactory) {
        this.width = width;
        this.height = height;

        Level level = new Level(width, height);

        // set all tiles to ground
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                level.setTile(x, y, groundFactory.createTile());
            }
        }

        // make a river cross in the middle of the level, so that none of the tanks can reach each other,
        // but it is possible to shoot each other
        // todo: implement

        return level;
    }

    /**
     * @param groundFactory
     * @param destructibleFactory
     * @param indestructibleFactory
     * @param liquidFactory
     * @return
     */
    @Override
    public Level generateLevel(TileFactory groundFactory, TileFactory destructibleFactory, TileFactory indestructibleFactory, TileFactory liquidFactory) {
        return this.generateLevel(this.width, this.height, groundFactory, destructibleFactory, indestructibleFactory, liquidFactory);
    }
}
