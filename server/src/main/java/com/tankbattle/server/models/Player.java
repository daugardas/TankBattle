package com.tankbattle.server.models;

import jakarta.websocket.Session;

import java.util.Objects;

public class Player {
    //    private String sessionId;
    private String uuid;
    private String username;
    private Coordinate coord;

//    public Player(String sessionId, int id, String username) {
////        this.sessionId = sessionId;
//        this.id = id;
//        this.username = username;
//        this.coord = new Coordinate(0, 0);
//    }

    public Player() {
        this.coord = new Coordinate(0, 0);
    }

    public Player(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.coord = new Coordinate(0, 0);
    }

    public Player(String uuid, String username, int x, int y) {
        this.uuid = uuid;
        this.username = username;
        this.coord = new Coordinate(x, y);
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Coordinate getCoord() {
        return this.coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return String.format("{uuid: '%s', username: '%s', coord: { x: %d, y: %d } }", this.uuid, this.username, this.coord.x, this.coord.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return uuid.equals(player.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
