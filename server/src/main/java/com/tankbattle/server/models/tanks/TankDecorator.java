package com.tankbattle.server.models.tanks;

import java.util.List;
import java.util.Set;

import com.tankbattle.server.models.tanks.weaponsystems.WeaponSystem;
import com.tankbattle.server.utils.SpatialGrid.GridNode;
import com.tankbattle.server.utils.Vector2;

public abstract class TankDecorator implements ITank {
    protected ITank decoratedTank;

    public TankDecorator(ITank decoratedTank) {
        this.decoratedTank = decoratedTank;
    }

    @Override
    public Tank getTank() {
        return decoratedTank.getTank();
    }

    @Override
    public WeaponSystem getWeaponSystem() {
        return decoratedTank.getWeaponSystem();
    }

    @Override
    public void setWeaponSystem(WeaponSystem weaponSystem) {
        decoratedTank.setWeaponSystem(weaponSystem);
    }

    @Override
    public Vector2 getSize() {
        return decoratedTank.getSize();
    }

    @Override
    public void setSize(Vector2 size) {
        decoratedTank.setSize(size);
    }

    @Override
    public int getHealth() {
        return decoratedTank.getHealth();
    }

    @Override
    public void setHealth(int health) {
        decoratedTank.setHealth(health);
    }

    @Override
    public int getSpeed() {
        return decoratedTank.getSpeed();
    }

    @Override
    public void setSpeed(int speed) {
        decoratedTank.setSpeed(speed);
    }

    @Override
    public void takeDamage(int damage) {
        decoratedTank.takeDamage(damage);
    }

    @Override
    public Vector2 getLocation() {
        return decoratedTank.getLocation();
    }

    @Override
    public void setLocation(Vector2 location) {
        decoratedTank.setLocation(location);
    }

    @Override
    public void setLocationToTile(Vector2 location) {
        decoratedTank.setLocationToTile(location);
    }

    @Override
    public Vector2 getLookDirection() {
        return decoratedTank.getLookDirection();
    }

    @Override
    public void setLookDirection(Vector2 lookDirection) {
        decoratedTank.setLookDirection(lookDirection);
    }

    @Override
    public byte getMovementDirection() {
        return decoratedTank.getMovementDirection();
    }

    @Override
    public void setMovementDirection(byte movementDirection) {
        decoratedTank.setMovementDirection(movementDirection);
    }

    @Override
    public void updateLocation() {
        decoratedTank.updateLocation();
    }

    @Override
    public void revertToPreviousPosition() {
        decoratedTank.revertToPreviousPosition();
    }


    // ICollidableEntity methods delegation
    @Override
    public int getQueryId() {
        return decoratedTank.getQueryId();
    }

    @Override
    public void setQueryId(int queryId) {
        decoratedTank.setQueryId(queryId);
    }

    @Override
    public void setCellIndices(int[] minIndices, int[] maxIndices) {
        decoratedTank.setCellIndices(minIndices, maxIndices);
    }

    @Override
    public int[] getCellIndicesMin() {
        return decoratedTank.getCellIndicesMin();
    }

    @Override
    public int[] getCellIndicesMax() {
        return decoratedTank.getCellIndicesMax();
    }

    @Override
    public void addGridNode(GridNode node) {
        decoratedTank.addGridNode(node);
    }

    @Override
    public List<GridNode> getGridNodes() {
        return decoratedTank.getGridNodes();
    }

    @Override
    public void clearGridNodes() {
        decoratedTank.clearGridNodes();
    }

    @Override
    public void setStaticEntity(boolean isStatic) {
        decoratedTank.setStaticEntity(isStatic);
    }

    @Override
    public boolean isStaticEntity() {
        return decoratedTank.isStaticEntity();
    }

    @Override
    public Set<String> getOccupiedCellKeys() {
        return decoratedTank.getOccupiedCellKeys();
    }

    @Override
    public void setOccupiedCells(Set<String> occupiedCells) {
        decoratedTank.setOccupiedCells(occupiedCells);
    }
}


