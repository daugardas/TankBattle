package com.tankbattle.server.interpreter;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.tankbattle.server.controllers.GameController;

public class CommandContext {
    private StringTokenizer tokenizer;
    private GameController gameController;
    private String input;

    public CommandContext(String input, GameController gameController) throws NullPointerException {
        this.input = input;
        this.tokenizer = new StringTokenizer(input);
        this.gameController = gameController;
    }

    public String nextToken() throws NoSuchElementException {
        return this.tokenizer.nextToken();
    }

    public boolean hasMoreTokens() {
        return this.tokenizer.hasMoreTokens();
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public int countTokens() {
        return this.tokenizer.countTokens();
    }

    public String peekToken() {
        if(!this.tokenizer.hasMoreTokens())
            return null;

        String token = this.tokenizer.nextToken();
        this.reset();
        return token;
    }

    public void reset() {
        this.tokenizer = new StringTokenizer(input);
    }
}
