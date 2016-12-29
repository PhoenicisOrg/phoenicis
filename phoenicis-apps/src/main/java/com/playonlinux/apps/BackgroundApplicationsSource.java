package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ScriptDTO;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class BackgroundApplicationsSource implements ApplicationsSource {
    private final ApplicationsSource delegatedAppManager;
    private final ExecutorService executorService;

    public BackgroundApplicationsSource(ApplicationsSource delegatedAppManager,
                                        ExecutorService executorService) {
        this.delegatedAppManager = delegatedAppManager;
        this.executorService = executorService;
    }

    @Override
    public TreeMap<String, CategoryDTO> fetchInstallableApplications() {
        throw new UnsupportedOperationException("The background apps manager is asynchroneous");
    }

    @Override
    public void fetchInstallableApplications(Consumer<List<CategoryDTO>> callback, Consumer<Exception> errorCallback) {
        executorService.submit(() -> delegatedAppManager.fetchInstallableApplications(callback, errorCallback));
    }

    @Override
    public void getScript(List<String> path, Consumer<ScriptDTO> callback, Consumer<Exception> errorCallback) {
        executorService.submit(() -> delegatedAppManager.getScript(path, callback, errorCallback));
    }
}
