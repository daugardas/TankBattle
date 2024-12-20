package com.tankbattle.visitors;

import com.tankbattle.models.Bullet;
import com.tankbattle.models.Collision;
import com.tankbattle.models.Player;
import com.tankbattle.models.PowerUp;
import com.tankbattle.models.tiles.Tile;

public interface Visitor {
    void visit(Bullet bullet);
    void visit(Collision collision);
    void visit(Player player);
    void visit(PowerUp powerUp);
    void visit(Tile tile);
}