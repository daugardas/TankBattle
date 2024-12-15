package com.tankbattle.server.models.tanks;

import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.AbstractCollidableEntity;
import com.tankbattle.server.models.tanks.weaponsystems.WeaponSystem;
import com.tankbattle.server.utils.Vector2;

public class TankProxy extends AbstractCollidableEntity implements ITank {
    private ITank realTank;
    private static final int MAX_HEALTH = 100;
    private int armor = 0;
    private boolean isDestroyed = false;

    public TankProxy(ITank tank) {
        this.realTank = tank;
    }

    @Override
    public void takeDamage(int damage) {
        if (isDestroyed) return;
        
        // Calculate actual damage after armor reduction
        int actualDamage = Math.max(1, damage - armor);
        System.out.println("Tank taking " + actualDamage + " damage (reduced from " + damage + " by armor)");
        
        int newHealth = realTank.getHealth() - actualDamage;
        setHealth(newHealth);
    }

    @Override
    public void setHealth(int health) {
        if (isDestroyed) return;

        // Validate health changes
        if (health > MAX_HEALTH) {
            System.out.println("Warning: Attempted to set health above maximum. Capping at " + MAX_HEALTH);
            realTank.setHealth(MAX_HEALTH);
        } else if (health <= 0) {
            System.out.println("Tank destroyed!");
            realTank.setHealth(0);
            isDestroyed = true;
            onTankDestroyed();
        } else {
            System.out.println("Tank health changed from " + realTank.getHealth() + " to " + health);
            realTank.setHealth(health);
        }
    }

    private void onTankDestroyed() {
        setSpeed(0);
        // Notify about tank destruction
        GameController gameController = SpringContext.getBean(GameController.class);
        gameController.notifyTankDestroyed(getLocation());
    }

    public void setArmor(int armor) {
        this.armor = Math.max(0, armor);
        System.out.println("Tank armor set to " + this.armor);
    }

    public int getArmor() {
        return armor;
    }

    @Override
    public Tank getTank() {
        return realTank.getTank();
    }

    @Override
    public Vector2 getLocation() {
        return realTank.getLocation();
    }

    @Override
    public void setLocation(Vector2 location) {
        realTank.setLocation(location);
    }

    @Override
    public void setLocationToTile(Vector2 tile) {
        realTank.setLocationToTile(tile);
    }

    @Override
    public void updateLocation() {
        realTank.updateLocation();
    }

    @Override
    public Vector2 getSize() {
        return realTank.getSize();
    }

    @Override
    public void setMovementDirection(byte direction) {
        realTank.setMovementDirection(direction);
    }

    @Override
    public byte getMovementDirection() {
        return realTank.getMovementDirection();
    }

    @Override
    public Vector2 getLookDirection() {
        return realTank.getLookDirection();
    }

    @Override
    public void setLookDirection(Vector2 direction) {
        realTank.setLookDirection(direction);
    }

    @Override
    public int getSpeed() {
        return realTank.getSpeed();
    }

    @Override
    public void setSpeed(int speed) {
        realTank.setSpeed(speed);
    }

    @Override
    public WeaponSystem getWeaponSystem() {
        return realTank.getWeaponSystem();
    }

    @Override
    public void setWeaponSystem(WeaponSystem weaponSystem) {
        realTank.setWeaponSystem(weaponSystem);
    }

    @Override
    public void setSize(Vector2 size) {
        realTank.setSize(size);
    }

    @Override
    public void revertToPreviousPosition() {
        realTank.revertToPreviousPosition();
    }

    @Override
    public int getHealth() {
        return realTank.getHealth();
    }
} 