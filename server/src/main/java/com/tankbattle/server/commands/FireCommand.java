package com.tankbattle.server.commands;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.utils.Vector2;

public class FireCommand implements ICommand {
    private Player player;

    private GameController gameController;

    public FireCommand(Player player) {
        this.gameController = SpringContext.getBean(GameController.class);
        this.player = player;
    }

    @Override
    public void execute() {
        Bullet bullet = new Bullet(player.getLocation());
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
