package com.tankbattle.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tankbattle.models.Collision;
import com.tankbattle.utils.Vector2;

public class CollisionManager {
    private final ExecutorService colllisionExecutorService = Executors.newSingleThreadExecutor();

    private static final CollisionManager INSTANCE = new CollisionManager();

    private final Set<Vector2> activeCollisionLocations = new HashSet<>();
    private final List<Collision> collisions = new ArrayList<>();
    private final List<Collision> explosions = new CopyOnWriteArrayList<>();
    private static final int EXPLOSION_DURATION = 1000; // 1 second

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

    public void addExplosion(Vector2 location) {
        Collision explosion = new Collision(location);
        explosions.add(explosion);
        
        // Remove explosion after duration
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                explosions.remove(explosion);
            }
        }, EXPLOSION_DURATION);
    }

    public List<Collision> getExplosions() {
        return explosions;
    }
}
