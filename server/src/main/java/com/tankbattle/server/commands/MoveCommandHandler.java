package com.tankbattle.server.commands;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.commands.ICommand;
import com.tankbattle.server.commands.MoveCommand;

import java.util.List;
import java.util.Map;

public class MoveCommandHandler extends CommandHandler {
    @Override
    protected boolean canHandle(Map<String, Object> command) {
        return "MOVE".equals(command.get("type"));
    }

    @Override
    protected void processCommand(Map<String, Object> command, Player player, List<ICommand> commands, List<ICommand> commandsLog) {
        MoveCommand moveCommand = new MoveCommand(player.getTank(),
                ((Integer) command.get("direction")).byteValue());
        if (!commands.contains(moveCommand)) {
            commands.add(moveCommand);
            commandsLog.add(moveCommand);
            System.out.println("Move command added to commands");
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
