package com.tankbattle.server.interpreter;

import java.util.ArrayList;
import java.util.List;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.interpreter.expressions.HelpExpression;
import com.tankbattle.server.interpreter.expressions.KickPlayerExpression;
import com.tankbattle.server.interpreter.expressions.ListPlayersExpression;
import com.tankbattle.server.interpreter.expressions.MovePlayerExpression;
import com.tankbattle.server.interpreter.expressions.SpawnRandomPowerUpExpression;

public class CommandInterpreter {
    private List<CommandExpression> expressions;

    public CommandInterpreter() {
        expressions = new ArrayList<>();
        expressions.add(new MovePlayerExpression());
        expressions.add(new HelpExpression());
        expressions.add(new KickPlayerExpression());
        // expressions.add(new ListPlayersExpression());
        // expressions.add(new SpawnRandomPowerUpExpression());
    }

    public void interpret(String input, GameController gameController) {
        CommandContext ctx = new CommandContext(input, gameController);

        boolean foundIntepretator = false;

        for (CommandExpression expr : expressions) {
            try {
                System.out.println(expr.getClass().getSimpleName() + " is trying to interpret '" + input + "'");
                System.out.println("nextToken() can be called for " + ctx.countTokens() + " more times");
                boolean interpreted = expr.interpret(ctx);
                if (interpreted) {
                    System.out.println(expr.getClass().getSimpleName() + " interpreted '" + input + "'");
                    foundIntepretator = true;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error interpreting command: " + e.getMessage());
            }
        }

        if (!foundIntepretator) {
            gameController.printToConsole("Command '" + input + "' not recognized.");
        }
    }

    public List<String> getCommandsList() {
        List<String> commands = new ArrayList<>();
        for (CommandExpression expression : expressions) {
            commands.add(expression.getCommandDescription());
        }

        return commands;
    }
}
