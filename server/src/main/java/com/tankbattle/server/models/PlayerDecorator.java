package com.tankbattle.server.models;

public abstract class PlayerDecorator implements IPlayer {
    protected IPlayer decoratedPlayer;

    public PlayerDecorator(IPlayer player) {
        this.decoratedPlayer = player;
    }

    // Delegate methods to the decorated player
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

    // Implement other methods similarly
}
