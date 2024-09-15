package com.tankbattle.server.models;

import jakarta.websocket.Session;

public class Player {
    private String sessionId;
    private int id;
    private String username;
    private Coordinate coord;

    public Player(String sessionId, int id, String username) {
        this.sessionId = sessionId;
        this.id = id;
        this.username = username;
        this.coord = new Coordinate(0, 0);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinate getCoordinates() {
        return this.coord;
    }

    public void setCoordinates(int x, int y) {
        this.coord.x = x;
        this.coord.y = y;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return String.format("{id: %d, username: '%s', coords: { x: %d, y: %d } }", this.id, this.username, this.coord.x, this.coord.y);
    }
}
