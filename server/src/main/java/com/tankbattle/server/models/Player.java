package com.tankbattle.server.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.utils.Vector2;

public class Player extends AbstractCollidableEntity {
    @JsonIgnore
    private GameController gameController;

    private String sessionId;
    private String username;

    // Player location is a coordinate system where every 1000 units is a new tile,
    // ensuring smooth transition between tiles
    private Vector2 location;
    private Vector2 size;
    private byte movementDirection;
    private float speed = 40;
    private Vector2 lookDirection;

    @JsonIgnore
    private int health = 20;

    private static final byte DIRECTION_UP = 0b1000;
    private static final byte DIRECTION_LEFT = 0b0100;
    private static final byte DIRECTION_DOWN = 0b0010;
    private static final byte DIRECTION_RIGHT = 0b0001;

    @JsonIgnore
    private Vector2 previousLocation; // To store previous position for collision response

    public Player() {
        location = new Vector2(0, 0);
        previousLocation = new Vector2(0, 0);
        lookDirection = new Vector2(0, 0);
        movementDirection = 0;
        size = new Vector2(800, 800);

        this.gameController = SpringContext.getBean(GameController.class);
    }

    public Player(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;

        location = new Vector2(0, 0);
        previousLocation = new Vector2(0, 0);
        lookDirection = new Vector2(0, 0);
        movementDirection = 0;
        size = new Vector2(800, 800);

        this.gameController = SpringContext.getBean(GameController.class);
    }

    public Player(String sessionId, String username, int x, int y) {
        this.sessionId = sessionId;
        this.username = username;

        location = new Vector2(x, y);
        previousLocation = new Vector2(x, y);
        lookDirection = new Vector2(0, 0);
        movementDirection = 0;

        size = new Vector2(800, 800);

        this.gameController = SpringContext.getBean(GameController.class);
    }

    @JsonIgnore
    public byte getMovementDirection() {
        return movementDirection;
    }

    @JsonIgnore
    public void setMovementDirection(byte movementDirection) {
        this.movementDirection = movementDirection;
    }

    @JsonIgnore
    public String getSessionId() {
        return this.sessionId;
    }

    @JsonIgnore
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Vector2 getLocation() {
        return this.location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    @JsonIgnore
    public void setLocationToTile(Vector2 location) {
        Vector2 newLocation = new Vector2(location.getX() * 1000 + 500, location.getY() * 1000 + 500);
        this.setLocation(newLocation);
    }

    public Vector2 getSize() {
        return this.size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Vector2 getLookDirection() {
        return lookDirection;
    }

    @JsonIgnore
    public int getHealth() {
        return health;
    }

    @JsonIgnore
    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    private void updateLookDirection() {
        switch (movementDirection) {
            case DIRECTION_UP:
                lookDirection.set(0, -1);
                break;
            case DIRECTION_LEFT:
                lookDirection.set(-1, 0);
                break;
            case DIRECTION_DOWN:
                lookDirection.set(0, 1);
                break;
            case DIRECTION_RIGHT:
                lookDirection.set(1, 0);
                break;
            case DIRECTION_UP | DIRECTION_RIGHT:
                lookDirection.set(1, -1);
                break;
            case DIRECTION_UP | DIRECTION_LEFT:
                lookDirection.set(-1, -1);
                break;
            case DIRECTION_DOWN | DIRECTION_LEFT:
                lookDirection.set(-1, 1);
                break;
            case DIRECTION_DOWN | DIRECTION_RIGHT:
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

        boolean movingVertically = false;
        boolean movingHorizontally = false;

        if ((movementDirection & DIRECTION_UP) != 0) {
            movingVertically = true;
            deltaY -= (movementDirection & (DIRECTION_LEFT | DIRECTION_RIGHT)) != 0 ? diagonalSpeed : speed;
        }

        if ((movementDirection & DIRECTION_DOWN) != 0) {
            movingVertically = true;
            deltaY += (movementDirection & (DIRECTION_LEFT | DIRECTION_RIGHT)) != 0 ? diagonalSpeed : speed;
        }

        if ((movementDirection & DIRECTION_LEFT) != 0) {
            movingHorizontally = true;
            deltaX -= (movementDirection & (DIRECTION_UP | DIRECTION_DOWN)) != 0 ? diagonalSpeed : speed;
        }

        if ((movementDirection & DIRECTION_RIGHT) != 0) {
            movingHorizontally = true;
            deltaX += (movementDirection & (DIRECTION_UP | DIRECTION_DOWN)) != 0 ? diagonalSpeed : speed;
        }

        // Calculate intended new position
        float newX = location.getX() + deltaX;
        float newY = location.getY() + deltaY;
        Vector2 newPosition = new Vector2(newX, newY);

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

        return leftCornerX < 0 ||
                rightCornerX > gameController.getLevelCoordinateWidth() ||
                topCornerY < 0 ||
                bottomCornerY > gameController.getLevelCoordinateHeight();
    }

    public void revertToPreviousPosition() {
        this.location = new Vector2(previousLocation.getX(), previousLocation.getY());
    }

    public String toString() {
        return String.format("{ sessionId: '%s', username: '%s', location: { x: %d, y: %d }}", this.sessionId,
                this.username, this.location.getX(), this.location.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Player player = (Player) o;
        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public GameController getGameController() {
        return this.gameController;
    }
}
