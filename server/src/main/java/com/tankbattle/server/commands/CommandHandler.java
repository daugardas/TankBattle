package com.tankbattle.server.commands;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.commands.ICommand;

import java.util.List;
import java.util.Map;

public abstract class CommandHandler {
    private static CommandHandler nextHandler;

    public void setNextHandler(CommandHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void handleCommand(Map<String, Object> command, Player player, List<ICommand> commands, List<ICommand> commandsLog) {
        if (nextHandler != null) {
            nextHandler.handleCommand(command, player, commands, commandsLog);
        }
    }
    public static CommandHandler getHandler() {
        return nextHandler;
    }

    protected abstract boolean canHandle(Map<String, Object> command);

    protected abstract void processCommand(Map<String, Object> command, Player player, List<ICommand> commands, List<ICommand> commandsLog);
}
