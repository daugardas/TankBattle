package com.tankbattle.server.events;

import com.tankbattle.server.models.Player;

public class CollisionEvent {
    public enum CollisionType {
        PLAYER_MAP,
        PLAYER_PLAYER,
        PLAYER_BULLET,
        PLAYER_POWERUP
    }

    private CollisionType type;
    private Player player;
    private Object otherEntity; // Can be Player, Bullet, PowerUp, or Tile

    public CollisionEvent(CollisionType type, Player player, Object otherEntity) {
        this.type = type;
        this.player = player;
        this.otherEntity = otherEntity;
    }

    public CollisionType getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public Object getOtherEntity() {
        return otherEntity;
    }
}
