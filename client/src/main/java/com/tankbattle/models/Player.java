package com.tankbattle.models;

import java.awt.Color;
import java.util.Objects;

import com.tankbattle.utils.Vector2;

public class Player extends Entity {
    protected String username;
    protected Vector2 location; // this is already the center of the player
    protected Vector2 size;
    protected Color outlineColor;
    protected Color fillColor;
    protected double rotationAngle = 0;

    public Player() {
    }

    public Player(String username, Vector2 location, Vector2 size, Color outlineColor,
                  Color fillColor) {
        this.username = username;
        this.location = location;
        this.size = size;
        this.outlineColor = outlineColor;
        this.fillColor = fillColor;
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

    public String toString() {
        return String.format("{ username: '%s', location: { x: %d, y: %d } }", this.username,
                (int) this.location.getX(),
                (int) this.location.getY());
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
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

    public PlayerType getPlayerType() {
        return PlayerType.OTHER_PLAYER;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
