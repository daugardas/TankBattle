package com.tankbattle.server.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.Vector2;

public class Player {
    private String sessionId;
    private String username;
    private Vector2 location;
    private Vector2 size;
    private byte movementDirection;
    private float speed = 2;
    private double rotationAngle = 0;
    
    public Player() {
        location = new Vector2(0, 0);
        movementDirection = 0;
        size = new Vector2(21,21);
    }

    public Player(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        location = new Vector2(0, 0);
        size = new Vector2(21,21);
        movementDirection = 0;
    }

    public Player(String sessionId, String username, int x, int y) {
        this.sessionId = sessionId;
        this.username = username;
        location = new Vector2(x, y);
        size = new Vector2(21,21);
        movementDirection = 0;
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

    @JsonIgnore
    public int getCenterX() {
        return location.getX() - size.getX() / 2;
    }

    @JsonIgnore
    public int getCenterY(){
        return location.getY() - size.getY() / 2;
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
            case 0b1000: // Up
                rotationAngle = 270;
                break;
            case 0b0100: // Left
                rotationAngle = 180;
                break;
            case 0b0010: // Down
                rotationAngle = 90;
                break;
            case 0b0001: // Right
                rotationAngle = 0;
                break;
            case 0b1001: // Up + Right
                rotationAngle = 315;
                break;
            case 0b1100: // Up + Left
                rotationAngle = 225;
                break;
            case 0b0110: // Down + Left
                rotationAngle = 135;
                break;
            case 0b0011: // Down + Right
                rotationAngle = 45;
                break;
            default:
                break;
        }
    }

    public void updateLocation() {
        float diagonalSpeed = speed / (float) Math.sqrt(2);
        float deltaY = 0;
        float deltaX = 0;

        if ((movementDirection & 0b1000) != 0) {
            deltaY -= (movementDirection & 0b0101) != 0 ? diagonalSpeed : speed;
        }

        if ((movementDirection & 0b0010) != 0) {
            deltaY += (movementDirection & 0b0101) != 0 ? diagonalSpeed : speed;
        }

        if ((movementDirection & 0b0100) != 0) {
            deltaX -= (movementDirection & 0b1010) != 0 ? diagonalSpeed : speed;
        }

        if ((movementDirection & 0b0001) != 0) {
            deltaX += (movementDirection & 0b1010) != 0 ? diagonalSpeed : speed;
        }

        float newX = location.getX() + deltaX;
        float newY = location.getY() + deltaY;

        if(newX - size.getX() / 2 >= 0 && newX + size.getX() / 2 <= 800) {
            location.setX(newX);
        }

        if(newY - size.getY() / 2 >= 0 && newY + size.getY() / 2 <= 800) {
            location.setY(newY);
        }
        updateRotationAngle();
    }

    public String toString() {
        return String.format("{ sessionId: '%s', username: '%s', location: { x: %d, y: %d } }", this.sessionId,
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
}
