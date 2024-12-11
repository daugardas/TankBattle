package com.tankbattle.server.interpreter.expressions;

import javax.swing.SwingUtilities;

import com.tankbattle.server.interpreter.CommandContext;
import com.tankbattle.server.interpreter.CommandExpression;

public class KickPlayerExpression implements CommandExpression {

    @Override
    public String getCommandDescription() {
        return "kick <username> - Kick specified user from session";
    }

    @Override
    public boolean interpret(CommandContext ctx) {
        try {
            if (!"kick".equals(ctx.nextToken())) {
                ctx.reset();
                return false;
            }

            if (!ctx.hasMoreTokens()) {
                ctx.getGameController().printToConsole("Correct usage\n" + getCommandDescription());
                return true;
            }

            String username = ctx.nextToken();
            SwingUtilities.invokeLater(() -> ctx.getGameController().kickPlayer(username));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(
                    "Encountered an error while interpreting first token in KickPlayerExpression: " + e.getMessage());

        }

        return false;
    }

    @Override
    public String getCommandExample() {
        return """
                'kick' examples:
                    `kick user1` <= Kicks a player with the usename 'user1'
                """;
    }

}
