package com.tankbattle.server.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.Vector2;

public class Level {
    private int width;
    private int height;
    private Tile[][] grid;
    private int spawnPointsCount;
    private Vector2[] spawnPoints;

    public Level() {
        width = 50;
        height = 50;
        spawnPointsCount = 4;

        initializeLevel();
    }

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        spawnPointsCount = 4;

        initializeLevel();
    }

    public Level(int width, int height, int spawnPointsCount) {
        this.width = width;
        this.height = height;
        this.spawnPointsCount = spawnPointsCount;

        initializeLevel();
    }

    // JSON getters for serialization and sending to the client

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @JsonIgnore
    public int getSpawnPointsCount() {
        return spawnPointsCount;
    }

    @JsonGetter("spawnPoints")
    public Vector2[] getSpawnPoints() {
        return spawnPoints;
    }

    @JsonIgnore
    public Tile[][] getGrid() {
        return grid;
    }

    @JsonGetter("tiles")
    public String[] getTilesJson() {
        String[] tiles = new String[height];
        for (int y = 0; y < height; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {
                Tile tile = grid[x][y];
                if (tile == null) {
                    sb.append("-");
                } else {
                    sb.append(tile.getSymbol());
                }
            }
            tiles[y] = sb.toString();
        }

        return tiles;
    }

    // methods below are not serialized to JSON

    private void initializeLevel() {
        grid = new Tile[width][height];
        spawnPoints = new Vector2[spawnPointsCount]; // 4 spawn points for 4 players

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = null;
            }
        }
    }

    private boolean isTileWithinLevelBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    @JsonIgnore
    public void setTile(int x, int y, Tile tile) {
        if (isTileWithinLevelBounds(x, y)) {
            grid[x][y] = tile;
        }
    }

    @JsonIgnore
    public Tile getTile(int x, int y) {
        if (isTileWithinLevelBounds(x, y)) {
            return grid[x][y];
        }

        return null;
    }

    @JsonIgnore
    public void setSpawnPoints(Vector2[] points) {
        if (points.length != spawnPointsCount) {
            throw new IllegalArgumentException(
                    "Expected" + spawnPointsCount + " spawn points, but got " + points.length);
        }

        // make sure that the points are Vector2 objects
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Spawn location cannot be null");
            }
        }

        // make sure that the points are within the level bounds
        for (int i = 0; i < points.length; i++) {
            if (!isTileWithinLevelBounds(points[i].getX(), points[i].getY())) {
                throw new IllegalArgumentException("Spawn location is out of bounds");
            }
        }

        spawnPoints = points;
    }

    @JsonIgnore
    public void addSpawnLocation(int x, int y) {
        int filledspawnPoints = 0;
        for (int i = 0; i < spawnPoints.length; i++) {
            if (spawnPoints[i] != null) {
                filledspawnPoints++;
            }
        }

        if (filledspawnPoints >= spawnPointsCount) {
            throw new IllegalArgumentException("Cannot add more spawn points");
        }

        if (!isTileWithinLevelBounds(x, y)) {
            throw new IllegalArgumentException("Spawn location is out of bounds");
        }

        spawnPoints[spawnPointsCount] = new Vector2(x, y);
    }

    @JsonIgnore
    public void setSpawnLocation(int index, int x, int y) {
        if (index < 0 || index >= spawnPointsCount) {
            throw new IllegalArgumentException("Invalid spawn location index");
        }

        if (!isTileWithinLevelBounds(x, y)) {
            throw new IllegalArgumentException("Spawn location is out of bounds");
        }

        spawnPoints[index] = new Vector2(x, y);
    }

    @JsonIgnore
    public boolean isTileASpawnLocation(int x, int y) {
        for (int i = 0; i < spawnPoints.length; i++) {
            // if spawn point is null, it means that it hasn't been set yet
            if (spawnPoints[i] == null) {
                continue;
            }

            if (spawnPoints[i].getX() == x && spawnPoints[i].getY() == y) {
                return true;
            }
        }

        return false;
    }

    @JsonIgnore
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // level dimensions
        sb.append("{ width: " + width + ", height: " + height + ", spawnPoints: [");

        for (int i = 0; i < spawnPoints.length; i++) {
            sb.append(spawnPoints[i].toString());
            if (i < spawnPoints.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("] }\n\n");

        // level grid
        sb.append("Level grid:\n");

        StringBuilder gridSb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = grid[x][y];
                if (tile == null) {
                    System.out.println("Tile is null. This should't happen.");
                } else {
                    gridSb.append(tile.getSymbol());
                }
            }
            gridSb.append("\n");
        }

        // set the spawn points on the grid
        for (int i = 0; i < spawnPoints.length; i++) {
            Vector2 spawnPoint = spawnPoints[i];
            gridSb.setCharAt(spawnPoint.getY() * (width + 1) + spawnPoint.getX(), (char) ('0' + i));
        }

        return sb.append(gridSb.toString()).toString();
    }

}
