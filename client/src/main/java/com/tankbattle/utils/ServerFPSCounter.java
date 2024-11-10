package com.tankbattle.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerFPSCounter {
    private static final ServerFPSCounter INSTANCE = new ServerFPSCounter();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private long serverUpdateCount;
    private float serverFps;
    private long serverFpsTimerStart;

    public static ServerFPSCounter getInstance() {
        return INSTANCE;
    }

    public void start() {
        this.serverUpdateCount = 0;
        this.serverFps = 0.0f;
        this.serverFpsTimerStart = System.currentTimeMillis();

        scheduler.scheduleAtFixedRate(this::updateServerFps, 0, 1, TimeUnit.SECONDS);
    }

    public synchronized void incrementServerUpdateCount() {
        serverUpdateCount++;
    }

    private synchronized void updateServerFps() {
        long currentTime = System.currentTimeMillis();
        serverFps = (serverUpdateCount * 1000.0f) / (currentTime - serverFpsTimerStart);
        serverFpsTimerStart = currentTime;
        serverUpdateCount = 0;
    }

    public float getServerFps() {
        return serverFps;
    }
}
