package com.tankbattle.models;

import java.util.Objects;

import com.tankbattle.models.tanks.Tank;

public class Player extends Entity {
    protected String username;
    protected Tank tank;
    protected int score;

    public Player() {
    }

    public Player(String username) {
        this.username = username;
        this.tank = new Tank();
        this.score = 0;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toString() {
        return String.format("{ username: '%s' }", this.username) + tank.toString();
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
