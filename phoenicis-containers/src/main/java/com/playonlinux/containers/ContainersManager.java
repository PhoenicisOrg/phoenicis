package com.playonlinux.containers;

import com.playonlinux.containers.dto.ContainerDTO;

import java.util.List;
import java.util.function.Consumer;

public interface ContainersManager {
    void fetchContainers(Consumer<List<ContainerDTO>> callback, Consumer<Exception> errorCallback);
}
