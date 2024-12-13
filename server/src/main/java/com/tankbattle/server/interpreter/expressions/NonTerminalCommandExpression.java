package com.tankbattle.server.interpreter.expressions;

import com.tankbattle.server.interpreter.CommandContext;

public interface NonTerminalCommandExpression {
    Object interpret(CommandContext ctx);
}
