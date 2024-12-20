package com.tankbattle.server.managers;

import com.tankbattle.server.models.Player;

public interface ScoreManager {
    void addScore(Player player, int points, String reason);
} 