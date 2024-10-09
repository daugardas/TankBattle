package com.tankbattle.server.strategies.Level;

import java.util.Random;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.models.Level;

public class RandomPlacementGenerator implements ProceduralGenerator {
    private Random random = new Random();
    private int wallDensity; // percentage of level occupied by walls
    private int indestructibleWallChance;

    public RandomPlacementGenerator(int wallDensity, int indestructibleWallChance) {
        this.wallDensity = wallDensity;
        this.indestructibleWallChance = indestructibleWallChance;
    }

    @Override
    public Level generateLevel(int width, int height, TileFactory groundFactory, TileFactory destructibleFactory,
            TileFactory indestructibleFactory, TileFactory liquidFactory) {
        Level level = new Level(width, height);

        // place wall randomly based on density
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile;
                if (random.nextInt(100) < wallDensity) {
                    tile = random.nextInt(100) < indestructibleWallChance ? indestructibleFactory.createTile()
                            : destructibleFactory.createTile();
                } else {
                    tile = groundFactory.createTile();
                }

                level.setTile(x, y, tile);
            }
        }

        placeSpawnLocations(level);

        return level;
    }

    public void placeSpawnLocations(Level level) {
        int placed = 0;

        while (placed < level.getSpawnPointsCount()) {
            int x = random.nextInt(level.getWidth());
            int y = random.nextInt(level.getHeight());

            // if tile is not already a spawn location, and is of PassableGroundTile class
            if (level.getTile(x, y).canPass() && !level.isTileASpawnLocation(x, y) && isSafe(level, x, y)) {
                level.setSpawnLocation(placed, x, y);
                placed++;
            }
        }
    }

    // check if the tile is surrounded by passable tiles
    private boolean isSafe(Level level, int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;

                Tile tile = level.getTile(nx, ny);
                if (tile != null && !tile.canPass()) {
                    return false;
                }
            }
        }

        return true;
    }

    private void placePowerUps(Level level, int count) {
        // TODO: implement
    }
}
