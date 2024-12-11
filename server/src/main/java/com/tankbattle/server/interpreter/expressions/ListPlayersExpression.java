package com.tankbattle.server.interpreter.expressions;

import java.util.List;

import com.tankbattle.server.interpreter.CommandContext;
import com.tankbattle.server.interpreter.CommandExpression;
import com.tankbattle.server.models.Player;

public class ListPlayersExpression implements CommandExpression {

    @Override
    public String getCommandDescription() {
        return """
                list - Lists online players in Console window
                """;
    }

    @Override
    public boolean interpret(CommandContext ctx) {
        try {
            if(!"list".equals(ctx.nextToken())){
                ctx.reset();
                return false;
            }

            List<Player> players = ctx.getGameController().getPlayers();
            StringBuilder builder = new StringBuilder();

            builder.append("|---------------------|\n");
            builder.append("| Name       | Health |\n");
            builder.append("|---------------------|\n");
            for (Player player : players) {
                builder.append(String.format("| %-10s | %-6d |\n", player.getUsername(), player.getTank().getHealth()));
            }
            builder.append("|---------------------|\n");
            ctx.getGameController().printToConsole(builder.toString());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(
                    "Encountered an error while interpreting first token in ListPlayersExpression: " + e.getMessage());
        }

        return false;
    }

    @Override
    public String getCommandExample() {
        return """
                'list' - Lists online players with their name and health info
                """;
    }
    
}
