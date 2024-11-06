package com.tankbattle.server.strategies.Level;

import java.util.ArrayList;
import java.util.Random;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.utils.Vector2;

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
        // generate level until it is possible to create spawn locations and they are connectable
        boolean spawnLocationsAreValid = false;
        while (!spawnLocationsAreValid) {
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

            spawnLocationsAreValid = placeSpawnLocations(level);
            if (!spawnLocationsAreValid) {
                level = new Level(width, height);
            }
        }

        return level;
    }


    public boolean placeSpawnLocations(Level level) {
        // as all player locations are in the corners of the map, we can just place them there and then check if they are connectable through passable tiles
        Vector2[] spawnPoints = new Vector2[level.getSpawnPointsCount()];
        spawnPoints[0] = new Vector2(0, 0);
        spawnPoints[1] = new Vector2(level.getWidth() - 1, 0);
        spawnPoints[2] = new Vector2(0, level.getHeight() - 1);
        spawnPoints[3] = new Vector2(level.getWidth() - 1, level.getHeight() - 1);

        // check if you can get from one point to another with A*
//        for (int i = 0; i < level.getSpawnPointsCount(); i++) {
//            for (int j = i + 1; j < level.getSpawnPointsCount(); j++) {
//                if (!level.canConnect(spawnPoints[i], spawnPoints[j])) {
//                    // if you can't connect the spawn points, return false
//                    return false;
//                }
//            }
//        }

        for (int i = 0; i < level.getSpawnPointsCount(); i++) {
            level.setSpawnLocation(i, spawnPoints[i].getX(), spawnPoints[i].getY());
        }

        return true;
    }

    private void placePowerUps(Level level, int count) {
        // TODO: implement
    }
}
