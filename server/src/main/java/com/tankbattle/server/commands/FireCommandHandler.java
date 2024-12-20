package com.tankbattle.server.commands;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.commands.ICommand;

import java.util.List;
import java.util.Map;

public class FireCommandHandler extends CommandHandler {
    @Override
    protected boolean canHandle(Map<String, Object> command) {
        return "FIRE".equals(command.get("type"));
    }

    @Override
    protected void processCommand(Map<String, Object> command, Player player, List<ICommand> commands, List<ICommand> commandsLog) {
        FireCommand fireCommand = new FireCommand(player.getTank(), player);
        if (!commands.contains(fireCommand)) {
            commands.add(fireCommand);
            System.out.println("Fire command added to commands");
        }
    }

    @Override
    public void handleCommand(Map<String, Object> command, Player player, List<ICommand> commands, List<ICommand> commandsLog) {
        CommandHandler nextHandler = getHandler();
        if (canHandle(command)) {
            processCommand(command, player, commands, commandsLog);
        } else if (nextHandler != null) {
            nextHandler.handleCommand(command, player, commands, commandsLog);
        }
    }
}
