
package com.tankbattle.commands;

public class FireCommand implements ICommand {
    final String type = "FIRE";

    public FireCommand() {
    }

    public String getType() {
        return type;
    }
}
