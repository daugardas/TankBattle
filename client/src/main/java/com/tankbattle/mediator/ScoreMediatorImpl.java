package com.tankbattle.mediator;

import java.util.ArrayList;
import java.util.List;

import com.tankbattle.models.Player;
import com.tankbattle.views.ScoreBoard;

public class ScoreMediatorImpl implements ScoreMediator {
    private static final ScoreMediatorImpl INSTANCE = new ScoreMediatorImpl();
    private ScoreBoard scoreBoard;
    private List<Player> currentPlayers = new ArrayList<>();

    private ScoreMediatorImpl() {}

    public static ScoreMediatorImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    @Override
    public void updateScores(List<Player> players) {
        this.currentPlayers = players;
        notifyScoreChange();
    }

    @Override
    public void notifyScoreChange() {
        if (scoreBoard != null) {
            scoreBoard.updatePlayers(currentPlayers);
        }
    }
} 