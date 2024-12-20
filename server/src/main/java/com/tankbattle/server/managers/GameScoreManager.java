package com.tankbattle.server.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tankbattle.server.models.Player;

@Component
public class GameScoreManager implements ScoreManager {
    private static final Logger logger = LoggerFactory.getLogger(GameScoreManager.class);

    @Override
    public void addScore(Player player, int points, String reason) {
        player.addScore(points);
        logger.info("Player '{}' earned {} points for {}", player.getUsername(), points, reason);
    }
} 