package com.tankbattle.server.mediator;

import com.tankbattle.server.models.Player;

public interface ScoreMediator {
    void addScore(Player player, int points, String reason);
} 