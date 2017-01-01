package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.util.Arrays;
import java.util.List;

class MultipleApplicationSource implements ApplicationsSource {
    private final ApplicationsSource applicationsSource;

    MultipleApplicationSource(ApplicationsSource ...applicationsSources) {
        this(Arrays.asList(applicationsSources));
    }

    MultipleApplicationSource(List<ApplicationsSource> applicationsSources) {
        ApplicationsSource lastApplicationSource = new NullApplicationSource();

        for (ApplicationsSource applicationSource : applicationsSources) {
            lastApplicationSource = new TeeApplicationsSource(lastApplicationSource, applicationSource);
        }

        this.applicationsSource = lastApplicationSource;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        return applicationsSource.fetchInstallableApplications();
    }
}
