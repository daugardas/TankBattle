package com.tankbattle.server.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.SpatialGrid.GridNode;

public abstract class AbstractCollidableEntity implements GameEntity {
    @JsonIgnore
    private int queryId;
    @JsonIgnore
    private int[] cellIndicesMin;
    @JsonIgnore
    private int[] cellIndicesMax;
    @JsonIgnore
    private final List<GridNode> gridNodes = new ArrayList<>();
    @JsonIgnore
    private boolean isStaticEntity = false;
    @JsonIgnore
    private Set<String> occupiedCells = new HashSet<>();
    
    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public void setCellIndices(int[] minIndices, int[] maxIndices) {
        this.cellIndicesMin = minIndices;
        this.cellIndicesMax = maxIndices;
    }

    public int[] getCellIndicesMin() {
        return cellIndicesMin;
    }

    public int[] getCellIndicesMax() {
        return cellIndicesMax;
    }

    public void addGridNode(GridNode node) {
        gridNodes.add(node);
    }

    public List<GridNode> getGridNodes() {
        return gridNodes;
    }

    public void clearGridNodes() {
        gridNodes.clear();
    }

    public void setStaticEntity(boolean isStatic) {
        this.isStaticEntity = isStatic;
    }

    public boolean isStaticEntity() {
        return isStaticEntity;
    }

    public Set<String> getOccupiedCellKeys() {
       return new HashSet<>(occupiedCells);
    }

    public void setOccupiedCells(Set<String> occupiedCells) {
        this.occupiedCells = (occupiedCells != null) ? new HashSet<>(occupiedCells) : new HashSet<>();
    }
}

