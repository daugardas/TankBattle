package com.tankbattle.server.interpreter.expressions;

import com.tankbattle.server.interpreter.CommandContext;
import com.tankbattle.server.interpreter.CommandExpression;

public class MovePlayerExpression implements CommandExpression {
    @Override
    public String getCommandDescription() {
        return "move <username> <x> <y> - Move a player by specified coordinates";
    }

    @Override
    public boolean interpret(CommandContext ctx) {
        try {
            String commandName = ctx.nextToken();
            if (!"move".equals(commandName)) {
                ctx.reset();
                return false;
            }

            // Move player command requires at least 2 more tokens to work
            if (ctx.countTokens() >= 2) {
                String username = new PlayerNameExpression().interpret(ctx);

                if (username == null) {
                    ctx.getGameController()
                            .printToConsole("Couldn't find online player with username '" + username + "'");
                    return true;
                }

                Float x = 0.0f;
                try {
                    x = new CoordinateExpression().interpret(ctx);
                } catch (NullPointerException e) {
                    ctx.getGameController().printToConsole("'x' value is not provided");
                    return true;
                } catch (NumberFormatException e) {
                    ctx.getGameController()
                            .printToConsole("Invalid 'x' value format provided. 'x' has to be a decimal number");
                    return true;
                }

                Float y = 0.0f;

                if (ctx.hasMoreTokens()) {

                    try {
                        y = new CoordinateExpression().interpret(ctx);
                    } catch (NullPointerException e) {
                        ctx.getGameController().printToConsole("'y' value is not provided");
                        return true;
                    } catch (NumberFormatException e) {
                        ctx.getGameController()
                                .printToConsole("Invalid 'y' value format provided. 'y' has to be a decimal number");
                        return true;
                    }
                }

                ctx.getGameController().movePlayer(username, x.floatValue(), y.floatValue());
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error when interpreting token: " + e.getMessage());
        }

        return false;
    }

    @Override
    public String getCommandExample() {
        return """
                'move' examples:
                    `move user1 20 20` <= moves a user named 'user1' 20 points right and 20 points down;
                    `move user2 20`    <= moves a user named 'user2' 20 points to the right;
                    `move user2 -20 0` <= moves a user named 'user2' 20 points left;
                """;
    }
}
