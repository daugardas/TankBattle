package com.tankbattle.server.models.tanks;

import com.tankbattle.server.models.ICollidableEntity;
import com.tankbattle.server.models.tanks.weaponsystems.WeaponSystem;
import com.tankbattle.server.utils.Vector2;

public interface ITank extends ICollidableEntity {
    WeaponSystem getWeaponSystem();
    void setWeaponSystem(WeaponSystem weaponSystem);

    Vector2 getSize();
    void setSize(Vector2 size);

    int getHealth();
    void setHealth(int health);

    int getSpeed();
    void setSpeed(int speed);

    void takeDamage(int damage);

    Vector2 getLocation();
    void setLocation(Vector2 location);
    void setLocationToTile(Vector2 location);

    Vector2 getLookDirection();
    void setLookDirection(Vector2 lookDirection);

    byte getMovementDirection();
    void setMovementDirection(byte movementDirection);

    void updateLocation();
    void revertToPreviousPosition();

    Tank getTank();
}

