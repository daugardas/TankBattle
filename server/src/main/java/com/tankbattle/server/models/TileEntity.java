package com.tankbattle.server.models;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.Vector2;

public class TileEntity extends AbstractCollidableEntity implements GameEntity {
    private Tile tile;
    private int gridX;
    private int gridY;

    public TileEntity(Tile tile, int gridX, int gridY) {
        this.tile = tile;
        this.gridX = gridX;
        this.gridY = gridY;
        setStaticEntity(true);
    }

    @Override
    public Vector2 getLocation() {
        float x = gridX * GameController.TILE_WIDTH + GameController.TILE_WIDTH / 2.0f;
        float y = gridY * GameController.TILE_HEIGHT + GameController.TILE_HEIGHT / 2.0f;
        return new Vector2(x, y);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(GameController.TILE_WIDTH, GameController.TILE_HEIGHT);
    }

    public Tile getTile() {
        return tile;
    }

    public boolean canPass() {
        return tile.canPass();
    }

    public boolean canProjectilePass() {
        return tile.canProjectilePass();
    }

    public void takeDamage(int damage) {
        tile.takeDamage(damage);
    }
    
}
