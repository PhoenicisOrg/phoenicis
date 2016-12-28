package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GitApplicationsSource implements ApplicationsSource {
    private final String gitRepositoryURL;
    private final LocalApplicationsSource.Factory localAppsManagerFactory;
    private List<CategoryDTO> cache;

    public GitApplicationsSource(String gitRepositoryURL, LocalApplicationsSource.Factory localAppsManagerFactory) {
        this.gitRepositoryURL = gitRepositoryURL;
        this.localAppsManagerFactory = localAppsManagerFactory;
    }

    @Override
    public synchronized List<CategoryDTO> fetchInstallableApplications() {
        if (cache != null) {
            return cache;
        }

        try {
            final File gitTmp = Files.createTempDirectory("git").toFile();
            gitTmp.deleteOnExit();
            new ProcessBuilder(Arrays.asList("git", "clone", gitRepositoryURL, gitTmp.getAbsolutePath()))
                    .inheritIO()
                    .start()
                    .waitFor();

            cache = localAppsManagerFactory.createInstance(gitTmp.getAbsolutePath()).fetchInstallableApplications();
            return cache;
        } catch (IOException | InterruptedException e) {
            return Collections.emptyList();
        }
    }


}
