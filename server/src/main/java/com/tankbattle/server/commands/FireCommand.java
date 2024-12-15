package com.tankbattle.server.commands;

import java.util.Objects;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.tanks.ITank;

public class FireCommand implements ICommand {
    private ITank tank;
    private Player player;

    public FireCommand(ITank tank, Player player) {
        this.tank = tank;
        this.player = player;
    }

    @Override
    public void execute() {
        tank.getWeaponSystem().fire(tank.getLocation(), tank.getLookDirection(), player);
    }

    @Override
    public void undo() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FireCommand that = (FireCommand) obj;
        return tank.equals(that.tank) && player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tank, player);
    }
}
