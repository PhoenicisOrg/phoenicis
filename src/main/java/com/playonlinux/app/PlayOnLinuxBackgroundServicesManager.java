package com.playonlinux.app;

import com.playonlinux.utils.BackgroundService;

import java.util.ArrayList;

public class PlayOnLinuxBackgroundServicesManager {
    ArrayList<BackgroundService> runningTask;

    PlayOnLinuxBackgroundServicesManager() {
        runningTask = new ArrayList<BackgroundService>();
    }

    void register(BackgroundService backgroundService) {
        runningTask.add(backgroundService);
        backgroundService.start();
    }

    void shutdown() {
        for(BackgroundService backgroundService: runningTask) {
            backgroundService.shutdown();
        }
    }
}
