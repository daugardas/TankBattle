package com.tankbattle.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tankbattle.controllers.GameManager;
import com.tankbattle.factories.TileFactory;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

public class Level {
    private int width;
    private int height;
    private Tile[][] grid;
    private Vector2[] spawnPoints;

    public Level() {
    }

    @JsonCreator
    public Level(@JsonProperty("width") int width, @JsonProperty("height") int height, @JsonProperty("tiles") String[] tiles, @JsonProperty("spawnPoints") Vector2[] spawnPoints) {
        this.width = width;
        this.height = height;
        setTiles(tiles);
        setSpawnPoints(spawnPoints);
    }

    @JsonIgnore
    public Level(int width, int height, Tile[][] grid, Vector2[] spawnPoints) {
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.spawnPoints = spawnPoints;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @JsonIgnore
    public Tile[][] getGrid() {
        return grid;
    }

    @JsonIgnore
    public void setGrid(Tile[][] grid) {
        this.grid = grid;
    }

    @JsonIgnore
    public Vector2[] getSpawnPoints() {
        return spawnPoints;
    }

    @JsonIgnore
    public void setSpawnPoints(Vector2[] spawnPoints) {
        this.spawnPoints = spawnPoints;
    }

    public void setTiles(String[] tiles) {
        Tile[][] grid = new Tile[width][height];

        TileFactory tileFactory = GameManager.getInstance().getRenderFacade().getTileFactory();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char tileType = tiles[x].charAt(y);
                try {
                    grid[y][x] = tileFactory.createTile(tileType);
                    grid[y][x].setLocation(x, y);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid tile type: " + tileType);
                }
            }
        }

        this.grid = grid;
    }

    @JsonIgnore
    public Tile getTile(int x, int y) {
        return grid[x][y];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Level:\n width = ").append(width).append(", height = ").append(height).append("\n");

        // append spawn points
        if (spawnPoints != null) {
            sb.append("Spawn points:\n");
            for (Vector2 spawnPoint : spawnPoints) {
                sb.append(spawnPoint.toString()).append("\n");
            }
        } else {
            sb.append("No spawn points\n");
        }

        // append grid
        if (grid != null) {
            sb.append("Grid:\n");
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    sb.append(grid[i][j].toString()).append(" ");
                }
                sb.append("\n");
            }
        } else {
            sb.append("No grid\n");
        }

        return sb.toString();
    }

}
