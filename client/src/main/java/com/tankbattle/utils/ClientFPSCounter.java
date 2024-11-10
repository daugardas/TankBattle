package com.tankbattle.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientFPSCounter {
    private static final ClientFPSCounter INSTANCE = new ClientFPSCounter();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private float fps;
    private float frameCount;
    private long fpsTimerStart;

    public static ClientFPSCounter getInstance() {
        return INSTANCE;
    }

    public void start() {
        this.fps = 0.0f;
        this.frameCount = 0;
        this.fpsTimerStart = System.currentTimeMillis();

        executorService.scheduleAtFixedRate(this::updateFps, 0, 1, TimeUnit.SECONDS);
    }

    public synchronized void incrementFrameCount() {
        frameCount++;
    }

    public void updateFps() {
            long currentTime = System.currentTimeMillis();
            fps = (frameCount * 1000.0f) / (currentTime - fpsTimerStart);
            fpsTimerStart = currentTime;
            frameCount = 0;
    }

    public synchronized float getFps() {
        return fps;
    }
}
