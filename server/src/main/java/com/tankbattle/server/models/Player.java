package com.tankbattle.server.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.utils.Vector2;

public class Player {
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
    private double rotationAngle = 0;
    private static final byte DIRECTION_UP = 0b1000;
    private static final byte DIRECTION_LEFT = 0b0100;
    private static final byte DIRECTION_DOWN = 0b0010;
    private static final byte DIRECTION_RIGHT = 0b0001;

    public Player() {
        location = new Vector2(0, 0);
        movementDirection = 0;
        size = new Vector2(800, 800);

        this.gameController = SpringContext.getBean(GameController.class);
    }

    public Player(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        location = new Vector2(0, 0);
        size = new Vector2(800, 800);
        movementDirection = 0;
        this.gameController = SpringContext.getBean(GameController.class);
    }

    public Player(String sessionId, String username, int x, int y) {
        this.sessionId = sessionId;
        this.username = username;
        location = new Vector2(x, y);
        size = new Vector2(800, 800);
        movementDirection = 0;
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

    public double getRotationAngle() {
        return rotationAngle;
    }

    private void updateRotationAngle() {
        switch (movementDirection) {
            case DIRECTION_UP:
                rotationAngle = 0;
                break;
            case DIRECTION_LEFT:
                rotationAngle = 270;
                break;
            case DIRECTION_DOWN:
                rotationAngle = 180;
                break;
            case DIRECTION_RIGHT:
                rotationAngle = 90;
                break;
            case DIRECTION_UP | DIRECTION_RIGHT:
                rotationAngle = 45;
                break;
            case DIRECTION_UP | DIRECTION_LEFT:
                rotationAngle = 315;
                break;
            case DIRECTION_DOWN | DIRECTION_LEFT:
                rotationAngle = 225;
                break;
            case DIRECTION_DOWN | DIRECTION_RIGHT:
                rotationAngle = 135;
                break;
            default:
                break;
        }
    }

    public void updateLocation() {
        float diagonalSpeed = speed / (float) Math.sqrt(2);
        float deltaY = 0;
        float deltaX = 0;
    
        if ((movementDirection & DIRECTION_UP) != 0) {
            deltaY -= (movementDirection & (DIRECTION_LEFT | DIRECTION_RIGHT)) != 0 ? diagonalSpeed : speed;
        }
    
        if ((movementDirection & DIRECTION_DOWN) != 0) {
            deltaY += (movementDirection & (DIRECTION_LEFT | DIRECTION_RIGHT)) != 0 ? diagonalSpeed : speed;
        }
    
        if ((movementDirection & DIRECTION_LEFT) != 0) {
            deltaX -= (movementDirection & (DIRECTION_UP | DIRECTION_DOWN)) != 0 ? diagonalSpeed : speed;
        }
    
        if ((movementDirection & DIRECTION_RIGHT) != 0) {
            deltaX += (movementDirection & (DIRECTION_UP | DIRECTION_DOWN)) != 0 ? diagonalSpeed : speed;
        }
    
        // If the player is trying to move
        if ((movementDirection & 0b1111) != 0) {
            float newX = location.getX() + deltaX;
            float newY = location.getY() + deltaY;
    
            if (!checkWorldBorderConstraints(newX, newY) && !checkTileCollision(newX, newY)) {
                location.setX(newX);
                location.setY(newY);
            }
        }
    
        updateRotationAngle();
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
    

    //complete abomination, needs to be reworked from the ground up
    private boolean checkTileCollision(float newX, float newY) {
        return gameController.getCollisionManager().checkTileCollisionAtPosition(this, newX, newY, gameController.getLevel().getGrid());
    }
    
    public String toString() {
        return String.format("{ sessionId: '%s', username: '%s', location: { x: %d, y: %d } }", this.sessionId,
                this.username, this.location.getX(), this.location.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;
        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
