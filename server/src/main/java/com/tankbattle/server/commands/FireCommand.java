package com.tankbattle.server.commands;

import java.util.Objects;


import com.tankbattle.server.models.tanks.Tank;

public class FireCommand implements ICommand {
    private Tank tank;

    public FireCommand(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.getWeaponSystem().fire(tank.getLocation(), tank.getLookDirection());
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
        return tank.equals(that.tank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tank);
    }
}
