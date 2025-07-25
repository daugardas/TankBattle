package com.tankbattle.server.interpreter.expressions;

import com.tankbattle.server.interpreter.CommandContext;
import com.tankbattle.server.interpreter.CommandExpression;

public class HelpExpression implements CommandExpression {
    @Override
    public String getCommandDescription() {
        return """
                help - Prints available commands
                    help <command> - Prints specific command examples""";
    }

    @Override
    public boolean interpret(CommandContext ctx) {

        try {
            if (!"help".equals(ctx.nextToken())) {
                ctx.reset();
                return false;
            }

            if (ctx.hasMoreTokens()) {
                String commandExample = new CommandExampleExpression().interpret(ctx);

                if (commandExample != null)
                    ctx.getGameController().printToConsole(commandExample);
            } else {
                // user wants to print all available commands
                ctx.getGameController().printHelpToConsole();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(
                    "Encountered an error while interpreting first token in HelpExpression: " + e.getMessage());
        }

        return false;
    }

    @Override
    public String getCommandExample() {
        return """
                'help' examples:
                    `help` <= Prints available commands;
                    `help help` <= Prints this example you are looking at;
                    `help move` <= Prints 'move' command examples;
                """;
    }

}
