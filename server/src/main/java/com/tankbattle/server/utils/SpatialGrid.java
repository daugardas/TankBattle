package com.tankbattle.server.utils;

import java.util.HashSet;
import java.util.Iterator;
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
        int xIndex;
        int yIndex;
    
        GridNode(GameEntity entity, int xIndex, int yIndex) {
            this.entity = entity;
            this.xIndex = xIndex;
            this.yIndex = yIndex;
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
    
        Set<String> cellsToAdd = new HashSet<>();
        for (int x = cellIndicesMin[0]; x <= cellIndicesMax[0]; x++) {
            for (int y = cellIndicesMin[1]; y <= cellIndicesMax[1]; y++) {
                cellsToAdd.add(x + "," + y);
            }
        }
    
        insertEntityInCells(entity, cellsToAdd);
    
        entity.setOccupiedCells(cellsToAdd);
        entity.setCellIndices(cellIndicesMin, cellIndicesMax);
        entity.setStaticEntity(isStatic);
    }
    

    public void updateEntity(GameEntity entity) {
        if (entity.isStaticEntity()) return;
    
        Set<String> oldCells = entity.getOccupiedCellKeys(); // Returns set of "x,y" strings for occupied cells
    
        int[] newCellIndicesMin = getCellIndices(entity, true);
        int[] newCellIndicesMax = getCellIndices(entity, false);
    
        Set<String> newCells = new HashSet<>();
        for (int x = newCellIndicesMin[0]; x <= newCellIndicesMax[0]; x++) {
            for (int y = newCellIndicesMin[1]; y <= newCellIndicesMax[1]; y++) {
                newCells.add(x + "," + y);
            }
        }
    
        if (oldCells.equals(newCells)) {
            return; // Entity hasn't moved to new cells
        }
    
        Set<String> cellsToRemove = new HashSet<>(oldCells);
        cellsToRemove.removeAll(newCells);
    
        Set<String> cellsToAdd = new HashSet<>(newCells);
        cellsToAdd.removeAll(oldCells);
    
        removeEntityFromCells(entity, cellsToRemove);
        insertEntityInCells(entity, cellsToAdd);
    
        entity.setOccupiedCells(newCells); // Update the entity's occupied cells
        entity.setCellIndices(newCellIndicesMin, newCellIndicesMax);
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

    private void insertEntityInCells(GameEntity entity, Set<String> cellsToAdd) {
        for (String cellKey : cellsToAdd) {
            String[] indices = cellKey.split(",");
            int x = Integer.parseInt(indices[0]);
            int y = Integer.parseInt(indices[1]);
    
            System.out.println("Inserting player into grid cell: " + x + "," + y);
    
            GridNode newNode = new GridNode(entity, x, y);
            newNode.next = grid[x][y];
            if (grid[x][y] != null) {
                grid[x][y].prev = newNode;
            }
            grid[x][y] = newNode;
    
            entity.addGridNode(newNode);
        }
    }
    
    
    

    private void removeEntityFromCells(GameEntity entity, Set<String> cellsToRemove) {
        Iterator<GridNode> iterator = entity.getGridNodes().iterator();
        while (iterator.hasNext()) {
            GridNode node = iterator.next();
            String nodeKey = node.xIndex + "," + node.yIndex;
    
            if (cellsToRemove.contains(nodeKey)) {
                System.out.println("Removing player from grid cell: " + node.xIndex + "," + node.yIndex);
    
                if (node.prev != null) {
                    node.prev.next = node.next;
                } else {
                    grid[node.xIndex][node.yIndex] = node.next;
                }
                if (node.next != null) {
                    node.next.prev = node.prev;
                }
                iterator.remove();
            }
        }
    }
    
    
    public void removeEntity(GameEntity entity) {
        Set<String> cellsToRemove = entity.getOccupiedCellKeys();
    
        if (cellsToRemove != null && !cellsToRemove.isEmpty()) {
            removeEntityFromCells(entity, cellsToRemove);
        }
    
        entity.setOccupiedCells(null);
        entity.setCellIndices(null, null);
        entity.clearGridNodes();
    }
    
}