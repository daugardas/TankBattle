package com.tankbattle.server.interpreter.expressions;

import com.tankbattle.server.interpreter.CommandContext;

public class CoordinateExpression implements NonTerminalCommandExpression {

    @Override
    public Float interpret(CommandContext ctx) {
        String numString = ctx.nextToken();
        return Float.parseFloat(numString);
    }

}
