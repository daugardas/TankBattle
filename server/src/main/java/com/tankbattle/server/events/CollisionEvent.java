package com.tankbattle.server.events;

import com.tankbattle.server.models.IPlayer;

public class CollisionEvent {
    private final Object firstEntity;

    private CollisionType type;
    public CollisionEvent(CollisionType type, Object firstEntity, Object otherEntity) {
        this.type = type;
        this.firstEntity = firstEntity;
        this.otherEntity = otherEntity;
    }
    private final Object otherEntity; // Can be Player, Bullet, PowerUp, or Tile

    public Object getFirstEntity() {
        return firstEntity;
    }

    public CollisionType getType() {
        return type;
    }

    public IPlayer getPlayer() {
        return (IPlayer) firstEntity;
    }

    public enum CollisionType {
        PLAYER_MAP,
        PLAYER_PLAYER,
        PLAYER_BULLET, PLAYER_POWERUP, BULLET_MAP
    }

    public Object getOtherEntity() {
        return otherEntity;
    }
}
