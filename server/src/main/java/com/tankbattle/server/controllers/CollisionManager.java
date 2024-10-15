package com.tankbattle.server.controllers;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.Vector2;

public class CollisionManager {

    private static final int TILE_SIZE = 1000;

    private boolean checkAABBCollision(Vector2 pos1, Vector2 size1, Vector2 pos2, Vector2 size2) {
        float left1 = pos1.getX() - size1.getX() / 2;
        float right1 = pos1.getX() + size1.getX() / 2;
        float top1 = pos1.getY() - size1.getY() / 2;
        float bottom1 = pos1.getY() + size1.getY() / 2;

        float left2 = pos2.getX() - size2.getX() / 2;
        float right2 = pos2.getX() + size2.getX() / 2;
        float top2 = pos2.getY() - size2.getY() / 2;
        float bottom2 = pos2.getY() + size2.getY() / 2;

        return !(left1 >= right2 || right1 <= left2 || top1 >= bottom2 || bottom1 <= top2);
    }

    public boolean checkTileCollision(Player player, Tile[][] tiles) {
        Vector2 playerPos = player.getLocation();
        Vector2 playerSize = player.getSize();

        int leftTile = (int) Math.floor((playerPos.getX() - playerSize.getX() / 2) / TILE_SIZE);
        int rightTile = (int) Math.floor((playerPos.getX() + playerSize.getX() / 2) / TILE_SIZE);
        int topTile = (int) Math.floor((playerPos.getY() - playerSize.getY() / 2) / TILE_SIZE);
        int bottomTile = (int) Math.floor((playerPos.getY() + playerSize.getY() / 2) / TILE_SIZE);

        System.out.println("Player at " + playerPos + " overlaps tiles: " + leftTile + "," + topTile + " to " + rightTile + "," + bottomTile);

        for (int y = topTile; y <= bottomTile; y++) {
            for (int x = leftTile; x <= rightTile; x++) {
                if (y >= 0 && x >= 0 && y < tiles.length && x < tiles[0].length) {
                    Tile tile = tiles[y][x];
                    if (!tile.canPass()) {
                        Vector2 tilePos = new Vector2(x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2);
                        Vector2 tileSize = new Vector2(TILE_SIZE, TILE_SIZE);
                        System.out.println("Checking collision with tile at " + x + "," + y + " (" + tile.getType() + ")");

                        if (checkAABBCollision(playerPos, playerSize, tilePos, tileSize)) {
                            System.out.println("Collision detected with tile at " + x + "," + y);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    //complete abomination, needs to be reworked from the ground up
    public boolean checkTileCollisionAtPosition(Player player, float newX, float newY, Tile[][] tiles) {
        Vector2 playerSize = player.getSize();
    
        int leftTile = (int) Math.floor((newX - playerSize.getX() / 2) / TILE_SIZE);
        int rightTile = (int) Math.floor((newX + playerSize.getX() / 2) / TILE_SIZE);
        int topTile = (int) Math.floor((newY - playerSize.getY() / 2) / TILE_SIZE);
        int bottomTile = (int) Math.floor((newY + playerSize.getY() / 2) / TILE_SIZE);
    
        for (int y = topTile; y <= bottomTile; y++) {
            for (int x = leftTile; x <= rightTile; x++) {
                if (y >= 0 && x >= 0 && y < tiles.length && x < tiles[0].length) {
                    Tile tile = tiles[y][x];
                    if (!tile.canPass()) {
                        Vector2 tilePos = new Vector2(x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2);
                        Vector2 tileSize = new Vector2(TILE_SIZE, TILE_SIZE);
    
                        Vector2 playerPos = new Vector2(newX, newY);
    
                        if (checkAABBCollision(playerPos, playerSize, tilePos, tileSize)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    

    public void handlePlayerTileCollision(Player player) {
        player.revertToPreviousPosition();
    }
}
