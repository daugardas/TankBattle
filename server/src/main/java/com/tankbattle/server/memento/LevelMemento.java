package com.tankbattle.server.memento;

import com.tankbattle.server.models.tiles.Tile;

public class LevelMemento {
    private Tile[][] grid;

    public LevelMemento(Tile[][] grid) {
        this.grid = new Tile[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            this.grid[i] = grid[i].clone();
        }
    }

    public Tile[][] getGrid() {
        Tile[][] copiedGrid = new Tile[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copiedGrid[i] = grid[i].clone();
        }
        return copiedGrid;
    }

    public void setState(Tile[][] grid) {
        this.grid = new Tile[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            this.grid[i] = grid[i].clone();
        }
    }
}

