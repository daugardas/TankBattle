package com.tankbattle.server.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.Node;
import com.tankbattle.server.utils.Vector2;

import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;

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

        spawnPoints[index] = new Vector2(x, y); // converting to moving coordinates from tile coordinates and adding padding
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
    public boolean canConnect(Vector2 start, Vector2 end) {
        // if the start or end point is not within the level bounds, return false
        if (!isTileWithinLevelBounds(start.getX(), start.getY()) || !isTileWithinLevelBounds(end.getX(), end.getY())) {
            return false;
        }

        // if the start and end points are the same, return true
        if (start.getX() == end.getX() && start.getY() == end.getY()) {
            return true;
        }

        // A* algorithm implementation
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();

        Node startNode = new Node(start, null, 0, start.distanceTo(end));
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(end)) {
                return true;
            }

            closedSet.add(current);
            var neighbors = getNeighbors(current.position);
            for (Vector2 neighbor : neighbors) {
                if (closedSet.contains(new Node(neighbor))) {
                    continue;
                }

                double tentativeG = current.g + current.position.distanceTo(neighbor);
                Node neighborNode = new Node(neighbor, current, tentativeG, neighbor.distanceTo(end));

                if (!openSet.contains(neighborNode) || tentativeG < neighborNode.g) {
                    openSet.add(neighborNode);
                }
            }
        }

        return false;
    }

    private Set<Vector2> getNeighbors(Vector2 position) {
        Set<Vector2> neighbors = new HashSet<>();
        int x = position.getX();
        int y = position.getY();


        if (isTileWithinLevelBounds(x + 1, y) && (isTileDestroyable(x + 1, y) || isTilePassable(x + 1, y)))
            neighbors.add(new Vector2(x + 1, y));
        if (isTileWithinLevelBounds(x - 1, y) && (isTileDestroyable(x - 1, y) || isTilePassable(x - 1, y)))
            neighbors.add(new Vector2(x - 1, y));
        if (isTileWithinLevelBounds(x, y + 1) && (isTileDestroyable(x, y + 1) || isTilePassable(x, y + 1)))
            neighbors.add(new Vector2(x, y + 1));
        if (isTileWithinLevelBounds(x, y - 1) && (isTileDestroyable(x, y - 1) || isTilePassable(x, y - 1)))
            neighbors.add(new Vector2(x, y - 1));

        return neighbors;
    }

    private boolean isTilePassable(int x, int y) {
        Tile tile = getTile(x, y);
        return tile != null && tile.canPass();
    }

    private boolean isTileDestroyable(int x, int y) {
        Tile tile = getTile(x, y);
        return tile != null && tile.canBeDestroyed();
    }

    @JsonIgnore
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // level dimensions
        if (this.spawnPoints != null && this.spawnPoints[this.spawnPointsCount - 1] != null) {
            sb.append("{ width: ").append(width).append(", height: ").append(height).append(", spawnPoints: [");

            for (int i = 0; i < spawnPoints.length; i++) {
                sb.append(spawnPoints[i].toString());
                if (i < spawnPoints.length - 1) {
                    sb.append(", ");
                }
            }

            sb.append("] }\n\n");
        } else {
            sb.append("{ width: ").append(width).append(", height: ").append(height).append(" }");
        }

        if (grid != null) {
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

            if (spawnPoints != null && this.spawnPoints[this.spawnPointsCount - 1] != null) {
                // set the spawn points on the grid
                for (int i = 0; i < spawnPoints.length; i++) {
                    Vector2 spawnPoint = spawnPoints[i];
                    gridSb.setCharAt(spawnPoint.getY() * (width + 1) + spawnPoint.getX(), (char) ('0' + i));
                }
            }

            sb.append(gridSb.toString());
        }

        return sb.toString();
    }

    public Tile[][] getTiles() {
        return grid;
    }

}
