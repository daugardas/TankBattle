package com.tankbattle.mediator;

import java.util.List;

import com.tankbattle.models.Player;
import com.tankbattle.views.ScoreBoard;

public interface ScoreMediator {
    void registerScoreBoard(ScoreBoard scoreBoard);
    void updateScores(List<Player> players);
    void notifyScoreChange();
} 