package com.tankbattle.server.models;

import java.util.List;
import java.util.Set;

import com.tankbattle.server.utils.SpatialGrid.GridNode;
import com.tankbattle.server.utils.Vector2;

public abstract class PlayerDecorator implements IPlayer, CollidableEntity {
    protected IPlayer decoratedPlayer;
    protected CollidableEntity collidableDecoratedPlayer;

    public PlayerDecorator(IPlayer player) {
        if (!(player instanceof CollidableEntity)) {
            throw new IllegalArgumentException("Decorated player must be a CollidableEntity");
        }
        this.decoratedPlayer = player;
        this.collidableDecoratedPlayer = (CollidableEntity) player;
    }

    // IPlayer methods

    @Override
    public void updateLocation() {
        decoratedPlayer.updateLocation();
    }

    @Override
    public void setMovementDirection(byte movementDirection) {
        decoratedPlayer.setMovementDirection(movementDirection);
    }

    @Override
    public byte getMovementDirection() {
        return decoratedPlayer.getMovementDirection();
    }

    @Override
    public void takeDamage(int damage) {
        decoratedPlayer.takeDamage(damage);
    }

    @Override
    public int getHealth() {
        return decoratedPlayer.getHealth();
    }

    @Override
    public void setHealth(int health) {
        decoratedPlayer.setHealth(health);
    }

    @Override
    public void setLocation(Vector2 location) {
        decoratedPlayer.setLocation(location);
    }

    @Override
    public void setSize(Vector2 size) {
        decoratedPlayer.setSize(size);
    }

    @Override
    public double getRotationAngle() {
        return decoratedPlayer.getRotationAngle();
    }

    @Override
    public void updateRotationAngle() {
        decoratedPlayer.updateRotationAngle();
    }

    @Override
    public String getSessionId() {
        return decoratedPlayer.getSessionId();
    }

    @Override
    public String getUsername() {
        return decoratedPlayer.getUsername();
    }

    @Override
    public void revertToPreviousPosition() {
        decoratedPlayer.revertToPreviousPosition();
    }

    @Override
    public Vector2 getLocation() {
        return decoratedPlayer.getLocation();
    }

    @Override
    public Vector2 getSize() {
        return decoratedPlayer.getSize();
    }

    // CollidableEntity methods

    @Override
    public int getQueryId() {
        return collidableDecoratedPlayer.getQueryId();
    }

    @Override
    public void setQueryId(int queryId) {
        collidableDecoratedPlayer.setQueryId(queryId);
    }

    @Override
    public void setCellIndices(int[] minIndices, int[] maxIndices) {
        collidableDecoratedPlayer.setCellIndices(minIndices, maxIndices);
    }

    @Override
    public int[] getCellIndicesMin() {
        return collidableDecoratedPlayer.getCellIndicesMin();
    }

    @Override
    public int[] getCellIndicesMax() {
        return collidableDecoratedPlayer.getCellIndicesMax();
    }

    @Override
    public void addGridNode(GridNode node) {
        collidableDecoratedPlayer.addGridNode(node);
    }

    @Override
    public List<GridNode> getGridNodes() {
        return collidableDecoratedPlayer.getGridNodes();
    }

    @Override
    public void clearGridNodes() {
        collidableDecoratedPlayer.clearGridNodes();
    }

    @Override
    public void setStaticEntity(boolean isStatic) {
        collidableDecoratedPlayer.setStaticEntity(isStatic);
    }

    @Override
    public boolean isStaticEntity() {
        return collidableDecoratedPlayer.isStaticEntity();
    }

    @Override
    public Set<String> getOccupiedCellKeys() {
        return collidableDecoratedPlayer.getOccupiedCellKeys();
    }

    @Override
    public void setOccupiedCells(Set<String> occupiedCells) {
        collidableDecoratedPlayer.setOccupiedCells(occupiedCells);
    }
}
