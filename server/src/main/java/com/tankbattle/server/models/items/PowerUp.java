package com.tankbattle.server.models.items;

import com.tankbattle.server.models.AbstractCollidableEntity;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.utils.Vector2;

public abstract class PowerUp extends AbstractCollidableEntity{
    private Vector2 location;
    private Vector2 size;
    private PowerUpType type;

    public PowerUp(Vector2 location, PowerUpType type) {
        this.location = location;
        this.type = type;
        this.size = new Vector2(500, 500);
    }

    public abstract void applyEffect(ITank tank);

    @Override
    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    @Override
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

}
