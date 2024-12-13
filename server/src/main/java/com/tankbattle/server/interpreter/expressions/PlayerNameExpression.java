package com.tankbattle.server.interpreter.expressions;

import java.util.List;

import com.tankbattle.server.interpreter.CommandContext;
import com.tankbattle.server.models.Player;

public class PlayerNameExpression implements NonTerminalCommandExpression {

    @Override
    public String interpret(CommandContext ctx) {
        try {
            String checkUsername = ctx.nextToken();
            List<Player> players =  ctx.getGameController().getPlayers();
            for (Player player : players) {
                if (player.getUsername().equals(checkUsername)) {
                    return checkUsername;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(
                    "Encountered an error while interpreting first token in KickPlayerExpression: " + e.getMessage());
        }

        return null;
    }
    
}
