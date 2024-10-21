package com.tankbattle.server.events;

import org.springframework.context.ApplicationEvent;

public class CollisionLocationEvent extends ApplicationEvent {
    private final int x;
    private final int y;

    public CollisionLocationEvent(Object source, int x, int y) {
        super(source);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
