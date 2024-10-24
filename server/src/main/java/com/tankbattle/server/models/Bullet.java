package com.tankbattle.server.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.SpatialGrid.GridNode;
import com.tankbattle.server.utils.Vector2;

public class Bullet  implements GameEntity {
    private Vector2 location;
    private Vector2 size;
    private int damage;
    private Vector2 velocity;

    @JsonIgnore
    private boolean markedForRemoval = false;

    public Bullet(Vector2 location, Vector2 velocity, int damage) {
        this.location = location;
        this.velocity = velocity;
        this.damage = damage;
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

    public int getDamage() {
        return damage;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void markForRemoval() {
        this.markedForRemoval = true;
    }

    public void updatePosition() {
        this.location.addToX(velocity.getX());
        this.location.addToY(velocity.getY());
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
    private boolean isStaticEntity = false;
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
    
    //endregion
}
