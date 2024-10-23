package com.tankbattle.server.utils;

import java.util.LinkedList;
import java.util.List;

import com.tankbattle.server.models.GameEntity;

public class SpatialGrid {
    private final int cellSize;
    private final int totalCellsX;
    private final int totalCellsY;
    private final GridNode[][] grid;
    private int queryId = 0;  // To track unique query IDs

    // GridNode class for linked list implementation in each cell
    private static class GridNode {
        GameEntity entity;
        GridNode next;
        GridNode prev;

        GridNode(GameEntity entity) {
            this.entity = entity;
        }
    }

    public SpatialGrid(int cellSize, int worldWidth, int worldHeight) {
        this.cellSize = cellSize;
        this.totalCellsX = (int) Math.ceil((double) worldWidth / cellSize);
        this.totalCellsY = (int) Math.ceil((double) worldHeight / cellSize);
        this.grid = new GridNode[totalCellsX][totalCellsY];
    }

    public void clear() {
        for (int x = 0; x < totalCellsX; x++) {
            for (int y = 0; y < totalCellsY; y++) {
                grid[x][y] = null;
            }
        }
    }

    public void addEntity(GameEntity entity) {
        int[] cellIndices = getCellIndices(entity);
        insertEntityInCell(entity, cellIndices[0], cellIndices[1]);
    }

    public void updateEntity(GameEntity entity, int oldX, int oldY) {
        int[] newCellIndices = getCellIndices(entity);
        if (oldX != newCellIndices[0] || oldY != newCellIndices[1]) {
            removeEntityFromCell(entity, oldX, oldY);
            insertEntityInCell(entity, newCellIndices[0], newCellIndices[1]);
        }
    }

    public List<GameEntity> getNearbyEntities(GameEntity entity) {
        int[] cellIndices = getCellIndices(entity);
        List<GameEntity> nearbyEntities = new LinkedList<>();

        queryId++;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int adjacentX = cellIndices[0] + dx;
                int adjacentY = cellIndices[1] + dy;

                if (adjacentX >= 0 && adjacentX < totalCellsX && adjacentY >= 0 && adjacentY < totalCellsY) {
                    GridNode node = grid[adjacentX][adjacentY];
                    while (node != null) {
                        GameEntity nearbyEntity = node.entity;

                        if (nearbyEntity.getQueryId() != queryId) {
                            nearbyEntity.setQueryId(queryId);
                            nearbyEntities.add(nearbyEntity);
                        }

                        node = node.next;
                    }
                }
            }
        }

        return nearbyEntities;
    }

    private int[] getCellIndices(GameEntity entity) {
        float x = entity.getLocation().getX();
        float y = entity.getLocation().getY();
        int cellX = Math.max(0, Math.min(totalCellsX - 1, (int) (x / cellSize)));
        int cellY = Math.max(0, Math.min(totalCellsY - 1, (int) (y / cellSize)));
        return new int[]{cellX, cellY};
    }

    private void insertEntityInCell(GameEntity entity, int x, int y) {
        GridNode newNode = new GridNode(entity);
        newNode.next = grid[x][y];
        if (grid[x][y] != null) {
            grid[x][y].prev = newNode;
        }
        grid[x][y] = newNode;
    }

    private void removeEntityFromCell(GameEntity entity, int x, int y) {
        GridNode node = grid[x][y];
        while (node != null && node.entity != entity) {
            node = node.next;
        }
        if (node == null) return;  // Entity not found in the cell

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            grid[x][y] = node.next;  // Node was the head of the list
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
    }
}
