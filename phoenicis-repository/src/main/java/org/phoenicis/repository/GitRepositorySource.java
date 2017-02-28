/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.repository;

import org.phoenicis.repository.dto.RepositoryDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class GitRepositorySource implements RepositorySource {
    private final String gitRepositoryURL;
    private final LocalRepositorySource.Factory localRepositorySourceFactory;
    private List<RepositoryDTO> cache;

    GitRepositorySource(String gitRepositoryURL, LocalRepositorySource.Factory localRepositorySourceFactory) {
        this.gitRepositoryURL = gitRepositoryURL;
        this.localRepositorySourceFactory = localRepositorySourceFactory;
    }

    @Override
    public synchronized List<RepositoryDTO> fetchRepositories() {
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

            cache = localRepositorySourceFactory.createInstance(gitTmp.getAbsolutePath()).fetchRepositories();
            return cache;
        } catch (IOException | InterruptedException e) {
            return Collections.emptyList();
        }
    }

}
