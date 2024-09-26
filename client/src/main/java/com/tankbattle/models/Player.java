package com.tankbattle.models;

import com.tankbattle.utils.Vector2;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Objects;

public class Player extends Entity {
    protected String username;
    protected Color color;
    protected Vector2 size;

    public Player() {
        location = new Vector2(0, 0);
        color = Color.GREEN;
        size = new Vector2(10, 10);
    }

    public Player(String username) {
        this.username = username;
        location = new Vector2(0, 0);
        color = Color.GREEN;
        size = new Vector2(10, 10);
    }

    public Player(String username, Vector2 location) {
        this.username = username;
        this.location = location;
        color = Color.GREEN;
        size = new Vector2(10, 10);
    }

    public Player(String username, Vector2 location, Vector2 size) {
        this.username = username;
        this.location = location;
        color = Color.GREEN;
        this.size = size;
    }

    public Player(String username, Vector2 location, Vector2 size, Color color) {
        this.username = username;
        this.location = location;
        this.size = size;
        this.color = color;
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
        return String.format("{ username: '%s', location: { x: %d, y: %d } }", this.username, this.location.getX(),
                this.location.getY());
    }

    public int getCenterX() {
        return location.getX() - size.getY() / 2;
    }

    public int getCenterY(){
        return location.getY() - size.getY() / 2;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(getCenterX(), getCenterY(), size.getX(), size.getY());

        // a pixel to see the center of the player
        g.setColor(Color.ORANGE);
        g.fillRect(location.getX(), location.getY(), 2, 2);

        // draw username on top of player, centered
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int usernameWidth = metrics.stringWidth(username);

        int usernameX = location.getX() - usernameWidth / 2;
        int usernameY = getCenterY() - 2;

        g.setColor(Color.BLACK);
        g.drawString(username, usernameX, usernameY);
    }

    @Override
    public boolean equals(Object o) {
        Player player = (Player) o;
        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
