package com.tankbattle.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpatialGrid {
    private int cellSize;
    private Map<String, List<Object>> grid;

    public SpatialGrid(int cellSize) {
        this.cellSize = cellSize;
        this.grid = new HashMap<>();
    }

    private String getCellKey(float x, float y) {
        int cellX = (int) (x / cellSize);
        int cellY = (int) (y / cellSize);
        return cellX + "," + cellY;
    }

    public void addEntity(Object entity, float x, float y) {
        String key = getCellKey(x, y);
        grid.computeIfAbsent(key, k -> new ArrayList<>()).add(entity);
    }

    public List<Object> getNearbyEntities(float x, float y) {
        String key = getCellKey(x, y);
        return grid.getOrDefault(key, new ArrayList<>());
    }

    public void clear() {
        grid.clear();
    }
}
