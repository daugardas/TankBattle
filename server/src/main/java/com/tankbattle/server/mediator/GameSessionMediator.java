package com.tankbattle.server.mediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.managers.ScoreManager;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.TileEntity;
import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.tanks.ITank;

@Component
public class GameSessionMediator implements GameMediator {
    private static final Logger logger = LoggerFactory.getLogger(GameSessionMediator.class);

    private final GameController gameController;
    private final ScoreManager scoreManager;

    @Autowired
    public GameSessionMediator(@Lazy GameController gameController, ScoreManager scoreManager) {
        this.gameController = gameController;
        this.scoreManager = scoreManager;
    }

    @Override
    public void handleCollision(CollisionEvent event) {
        switch (event.getType()) {
            case BULLET_MAP:
                handleBulletMapCollision(event);
                break;
            case PLAYER_POWERUP:
                handleTankPowerUpCollision(event);
                break;
            case PLAYER_PLAYER:
                handleTankTankCollision(event);
                break;
            case PLAYER_BULLET:
                handleTankBulletCollision(event);
                break;
            default:
                logger.warn("Unhandled collision type: {}", event.getType());
                break;
        }
    }

    private void handleBulletMapCollision(CollisionEvent event) {
        Bullet bullet = (Bullet) event.getFirstEntity();
        TileEntity tile = (TileEntity) event.getOtherEntity();
        Player shooter = bullet.getShooter();

        tile.takeDamage(bullet.getDamage());
        if (tile.getHealth() <= 0) {
            updateTileToGround(tile);
            if (shooter != null) {
                awardPoints(shooter, 10, "destroying a tile");
            }
        }

        int collisionX = (bullet.getLocation().getX() + tile.getLocation().getX()) / 2;
        int collisionY = (bullet.getLocation().getY() + tile.getLocation().getY()) / 2;

        removeBullet(bullet);
        sendCollisionLocation(collisionX, collisionY);
    }

    private void handleTankPowerUpCollision(CollisionEvent event) {
        ITank tank = event.getTank();
        PowerUp powerUp = (PowerUp) event.getOtherEntity();
        
        Player collector = gameController.getPlayers().stream()
            .filter(p -> p.getTank() == tank)
            .findFirst()
            .orElse(null);

        if (collector != null) {
            awardPoints(collector, 5, "collecting power-up");
        }

        removePowerUp(powerUp);
        powerUp.applyEffect(tank);
    }

    private void handleTankTankCollision(CollisionEvent event) {
        ITank tank1 = event.getTank();
        ITank tank2 = (ITank) event.getOtherEntity();

        tank1.revertToPreviousPosition();
        tank2.revertToPreviousPosition();

        int collisionX = (tank1.getLocation().getX() + tank2.getLocation().getX()) / 2;
        int collisionY = (tank1.getLocation().getY() + tank2.getLocation().getY()) / 2;

        sendCollisionLocation(collisionX, collisionY);
    }

    private void handleTankBulletCollision(CollisionEvent event) {
        ITank tank = event.getTank();
        Bullet bullet = (Bullet) event.getOtherEntity();
        Player shooter = bullet.getShooter();

        bullet.markForRemoval();
        tank.takeDamage(bullet.getDamage());

        if (shooter != null && tank != shooter.getTank()) {
            awardPoints(shooter, 10, "hitting enemy tank");
        }

        int collisionX = (tank.getLocation().getX() + bullet.getLocation().getX()) / 2;
        int collisionY = (tank.getLocation().getY() + bullet.getLocation().getY()) / 2;

        removeBullet(bullet);
        sendCollisionLocation(collisionX, collisionY);
    }

    @Override
    public void removeBullet(Bullet bullet) {
        gameController.removeCollidedBullet(bullet);
    }

    @Override
    public void removePowerUp(PowerUp powerUp) {
        gameController.removePowerUp(powerUp);
    }

    @Override
    public void updateTileToGround(TileEntity tile) {
        gameController.updateLevelTileToGround(tile);
    }

    @Override
    public void sendCollisionLocation(int x, int y) {
        gameController.sendCollisionLocation(x, y);
    }

    @Override
    public void awardPoints(Player player, int points, String reason) {
        scoreManager.addScore(player, points, reason);
    }
} 