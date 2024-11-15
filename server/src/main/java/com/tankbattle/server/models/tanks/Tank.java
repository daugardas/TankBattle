package com.tankbattle.server.models.tanks;

import com.tankbattle.server.utils.Constants;
import com.tankbattle.server.utils.Vector2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.AbstractCollidableEntity;
import com.tankbattle.server.models.tanks.weaponsystems.WeaponSystem;
import com.tankbattle.server.models.tanks.weaponsystems.Cannon;

public abstract class Tank extends AbstractCollidableEntity {
    @JsonIgnore
    protected WeaponSystem weaponSystem;

    protected Vector2 size;
    protected int health;

    @JsonIgnore
    protected int speed;

    protected Vector2 location;
    protected Vector2 lookDirection;
    protected byte movementDirection;

    @JsonIgnore
    protected byte previousDirection;

    @JsonIgnore
    private Vector2 previousLocation; // To store previous position for collision response


    public Tank(WeaponSystem weaponSystem, Vector2 size, int health, int speed) {
        this.weaponSystem = weaponSystem;
        this.size = size;
        this.health = health;
        this.speed = speed;

        location = new Vector2(0, 0);
        lookDirection = new Vector2(0, 0);
        movementDirection = 0;
        previousDirection = 0;
        previousLocation = new Vector2(0, 0);
    }

    public WeaponSystem getWeaponSystem() {
        return weaponSystem;
    }

    public void setWeaponSystem(WeaponSystem weaponSystem) {
        this.weaponSystem = weaponSystem;
    }

    public Vector2 getSize() {
        return this.size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public Vector2 getLocation() {
        return this.location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public void setLocationToTile(Vector2 location) {
        Vector2 newLocation = new Vector2(location.getX() * 1000 + 500, location.getY() * 1000 + 500);
        this.setLocation(newLocation);
    }

    public Vector2 getLookDirection() {
        return lookDirection;
    }

    public void setLookDirection(Vector2 lookDirection) {
        this.lookDirection = lookDirection;
    }

    public byte getMovementDirection() {
        return movementDirection;
    }

    public void setMovementDirection(byte movementDirection) {
        this.movementDirection = movementDirection;
    }

    private void updateLookDirection() {
        switch (movementDirection) {
            case Constants.DIRECTION_UP:
                lookDirection.set(0, -1);
                break;
            case Constants.DIRECTION_LEFT:
                lookDirection.set(-1, 0);
                break;
            case Constants.DIRECTION_DOWN:
                lookDirection.set(0, 1);
                break;
            case Constants.DIRECTION_RIGHT:
                lookDirection.set(1, 0);
                break;
            case Constants.DIRECTION_UP | Constants.DIRECTION_RIGHT:
                lookDirection.set(1, -1);
                break;
            case Constants.DIRECTION_UP | Constants.DIRECTION_LEFT:
                lookDirection.set(-1, -1);
                break;
            case Constants.DIRECTION_DOWN | Constants.DIRECTION_LEFT:
                lookDirection.set(-1, 1);
                break;
            case Constants.DIRECTION_DOWN | Constants.DIRECTION_RIGHT:
                lookDirection.set(1, 1);
                break;
            default:
                break;
        }
    }

    public void updateLocation() {
        // Calculate intended movement
        float diagonalSpeed = speed / (float) Math.sqrt(2);
        float deltaY = 0;
        float deltaX = 0;

        if ((movementDirection & Constants.DIRECTION_UP) != 0) {
            deltaY -= (movementDirection & (Constants.DIRECTION_LEFT | Constants.DIRECTION_RIGHT)) != 0 ? diagonalSpeed : speed;
        }

        if ((movementDirection & Constants.DIRECTION_DOWN) != 0) {
            deltaY += (movementDirection & (Constants.DIRECTION_LEFT | Constants.DIRECTION_RIGHT)) != 0 ? diagonalSpeed : speed;
        }

        if ((movementDirection & Constants.DIRECTION_LEFT) != 0) {
            deltaX -= (movementDirection & (Constants.DIRECTION_UP | Constants.DIRECTION_DOWN)) != 0 ? diagonalSpeed : speed;
        }

        if ((movementDirection & Constants.DIRECTION_RIGHT) != 0) {
            deltaX += (movementDirection & (Constants.DIRECTION_UP | Constants.DIRECTION_DOWN)) != 0 ? diagonalSpeed : speed;
        }

        // Calculate intended new position
        float newX = location.getX() + deltaX;
        float newY = location.getY() + deltaY;

        // Check for world border constraints
        if (checkWorldBorderConstraints(newX, newY)) {
            // Prevent movement beyond world boundaries
            return;
        }
        previousLocation = new Vector2(location.getX(), location.getY());
        location.setX(newX);
        location.setY(newY);


        updateLookDirection();
    }

    private boolean checkWorldBorderConstraints(float newX, float newY) {
        float leftCornerX = newX - size.getX() / 2;
        float rightCornerX = newX + size.getX() / 2;
        float topCornerY = newY - size.getY() / 2;
        float bottomCornerY = newY + size.getY() / 2;

        GameController gameController = SpringContext.getBean(GameController.class);

        return leftCornerX < 0 ||
                rightCornerX > gameController.getLevelCoordinateWidth() ||
                topCornerY < 0 ||
                bottomCornerY > gameController.getLevelCoordinateHeight();
    }

    public void revertToPreviousPosition() {
        this.location = new Vector2(previousLocation.getX(), previousLocation.getY());
    }

}
