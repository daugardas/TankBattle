package com.tankbattle.server.models;
import com.tankbattle.server.utils.*;

import jakarta.websocket.Session;

import java.util.Objects;

public class Player {
    private String sessionId;
    private String username;
    private Vector2 location;

    public Player() {
        this.location = new Vector2(0, 0);
    }

    public Player(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        this.location = new Vector2(0, 0);
    }

    public Player(String sessionId, String username, int x, int y) {
        this.sessionId = sessionId;
        this.username = username;
        this.location = new Vector2(x, y);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
        return String.format("{ sessionId: '%s', username: '%s', location: { x: %d, y: %d } }", this.sessionId, this.username, this.location.x, this.location.y);
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
