package com.tankbattle.server.models.tanks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.AbstractCollidableEntity;
import com.tankbattle.server.models.tanks.weaponsystems.WeaponSystem;
import com.tankbattle.server.utils.Constants;
import com.tankbattle.server.utils.Vector2;

public abstract class Tank extends AbstractCollidableEntity implements ITank {
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

    @Override
    public WeaponSystem getWeaponSystem() {
        return weaponSystem;
    }

    @Override
    public void setWeaponSystem(WeaponSystem weaponSystem) {
        this.weaponSystem = weaponSystem;
    }

    @Override
    public Vector2 getSize() {
        return this.size;
    }

    @Override
    public void setSize(Vector2 size) {
        this.size = size;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public Vector2 getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Vector2 location) {
        this.location = location;
    }

    @Override
    public void setLocationToTile(Vector2 location) {
        Vector2 newLocation = new Vector2(location.getX() * 1000 + 500, location.getY() * 1000 + 500);
        this.setLocation(newLocation);
    }
    @Override

    public Vector2 getLookDirection() {
        return lookDirection;
    }

    @Override
    public void setLookDirection(Vector2 lookDirection) {
        this.lookDirection = lookDirection;
    }

    @Override
    public byte getMovementDirection() {
        return movementDirection;
    }

    @Override
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

    @Override
    public void updateLocation() {
        // Calculate intended movement
        float diagonalSpeed = getSpeed() / (float) Math.sqrt(2);
        float deltaY = 0;
        float deltaX = 0;

        if ((movementDirection & Constants.DIRECTION_UP) != 0) {
            deltaY -= (movementDirection & (Constants.DIRECTION_LEFT | Constants.DIRECTION_RIGHT)) != 0 ? diagonalSpeed : getSpeed();
        }

        if ((movementDirection & Constants.DIRECTION_DOWN) != 0) {
            deltaY += (movementDirection & (Constants.DIRECTION_LEFT | Constants.DIRECTION_RIGHT)) != 0 ? diagonalSpeed : getSpeed();
        }

        if ((movementDirection & Constants.DIRECTION_LEFT) != 0) {
            deltaX -= (movementDirection & (Constants.DIRECTION_UP | Constants.DIRECTION_DOWN)) != 0 ? diagonalSpeed : getSpeed();
        }

        if ((movementDirection & Constants.DIRECTION_RIGHT) != 0) {
            deltaX += (movementDirection & (Constants.DIRECTION_UP | Constants.DIRECTION_DOWN)) != 0 ? diagonalSpeed : getSpeed();
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

    @Override
    public void revertToPreviousPosition() {
        this.location = new Vector2(previousLocation.getX(), previousLocation.getY());
    }

    @JsonIgnore
    @Override
    public Tank getTank() {
        return this;
    }

}
