package com.tankbattle.server.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.models.tanks.HeavyTank;
import com.tankbattle.server.models.tanks.Tank;

public class Player {
    @JsonIgnore
    private String sessionId;

    private String username;
    private Tank tank;

    public Player() {
        tank = new HeavyTank();
    }

    public Player(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;

        tank = new HeavyTank();
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

    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
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
