package com.tankbattle.models;

import com.tankbattle.utils.Vector2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Objects;

public class Player extends Entity {
    protected String username;
    protected int size = 30;

    public Player() {
        this.location = new Vector2(0, 0);
    }

    public Player(String username) {
        this.username = username;
        this.location = new Vector2(0, 0);
    }

    public Player(String username, Vector2 location) {
        this.username = username;
        this.location = location;
    }

    public Vector2 getLocation() {
        return this.location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return String.format("{ username: '%s', location: { x: %d, y: %d } }", this.username, this.location.x,
                this.location.y);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(location.x, location.y, size, size);
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
