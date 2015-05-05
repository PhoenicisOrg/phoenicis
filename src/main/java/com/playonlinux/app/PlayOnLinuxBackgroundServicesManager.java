package com.playonlinux.app;

import com.playonlinux.utils.BackgroundService;

import java.util.ArrayList;

public class PlayOnLinuxBackgroundServicesManager {
    ArrayList<BackgroundService> runningTask;

    PlayOnLinuxBackgroundServicesManager() {
        runningTask = new ArrayList<>();
    }

    void register(BackgroundService backgroundService) {
        runningTask.add(backgroundService);
        backgroundService.start();
    }

    void shutdown() {
        runningTask.forEach(com.playonlinux.utils.BackgroundService::shutdown);
    }
}
