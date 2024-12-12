package com.tankbattle.server.interpreter.expressions;

import com.tankbattle.server.interpreter.CommandContext;

public class CommandExampleExpression implements NonTerminalCommandExpression {

    @Override
    public String interpret(CommandContext ctx) {
        String commandExample = null;
        try {
            String commandToGetExampleFor = ctx.nextToken();

            switch (commandToGetExampleFor) {
                case "help":
                    commandExample = new HelpExpression().getCommandExample();
                    break;
                case "move":
                    commandExample = new MovePlayerExpression().getCommandExample();
                    break;
                case "kick":
                    commandExample = new KickPlayerExpression().getCommandExample();
                    break;
                case "list":
                    commandExample = new ListPlayersExpression().getCommandExample();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(
                    "Encountered an error while interpreting first token in CommandExampleExpression: " + e.getMessage());
        }

        return commandExample;
    }

}
