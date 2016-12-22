package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GitAppsManager implements AppsManager {
    private final String gitRepositoryURL;
    private final LocalAppsManager.Factory localAppsManagerFactory;

    public GitAppsManager(String gitRepositoryURL, LocalAppsManager.Factory localAppsManagerFactory) {
        this.gitRepositoryURL = gitRepositoryURL;
        this.localAppsManagerFactory = localAppsManagerFactory;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        // FIXME : Use a background thread
        try {
            final File gitTmp = Files.createTempDirectory("git").toFile();
            gitTmp.deleteOnExit();
            new ProcessBuilder(Arrays.asList("git", "clone", gitRepositoryURL, gitTmp.getAbsolutePath()))
                .start()
                .waitFor();

            return localAppsManagerFactory.createInstance(gitTmp.getAbsolutePath()).fetchInstallableApplications();
        } catch (IOException | InterruptedException e) {
            return Collections.emptyList();
        }
    }
}
