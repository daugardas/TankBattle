package com.tankbattle.server.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tankbattle.server.models.GameEntity;

/**
 * SpatialGrid partitions the game world into grid cells to optimize collision detection.
 * Utilizes an immutable GridCell as keys and thread-safe collections for high performance.
 */
public class SpatialGrid {
    private final int cellSize;
    private final int worldWidth;
    private final int worldHeight;
    private final Map<GridCell, List<GameEntity>> grid;

    /**
     * Initializes the SpatialGrid.
     *
     * @param cellSize   Size of each grid cell in world units.
     * @param worldWidth  Width of the game world in units.
     * @param worldHeight Height of the game world in units.
     */
    public SpatialGrid(int cellSize, int worldWidth, int worldHeight) {
        this.cellSize = cellSize;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.grid = new ConcurrentHashMap<>();
    }

    /**
     * Clears all entities from the grid.
     */
    public void clear() {
        grid.clear();
    }

    /**
     * Adds an entity to the appropriate grid cell(s) based on its position and size.
     *
     * @param entity The game entity to add.
     */
    public void addEntity(GameEntity entity) {
        List<GridCell> occupiedCells = getOccupiedCells(entity);
        for (GridCell cell : occupiedCells) {
            grid.computeIfAbsent(cell, k -> Collections.synchronizedList(new ArrayList<>())).add(entity);
        }
    }

    /**
     * Retrieves a list of entities that are in the same or adjacent cells as the given entity.
     *
     * @param entity The reference game entity.
     * @return A list of nearby entities.
     */
    public List<GameEntity> getNearbyEntities(GameEntity entity) {
        List<GridCell> occupiedCells = getOccupiedCells(entity);
        List<GameEntity> nearby = new ArrayList<>();

        for (GridCell cell : occupiedCells) {
            // Check the cell and its 8 neighbors
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int adjacentX = cell.getX() + dx;
                    int adjacentY = cell.getY() + dy;

                    // Ensure adjacent cells are within world boundaries
                    if (adjacentX < 0 || adjacentX >= getTotalCellsX() ||
                        adjacentY < 0 || adjacentY >= getTotalCellsY()) {
                        continue;
                    }

                    GridCell adjacentCell = new GridCell(adjacentX, adjacentY);
                    List<GameEntity> entitiesInCell = grid.getOrDefault(adjacentCell, Collections.emptyList());
                    synchronized (entitiesInCell) {
                        nearby.addAll(entitiesInCell);
                    }
                }
            }
        }

        return nearby;
    }

    /**
     * Determines which grid cells an entity occupies based on its position and size.
     *
     * @param entity The game entity.
     * @return A list of grid cells occupied by the entity.
     */
    private List<GridCell> getOccupiedCells(GameEntity entity) {
        List<GridCell> cells = new ArrayList<>();

        float left = entity.getLocation().getX() - entity.getSize().getX() / 2;
        float right = entity.getLocation().getX() + entity.getSize().getX() / 2;
        float top = entity.getLocation().getY() - entity.getSize().getY() / 2;
        float bottom = entity.getLocation().getY() + entity.getSize().getY() / 2;

        int leftCell = Math.max(0, (int) Math.floor(left / cellSize));
        int rightCell = Math.min(getTotalCellsX() - 1, (int) Math.floor(right / cellSize));
        int topCell = Math.max(0, (int) Math.floor(top / cellSize));
        int bottomCell = Math.min(getTotalCellsY() - 1, (int) Math.floor(bottom / cellSize));

        for (int x = leftCell; x <= rightCell; x++) {
            for (int y = topCell; y <= bottomCell; y++) {
                cells.add(new GridCell(x, y));
            }
        }

        return cells;
    }

    /**
     * Calculates the total number of cells along the X-axis.
     *
     * @return Total cells in X direction.
     */
    private int getTotalCellsX() {
        return (int) Math.ceil((double) worldWidth / cellSize);
    }

    /**
     * Calculates the total number of cells along the Y-axis.
     *
     * @return Total cells in Y direction.
     */
    private int getTotalCellsY() {
        return (int) Math.ceil((double) worldHeight / cellSize);
    }
}
