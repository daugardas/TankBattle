package com.tankbattle.server.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.SpatialGrid.GridNode;
import com.tankbattle.server.utils.Vector2;

public class PowerUp implements GameEntity, Cloneable {
    private Vector2 location;
    private Vector2 size;
    private PowerUpType type;

    @JsonIgnore
    private boolean consumed = false;

    public PowerUp(Vector2 location, PowerUpType type) {
        this.location = location;
        this.type = type;
        this.size = new Vector2(100, 100); // Example size
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public PowerUpType getType() {
        return type;
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        this.consumed = true;
    }

    //region Collision_stuff
    @JsonIgnore
    private int queryId;
    @JsonIgnore
    private int[] cellIndicesMin;
    @JsonIgnore
    private int[] cellIndicesMax;
    @JsonIgnore
    private List<GridNode> gridNodes = new ArrayList<>();
    @JsonIgnore
    private boolean isStaticEntity = true;
    @JsonIgnore
    private Set<String> occupiedCells = new HashSet<>();

    @Override
    public int getQueryId() {
        return queryId;
    }

    @Override
    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    @Override
    public void setCellIndices(int[] minIndices, int[] maxIndices) {
        this.cellIndicesMin = minIndices;
        this.cellIndicesMax = maxIndices;
    }

    @Override
    public int[] getCellIndicesMin() {
        return cellIndicesMin;
    }

    @Override
    public int[] getCellIndicesMax() {
        return cellIndicesMax;
    }

    @Override
    public void addGridNode(GridNode node) {
        gridNodes.add(node);
    }

    @Override
    public List<GridNode> getGridNodes() {
        return gridNodes;
    }

    @Override
    public void clearGridNodes() {
        gridNodes.clear();
    }

    @Override
    public void setStaticEntity(boolean isStatic) {
        this.isStaticEntity = isStatic;
    }

    @Override
    public boolean isStaticEntity() {
        return isStaticEntity;
    }

    @Override
    public Set<String> getOccupiedCellKeys() {
        return new HashSet<>(occupiedCells);
    }

    @Override
    public void setOccupiedCells(Set<String> occupiedCells) {
        this.occupiedCells = (occupiedCells != null) ? new HashSet<>(occupiedCells) : new HashSet<>();
    }

    // Deep copy implementation in clone()
    @Override
    public PowerUp clone() {
        try {
            PowerUp clonedPowerUp = (PowerUp) super.clone();

            // Deep copy for mutable fields
            clonedPowerUp.location = new Vector2(this.location.getX(), this.location.getY());
            clonedPowerUp.size = new Vector2(this.size.getX(), this.size.getY());

            // Deep copy cell indices arrays
            clonedPowerUp.cellIndicesMin = this.cellIndicesMin != null ? this.cellIndicesMin.clone() : null;
            clonedPowerUp.cellIndicesMax = this.cellIndicesMax != null ? this.cellIndicesMax.clone() : null;

            // Deep copy for gridNodes list
            clonedPowerUp.gridNodes = new ArrayList<>(this.gridNodes);

            // Deep copy for occupiedCells set
            clonedPowerUp.occupiedCells = new HashSet<>(this.occupiedCells);

            return clonedPowerUp;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning failed", e);
        }
    }

    //endregion
}
