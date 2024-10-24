package com.tankbattle.server.models;

import java.util.List;
import java.util.Set;

import com.tankbattle.server.utils.SpatialGrid.GridNode;
import com.tankbattle.server.utils.Vector2;

public interface GameEntity {
    Vector2 getLocation();
    Vector2 getSize();

    // Stuff for spatial grid management
    int getQueryId();
    void setQueryId(int queryId);

    void setCellIndices(int[] minIndices, int[] maxIndices);
    int[] getCellIndicesMin();
    int[] getCellIndicesMax();

    void addGridNode(GridNode node);
    List<GridNode> getGridNodes();
    void clearGridNodes();

    void setStaticEntity(boolean isStatic);
    boolean isStaticEntity();

    //Not sure if this helps or hurts the performance
    //It reduces incert and remove operations but replaces them with new checks
    Set<String> getOccupiedCellKeys();
    void setOccupiedCells(Set<String> occupiedCells);
}
