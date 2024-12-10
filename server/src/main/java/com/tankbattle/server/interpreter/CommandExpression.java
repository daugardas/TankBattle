package com.tankbattle.server.interpreter;

public interface CommandExpression {
    String getCommandDescription();

    // returns true, if the CommandExpression can interpret the given ctx,
    // returns false, if another CommandExpression should try interpreting
    boolean interpret(CommandContext ctx);

    String getCommandExample();
}
