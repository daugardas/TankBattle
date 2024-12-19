package com.tankbattle.server.memento;

import com.tankbattle.server.models.tiles.Tile;

public class LevelMemento {
    private Tile[][] grid;

    public LevelMemento(Tile[][] grid) {
        // Deep copy the grid to ensure independence of the memento
        this.grid = new Tile[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            this.grid[i] = grid[i].clone();
        }
    }

    // Getter for the grid state
    public Tile[][] getGrid() {
        Tile[][] copiedGrid = new Tile[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copiedGrid[i] = grid[i].clone();
        }
        return copiedGrid;
    }

    // Setter for the grid state
    public void setState(Tile[][] grid) {
        this.grid = new Tile[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            this.grid[i] = grid[i].clone();
        }
    }
}

