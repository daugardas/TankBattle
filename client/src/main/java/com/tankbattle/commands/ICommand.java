package com.tankbattle.commands;

public interface ICommand {
    default void execute() {
    }

    default void undo() {
    }
}
