package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.tiles.Tile;

import java.util.Random;

public class RandomPlacementGenerator implements LevelGenerator {
    private final Random random = new Random();
    private final int wallDensity; // percentage of level occupied by walls
    private final int indestructibleWallChance;
    private int width;
    private int height;

    public RandomPlacementGenerator() {
        this.wallDensity = 30;
        this.indestructibleWallChance = 5;
        this.width = 10;
        this.height = 10;
    }

    public RandomPlacementGenerator(int wallDensity, int indestructibleWallChance) {
        this.wallDensity = wallDensity;
        this.indestructibleWallChance = indestructibleWallChance;
        this.width = 10;
        this.height = 10;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public Level generateLevel(int width, int height, TileFactory groundFactory, TileFactory destructibleFactory, TileFactory indestructibleFactory, TileFactory liquidFactory) {
        this.width = width;
        this.height = height;

        Level level = new Level(width, height);
        // place wall randomly based on density
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile;
                if (random.nextInt(100) < wallDensity) {
//                        tile = random.nextInt(100) < indestructibleWallChance ? indestructibleFactory.createTile()
//                                : destructibleFactory.createTile();
                    tile = destructibleFactory.createTile();
                } else {
                    tile = groundFactory.createTile();
                }

                level.setTile(x, y, tile);
            }
        }

        // set corner tiles to ground tiles
        level.setTile(0, 0, groundFactory.createTile());
        level.setTile(width - 1, 0, groundFactory.createTile());
        level.setTile(0, height - 1, groundFactory.createTile());
        level.setTile(width - 1, height - 1, groundFactory.createTile());

        return level;
    }

    @Override
    public Level generateLevel(TileFactory groundFactory, TileFactory destructibleFactory, TileFactory indestructibleFactory, TileFactory liquidFactory) {
        return this.generateLevel(this.width, this.height, groundFactory, destructibleFactory, indestructibleFactory, liquidFactory);
    }
}
