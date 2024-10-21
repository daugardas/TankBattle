package com.tankbattle.server.listeners;

import com.tankbattle.server.events.CollisionEvent;

public interface CollisionListener {
    void onCollision(CollisionEvent event);
}
