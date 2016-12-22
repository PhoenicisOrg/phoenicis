package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class BackgroundAppsManager implements AppsManager {
    private final AppsManager delegatedAppManager;
    private final ExecutorService executorService;

    public BackgroundAppsManager(AppsManager delegatedAppManager,
                                 ExecutorService executorService) {
        this.delegatedAppManager = delegatedAppManager;
        this.executorService = executorService;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        throw new UnsupportedOperationException("The background apps manager is asynchroneous");
    }

    @Override
    public void fetchInstallableApplications(Consumer<List<CategoryDTO>> callback, Consumer<Exception> errorCallback) {
        executorService.submit(() -> delegatedAppManager.fetchInstallableApplications(callback, errorCallback));
    }
}
