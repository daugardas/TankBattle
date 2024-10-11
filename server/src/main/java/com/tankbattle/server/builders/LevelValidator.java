package com.tankbattle.server.builders;

import com.tankbattle.server.models.Level;
import com.tankbattle.server.utils.Vector2;

public final class LevelValidator {
    public static boolean isFullyConnected(Level level) {
        // need to check if each spawn point is reachable from each other spawn point
        for (Vector2 spawnPoint : level.getSpawnPoints()) {
            if (!spawnPointCanReachOtherSpawnPoints(level, spawnPoint)) {
                return false;
            }
        }

        return true;
    }

    private static boolean spawnPointCanReachOtherSpawnPoints(Level level, Vector2 spawnPoint) {
        for (Vector2 otherSpawnPoint : level.getSpawnPoints()) {
            if (spawnPoint != otherSpawnPoint
                    && !spawnPointCanReachOtherSpawnPoint(level, spawnPoint, otherSpawnPoint)) {
                return false;
            }
        }

        return true;
    }

    private static boolean spawnPointCanReachOtherSpawnPoint(Level level, Vector2 spawnPoint, Vector2 otherSpawnPoint) {
        // need to check if there is a path between the two spawn points

        // path can be made up of tiles that are passable

        // todo: implement pathfinding algorithm

        return true;
    }
}
