package com.tankbattle.server.commands;

import java.util.Objects;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.utils.Constants;

public class MoveCommand implements ICommand {
    private Player player;
    private byte direction;

    public MoveCommand(Player player, byte direction) {
        this.player = player;
        this.direction = direction;
    }

    @Override
    public void execute() {
        player.setMovementDirection(direction);
    }

    @Override
    public void undo() {
        byte reverseDirection = 0;

        if ((direction >> 3 << 3 & Constants.DIRECTION_UP) != 0) {
            reverseDirection |= Constants.DIRECTION_DOWN;
        }

        if ((direction >> 1 << 3 >> 2 & Constants.DIRECTION_DOWN) != 0) {
            reverseDirection |= Constants.DIRECTION_UP;
        }

        if ((direction << 1 >> 3 << 2 & Constants.DIRECTION_LEFT) != 0) {
            reverseDirection |= Constants.DIRECTION_RIGHT;
        }

        if ((direction << 3 >> 3 & Constants.DIRECTION_RIGHT) != 0) {
            reverseDirection |= Constants.DIRECTION_LEFT;
        }

        player.setMovementDirection(reverseDirection);
    }

    public String toString() {
        return String.format("%s %d", player.toString(), direction);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MoveCommand that = (MoveCommand) obj;
        return direction == that.direction && player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, direction);
    }
}
