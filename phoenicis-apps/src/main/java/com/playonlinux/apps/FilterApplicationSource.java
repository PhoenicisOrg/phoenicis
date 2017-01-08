package com.playonlinux.apps;

import com.phoenicis.entities.OperatingSystem;
import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ScriptDTO;
import com.playonlinux.tools.system.OperatingSystemFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Takes an Application Source and removes all unsupported scripts
 */
class FilterApplicationSource implements ApplicationsSource {
    private final ApplicationsSource applicationsSource;
    private final OperatingSystemFetcher operatingSystemFetcher;
    private final boolean enforceIncompatibleOperatingSystems;

    FilterApplicationSource(ApplicationsSource applicationsSource,
                            OperatingSystemFetcher operatingSystemFetcher,
                            boolean enforceIncompatibleOperatingSystems) {
        this.applicationsSource = applicationsSource;
        this.operatingSystemFetcher = operatingSystemFetcher;
        this.enforceIncompatibleOperatingSystems = enforceIncompatibleOperatingSystems;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        final OperatingSystem currentOperatingSystem = operatingSystemFetcher.fetchCurrentOperationSystem();
        final List<CategoryDTO> categories = applicationsSource.fetchInstallableApplications();
        final List<CategoryDTO> filteredCategories = new ArrayList<>();
        for (CategoryDTO category : categories) {
            final List<ApplicationDTO> applications = new ArrayList<>();
            for (ApplicationDTO application : category.getApplications()) {
                final List<ScriptDTO> scripts;
                if(enforceIncompatibleOperatingSystems) {
                    scripts = application.getScripts();
                } else {
                    scripts = application.getScripts().stream().filter(script -> script.getCompatibleOperatingSystems() == null || script.getCompatibleOperatingSystems().contains(currentOperatingSystem)).collect(Collectors.toList());
                }
                if (!scripts.isEmpty()) {
                    applications.add(
                            new ApplicationDTO.Builder(application)
                                    .withScripts(scripts)
                                    .build()
                    );
                }
            }
            if (!applications.isEmpty()) {
                filteredCategories.add(
                        new CategoryDTO.Builder(category)
                                .withApplications(applications)
                                .build()
                );
            }
        }

        return filteredCategories;
    }
}
