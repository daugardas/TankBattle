package com.tankbattle.server.iterators;

import com.tankbattle.server.models.Level;

public interface LevelTileIterator {
    boolean hasNext();
    TilePosition next();
    void reset();
    void setLevel(Level level);
}