package com.tankbattle.server.iterators;

import com.tankbattle.server.models.Level;

public class SpiralIterator implements LevelTileIterator {
    private Level level;
    private int width;
    private int height;
    private int currentX;
    private int currentY;
    private int layer = 0;
    private int direction = 0; // 0: right, 1: down, 2: left, 3: up
    private int stepSize = 1;  // Add step size to create gaps in the spiral

    public SpiralIterator(Level level) {
        setLevel(level);
    }

    public void setLevel(Level level) {
        this.level = level;
        if (level != null) {
            this.width = level.getWidth();
            this.height = level.getHeight();
            reset();
        }
    }

    @Override
    public boolean hasNext() {
        return layer * 2 < Math.min(width, height);
    }

    @Override
    public TilePosition next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more tiles to iterate over");
        }

        TilePosition current = new TilePosition(currentX, currentY, level.getTile(currentX, currentY));
        
        // Move to next position with larger steps to create gaps
        switch (direction) {
            case 0: // right
                if (currentX < width - 1 - layer) {
                    currentX += stepSize;
                    if (currentX >= width - 1 - layer) {
                        currentX = width - 1 - layer;
                        direction = 1;
                    }
                } else direction = 1;
                break;
            case 1: // down
                if (currentY < height - 1 - layer) {
                    currentY += stepSize;
                    if (currentY >= height - 1 - layer) {
                        currentY = height - 1 - layer;
                        direction = 2;
                    }
                } else direction = 2;
                break;
            case 2: // left
                if (currentX > layer) {
                    currentX -= stepSize;
                    if (currentX <= layer) {
                        currentX = layer;
                        direction = 3;
                    }
                } else direction = 3;
                break;
            case 3: // up
                if (currentY > layer + 1) {
                    currentY -= stepSize;
                    if (currentY <= layer + 1) {
                        currentY = layer + 1;
                        direction = 0;
                        layer++;
                        currentX = layer;
                        currentY = layer;
                        // Increase step size as we move outward
                        stepSize = Math.min(3, 1 + layer / 5);
                    }
                } else {
                    direction = 0;
                    layer++;
                    currentX = layer;
                    currentY = layer;
                    stepSize = Math.min(3, 1 + layer / 5);
                }
                break;
        }

        return current;
    }

    @Override
    public void reset() {
        currentX = width / 2;
        currentY = height / 2;
        layer = 0;
        direction = 0;
        stepSize = 1;
    }
} 