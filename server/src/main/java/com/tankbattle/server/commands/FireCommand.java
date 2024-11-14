package com.tankbattle.server.commands;

import java.util.Objects;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.IPlayer;
import com.tankbattle.server.utils.Vector2;

public class FireCommand implements ICommand {
    private IPlayer player;
    private GameController gameController;

    public FireCommand(GameController gameController, IPlayer player) {
        this.gameController = gameController;
        this.player = player;
    }

    @Override
    public void execute() {
        Bullet bullet = new Bullet(player.getLocation(), Vector2.convertByteToVector2(player.getMovementDirection()));
        gameController.addBullet(bullet);
    }

    @Override
    public void undo() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FireCommand that = (FireCommand) obj;
        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}
