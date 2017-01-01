package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.util.Arrays;
import java.util.List;

class MultipleApplicationSource implements ApplicationsSource {
    private final List<ApplicationsSource> applicationSources;

    MultipleApplicationSource(ApplicationsSource ...applicationsSources) {
        this(Arrays.asList(applicationsSources));
    }

    MultipleApplicationSource(List<ApplicationsSource> applicationsSources) {
        this.applicationSources = applicationsSources;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        ApplicationsSource lastApplicationSource = new NullApplicationSource();

        for (ApplicationsSource applicationSource : applicationSources) {
            lastApplicationSource = new TeeApplicationsSource(lastApplicationSource, applicationSource);
        }

        return lastApplicationSource.fetchInstallableApplications();
    }
}
