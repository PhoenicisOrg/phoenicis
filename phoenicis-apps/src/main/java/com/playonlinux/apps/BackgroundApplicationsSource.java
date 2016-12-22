package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.util.List;
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
    public List<CategoryDTO> fetchInstallableApplications() {
        throw new UnsupportedOperationException("The background apps manager is asynchroneous");
    }

    @Override
    public void fetchInstallableApplications(Consumer<List<CategoryDTO>> callback, Consumer<Exception> errorCallback) {
        executorService.submit(() -> delegatedAppManager.fetchInstallableApplications(callback, errorCallback));
    }
}
