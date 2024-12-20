package com.tankbattle.server.strategies.Level;

import java.util.Random;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.iterators.LevelTileIterator;
import com.tankbattle.server.iterators.LineByLineIterator;
import com.tankbattle.server.iterators.SpiralIterator;
import com.tankbattle.server.iterators.TilePosition;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.tiles.Tile;

public class RandomPlacementGenerator extends LevelGenerator {
    private final Random random = new Random();
    private final int wallDensity; // percentage of level occupied by walls
    private final int indestructibleWallChance;
    private final LevelTileIterator iterator;

    public RandomPlacementGenerator() {
        this(40, 10, new LineByLineIterator(null));
    }

    public RandomPlacementGenerator(int wallDensity, int indestructibleWallChance) {
        this(wallDensity, indestructibleWallChance, new LineByLineIterator(null));
    }

    public RandomPlacementGenerator(int wallDensity, int indestructibleWallChance, LevelTileIterator iterator) {
        super(30, 30);
        this.wallDensity = wallDensity;
        this.indestructibleWallChance = indestructibleWallChance;
        this.iterator = iterator;
    }

    @Override
    protected boolean shouldAddObstacles() {
        return true;
    }

    @Override
    protected void addObstacles(Level level, TileFactory destructibleFactory, TileFactory indestructibleFactory) {
        iterator.setLevel(level);
        iterator.reset();
        
        int distanceFromStart = 0;
        int totalTiles = width * height;
        
        while (iterator.hasNext()) {
            try {
                TilePosition pos = iterator.next();
                if (pos == null) continue;
                
                int x = pos.getX();
                int y = pos.getY();
                
                // skip corner tiles for spawnpoints
                if ((x == 0 && y == 0) || (x == width - 1 && y == 0) || 
                    (x == 0 && y == height - 1) || (x == width - 1 && y == height - 1)) {
                    continue;
                }

                int adjustedWallDensity = getAdjustedWallDensity(x, y, distanceFromStart, totalTiles);
                
                Tile tile = level.getTile(x, y);
                if (random.nextInt(100) < adjustedWallDensity) {
                    if (iterator instanceof SpiralIterator) {
                        // Create alternating pattern based on distance from start
                        double progress = (double) distanceFromStart / totalTiles;
                        // Create a repeating pattern every 20 tiles
                        int pattern = (distanceFromStart / 3) % 3;
                        switch (pattern) {
                            case 0:
                                tile = indestructibleFactory.createTile();
                                break;
                            case 1:
                                tile = destructibleFactory.createTile();
                                break;
                            case 2:
                                // Leave empty space for this segment
                                break;
                        }
                    } else {
                        tile = random.nextInt(100) < indestructibleWallChance ? 
                            indestructibleFactory.createTile() : 
                            destructibleFactory.createTile();
                    }
                }
                level.setTile(x, y, tile);
                distanceFromStart++;
            } catch (Exception e) {
                continue;
            }
        }
    }

    private int getAdjustedWallDensity(int x, int y, int distanceFromStart, int totalTiles) {
        if (iterator instanceof SpiralIterator) {
            // For spiral, always place walls but alternate between types
            double progress = (double) distanceFromStart / totalTiles;
            // Make it almost guaranteed to place a wall
            return 95;
        } else if (iterator instanceof LineByLineIterator) {
            // For spawn points, decrease density as we move away from spawn points
            int minDistanceToSpawn = Math.min(
                Math.min(
                    Math.min(
                        distance(x, y, 0, 0),
                        distance(x, y, width-1, 0)
                    ),
                    distance(x, y, 0, height-1)
                ),
                distance(x, y, width-1, height-1)
            );
            double factor = 1.0 - (minDistanceToSpawn / (double) Math.max(width, height));
            return (int) (wallDensity * (0.5 + factor));
        }
        return wallDensity;
    }

    private int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
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
