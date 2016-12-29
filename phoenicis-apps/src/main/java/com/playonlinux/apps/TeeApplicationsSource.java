package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.util.SortedMap;
import java.util.TreeMap;

public class TeeApplicationsSource implements ApplicationsSource {
    private final ApplicationsSource leftApplicationSource;
    private final ApplicationsSource rightApplicationSource;

    /**
     * merges fetched applications from two sources
     * If an application is found in both sources, the leftApplicationSource will be used.
     * @param leftApplicationSource
     * @param rightApplicationSource
     */
    public TeeApplicationsSource(ApplicationsSource leftApplicationSource,
                                 ApplicationsSource rightApplicationSource) {
        this.leftApplicationSource = leftApplicationSource;
        this.rightApplicationSource = rightApplicationSource;
    }

    @Override
    public SortedMap<String, CategoryDTO> fetchInstallableApplications() {
        SortedMap<String, CategoryDTO> leftApps = leftApplicationSource.fetchInstallableApplications();
        SortedMap<String, CategoryDTO> rightApps = rightApplicationSource.fetchInstallableApplications();

        SortedMap<String, CategoryDTO> mergedApps = new TreeMap<>(rightApps);

        for (String categoryName : leftApps.keySet()) {
            CategoryDTO category = leftApps.get(categoryName);
            if (mergedApps.containsKey(categoryName)) {
                mergedApps.get(categoryName).getApplications().putAll(category.getApplications());
            } else {
                mergedApps.put(categoryName, category);
            }
        }
        return mergedApps;
    }
}
