package com.tankbattle.commands;

public class MoveCommand implements ICommand {
    final String type = "MOVE";
    byte direction;

    public MoveCommand(byte direction){
        this.direction = direction;
    }

    public byte getDirection() {
        return direction;
    }

    public String getType() {
        return type;
    }
}
