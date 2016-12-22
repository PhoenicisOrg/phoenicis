package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.util.List;
import java.util.function.Consumer;

public class TeeApplicationsSource implements ApplicationsSource {
    private final ApplicationsSource leftApplicationSource;
    private final ApplicationsSource rightApplicationSource;

    public TeeApplicationsSource(ApplicationsSource leftApplicationSource,
                                 ApplicationsSource rightApplicationSource) {
        this.leftApplicationSource = leftApplicationSource;
        this.rightApplicationSource = rightApplicationSource;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        throw new IllegalStateException("Not implemented yet");
    }
}
