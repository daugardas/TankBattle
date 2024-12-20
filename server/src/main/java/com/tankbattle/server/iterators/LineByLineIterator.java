package com.tankbattle.server.iterators;

import com.tankbattle.server.models.Level;

public class LineByLineIterator implements LevelTileIterator {
    private Level level;
    private int width;
    private int height;
    private int currentX = 0;
    private int currentY = 0;

    public LineByLineIterator(Level level) {
        setLevel(level);
    }

    public void setLevel(Level level) {
        this.level = level;
        if (level != null) {
            this.width = level.getWidth();
            this.height = level.getHeight();
        }
    }

    @Override
    public boolean hasNext() {
        return currentY < height && currentX < width;
    }

    @Override
    public TilePosition next() {
        if (!hasNext()) {
            return null;
        }

        TilePosition position = new TilePosition(currentX, currentY, level.getTile(currentX, currentY));
        
        currentX++;
        if (currentX >= width) {
            currentX = 0;
            currentY++;
        }

        return position;
    }

    @Override
    public void reset() {
        currentX = 0;
        currentY = 0;
    }
} 