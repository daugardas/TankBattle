package com.tankbattle.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tankbattle.models.Collision;
import com.tankbattle.utils.Vector2;

public class CollisionManager {
    private final ExecutorService colllisionExecutorService = Executors.newSingleThreadExecutor();

    private static final CollisionManager INSTANCE = new CollisionManager();

    private final Set<Vector2> activeCollisionLocations = new HashSet<>();
    private final List<Collision> collisions = new ArrayList<>();

    private CollisionManager() {
    }

    public static CollisionManager getInstance() {
        return INSTANCE;
    }

    public void addCollision(Vector2 location) {
        colllisionExecutorService.execute(() -> {
            Vector2 collisionLocation = new Vector2(location.getX(), location.getY());
            if (activeCollisionLocations.contains(collisionLocation)) {
                return;
            }
            activeCollisionLocations.add(collisionLocation);
            collisions.add(new Collision(collisionLocation));
        });
    }

    public List<Collision> getCollisions() {
        return collisions;
    }

    public void clearCollisions() {
        colllisionExecutorService.execute(() -> {
            collisions.clear();
            activeCollisionLocations.clear();
        });
    }

    public void shutdown() {
        colllisionExecutorService.shutdown();
    }
}
