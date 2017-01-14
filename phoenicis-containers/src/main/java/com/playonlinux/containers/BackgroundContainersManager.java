package com.playonlinux.containers;

import com.playonlinux.containers.dto.ContainerDTO;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

class BackgroundContainersManager implements ContainersManager {
    private final ContainersManager delegatedContainersManager;
    private final ExecutorService executorService;

    BackgroundContainersManager(ContainersManager delegatedContainersManager,
                                 ExecutorService executorService) {
        this.delegatedContainersManager = delegatedContainersManager;
        this.executorService = executorService;
    }

    @Override
    public void fetchContainers(Consumer<List<ContainerDTO>> callback, Consumer<Exception> errorCallback) {
        executorService.submit(() -> delegatedContainersManager.fetchContainers(callback, errorCallback));
    }
}
