package com.tankbattle.server.models;

import java.util.List;
import java.util.Set;

import com.tankbattle.server.utils.SpatialGrid;

public interface CollidableEntity extends GameEntity {
    int getQueryId();
    void setQueryId(int queryId);
    void setCellIndices(int[] minIndices, int[] maxIndices);
    int[] getCellIndicesMin();
    int[] getCellIndicesMax();
    void addGridNode(SpatialGrid.GridNode node);
    List<SpatialGrid.GridNode> getGridNodes();
    void clearGridNodes();
    void setStaticEntity(boolean isStatic);
    boolean isStaticEntity();
    Set<String> getOccupiedCellKeys();
    void setOccupiedCells(Set<String> occupiedCells);
}