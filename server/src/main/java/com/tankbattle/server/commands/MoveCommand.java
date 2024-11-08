package com.tankbattle.server.commands;

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
        player.updateLocation();
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
        player.updateLocation();
    }
}
