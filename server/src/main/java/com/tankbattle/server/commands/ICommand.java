package com.tankbattle.server.commands;

public interface ICommand {
    void execute();
    void undo();
}
