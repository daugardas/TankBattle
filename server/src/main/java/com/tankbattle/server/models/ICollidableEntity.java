package com.tankbattle.server.models;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.SpatialGrid;

public interface ICollidableEntity  extends GameEntity {
    @JsonIgnore
    int getQueryId();
    @JsonIgnore
    void setQueryId(int queryId);
    @JsonIgnore
    void setCellIndices(int[] minIndices, int[] maxIndices);
    @JsonIgnore
    int[] getCellIndicesMin();
    @JsonIgnore
    int[] getCellIndicesMax();
    @JsonIgnore
    void addGridNode(SpatialGrid.GridNode node);
    @JsonIgnore
    List<SpatialGrid.GridNode> getGridNodes();
    @JsonIgnore
    void clearGridNodes();
    @JsonIgnore
    void setStaticEntity(boolean isStatic);
    @JsonIgnore
    boolean isStaticEntity();
    @JsonIgnore
    Set<String> getOccupiedCellKeys();
    @JsonIgnore
    void setOccupiedCells(Set<String> occupiedCells);
}
