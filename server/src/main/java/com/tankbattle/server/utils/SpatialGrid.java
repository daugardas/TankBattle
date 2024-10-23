package com.tankbattle.server.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.tankbattle.server.models.GameEntity;

public class SpatialGrid {
    private final int cellSize;
    private final int totalCellsX;
    private final int totalCellsY;
    private final GridNode[][] grid;
    private int queryId = 0;

    public static class GridNode {
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

    public void addEntity(GameEntity entity, boolean isStatic) {
        int[] cellIndicesMin = getCellIndices(entity, true);
        int[] cellIndicesMax = getCellIndices(entity, false);
        insertEntityInCells(entity, cellIndicesMin, cellIndicesMax);

        entity.setCellIndices(cellIndicesMin, cellIndicesMax);
        entity.setStaticEntity(isStatic);
    }

    public void updateEntity(GameEntity entity) {
        if (entity.isStaticEntity()) return;  // Static entities do not move
    
        int[] oldCellIndicesMin = entity.getCellIndicesMin();
        int[] oldCellIndicesMax = entity.getCellIndicesMax();
    
        int[] newCellIndicesMin = getCellIndices(entity, true);
        int[] newCellIndicesMax = getCellIndices(entity, false);
    
        if (!cellIndicesEqual(oldCellIndicesMin, newCellIndicesMin) ||
            !cellIndicesEqual(oldCellIndicesMax, newCellIndicesMax)) {
            
            removeEntityFromCells(entity, oldCellIndicesMin, oldCellIndicesMax);
            
            insertEntityInCells(entity, newCellIndicesMin, newCellIndicesMax);
    
            entity.setCellIndices(newCellIndicesMin, newCellIndicesMax);
        }
    }
    

    public List<GameEntity> getNearbyEntities(GameEntity entity) {
        int[] cellIndicesMin = getCellIndices(entity, true);
        int[] cellIndicesMax = getCellIndices(entity, false);

        Set<GameEntity> nearbyEntities = new HashSet<>();
        queryId++;

        for (int x = cellIndicesMin[0]; x <= cellIndicesMax[0]; x++) {
            for (int y = cellIndicesMin[1]; y <= cellIndicesMax[1]; y++) {
                if (x >= 0 && x < totalCellsX && y >= 0 && y < totalCellsY) {
                    GridNode node = grid[x][y];
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

        nearbyEntities.remove(entity);

        return new LinkedList<>(nearbyEntities);
    }

    private int[] getCellIndices(GameEntity entity, boolean isMin) {
        float x = entity.getLocation().getX();
        float y = entity.getLocation().getY();
        float halfWidth = entity.getSize().getX() / 2;
        float halfHeight = entity.getSize().getY() / 2;

        float left = x - halfWidth;
        float right = x + halfWidth;
        float top = y - halfHeight;
        float bottom = y + halfHeight;

        int cellX = (int) ((isMin ? left : right) / cellSize);
        int cellY = (int) ((isMin ? top : bottom) / cellSize);

        cellX = Math.max(0, Math.min(totalCellsX - 1, cellX));
        cellY = Math.max(0, Math.min(totalCellsY - 1, cellY));

        return new int[]{cellX, cellY};
    }

    private void insertEntityInCells(GameEntity entity, int[] minIndices, int[] maxIndices) {
        for (int x = minIndices[0]; x <= maxIndices[0]; x++) {
            for (int y = minIndices[1]; y <= maxIndices[1]; y++) {
                System.out.println("Inserting player into grid cell: " + x + "," + y);
    
                GridNode newNode = new GridNode(entity);
                newNode.next = grid[x][y];
                if (grid[x][y] != null) {
                    grid[x][y].prev = newNode;
                }
                grid[x][y] = newNode;
    
                entity.addGridNode(newNode);
            }
        }
    }
    

    private void removeEntityFromCells(GameEntity entity, int[] minIndices, int[] maxIndices) {
        for (GridNode node : entity.getGridNodes()) {
            int[] indices = getNodeIndices(node);
            System.out.println("Removing player from grid cell: " + indices[0] + "," + indices[1]);
    
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                grid[indices[0]][indices[1]] = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
        entity.clearGridNodes();
    }
    
    
    

    public void removeEntity(GameEntity entity) {
        int[] cellIndicesMin = entity.getCellIndicesMin();
        int[] cellIndicesMax = entity.getCellIndicesMax();
    
        if (cellIndicesMin != null && cellIndicesMax != null) {
            removeEntityFromCells(entity, cellIndicesMin, cellIndicesMax);
        }
    
        entity.setCellIndices(null, null);
        entity.clearGridNodes();
    }
    

    private boolean cellIndicesEqual(int[] indices1, int[] indices2) {
        return indices1[0] == indices2[0] && indices1[1] == indices2[1];
    }

    private int[] getNodeIndices(GridNode node) {
        GameEntity entity = node.entity;
    
        int[] cellIndicesMin = getCellIndices(entity, true);

        return cellIndicesMin; 
    }
    
}
