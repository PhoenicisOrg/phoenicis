package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class GitApplicationsSource implements ApplicationsSource {
    private final String gitRepositoryURL;
    private final LocalApplicationsSource.Factory localAppsSourceFactory;
    private List<CategoryDTO> cache;

    public GitApplicationsSource(String gitRepositoryURL, LocalApplicationsSource.Factory localAppsSourceFactory) {
        this.gitRepositoryURL = gitRepositoryURL;
        this.localAppsSourceFactory = localAppsSourceFactory;
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

            cache = localAppsSourceFactory.createInstance(gitTmp.getAbsolutePath()).fetchInstallableApplications();
            return cache;
        } catch (IOException | InterruptedException e) {
            return Collections.emptyList();
        }
    }


}
