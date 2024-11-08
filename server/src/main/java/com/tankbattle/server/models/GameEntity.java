package com.tankbattle.server.models;

import com.tankbattle.server.utils.Vector2;

public interface GameEntity {
    Vector2 getLocation();
    Vector2 getSize();
}
