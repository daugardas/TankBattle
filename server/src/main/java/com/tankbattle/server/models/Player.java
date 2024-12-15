package com.tankbattle.server.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.models.tanks.LightTank;
import com.tankbattle.server.models.tanks.TankProxy;
import com.tankbattle.server.utils.Vector2;


public class Player {
    @JsonIgnore
    private String sessionId;

    private String username;
    private ITank tank;
    private Vector2 spawnLocation;

    public Player() {
        // Default constructor needed for JSON deserialization
    }

    public Player(String sessionId, String username, Vector2 spawnLocation) {
        this.sessionId = sessionId;
        this.username = username;
        this.spawnLocation = spawnLocation;

        // Create tank with spawn location
        tank = new TankProxy(new LightTank(), spawnLocation);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ITank getTank() {
        return tank;
    }

    public void setTank(ITank tank) {
        this.tank = tank;
    }

    public String toString() {
        return String.format("{ sessionId: '%s', username: '%s' }", this.sessionId, this.username);
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
