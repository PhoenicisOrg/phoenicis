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

package org.phoenicis.repository.repositoryTypes;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.tools.files.FileUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class GitRepository implements Repository {
    private final static Logger LOGGER = LoggerFactory.getLogger(GitRepository.class);

    private final FileUtilities fileUtilities;

    private final URI repositoryUri;
    private final String branch;

    private final LocalRepository.Factory localRepositoryFactory;

    private final File localFolder;

    public GitRepository(URI repositoryUri, String branch, String cacheDirectoryPath,
            LocalRepository.Factory localRepositoryFactory, FileUtilities fileUtilities) {
        super();

        this.fileUtilities = fileUtilities;
        this.repositoryUri = repositoryUri;
        this.branch = branch == null ? "master" : branch;
        this.localRepositoryFactory = localRepositoryFactory;

        this.localFolder = createRepositoryLocation(cacheDirectoryPath);
    }

    private File createRepositoryLocation(String cacheDirectoryPath) {
        int hashcode = new HashCodeBuilder().append(repositoryUri).append(branch).toHashCode();

        return new File(cacheDirectoryPath + "/git" + hashcode);
    }

    @Override
    public synchronized RepositoryDTO fetchInstallableApplications() {
        LOGGER.info(String.format("Begin fetching process of git-repository '%s' in '%s'", this.repositoryUri,
                localFolder.getAbsolutePath()));

        boolean folderExists = localFolder.exists();

        // check that the repository folder exists
        if (!folderExists) {
            LOGGER.info(String.format("Creating new folder '%s' for git-repository '%s'", localFolder.getAbsolutePath(),
                    this.repositoryUri));

            if (!localFolder.mkdirs()) {
                LOGGER.error(String.format("Couldn't create folder for git repository '%s' at '%s'", this.repositoryUri,
                        localFolder.getAbsolutePath()));

                return new RepositoryDTO.Builder().build();
            }
        }

        RepositoryDTO result = null;
        Git gitRepository = null;

        try {
            /*
             * if the repository folder previously didn't exist, clone the
             * repository now and checkout the correct branch
             */
            if (!folderExists) {
                LOGGER.info(String.format("Cloning git-repository '%s' to '%s'", this.repositoryUri,
                        localFolder.getAbsolutePath()));

                gitRepository = Git.cloneRepository().setURI(this.repositoryUri.toString()).setDirectory(localFolder)
                        .setBranch(branch).call();
            }
            /*
             * otherwise open the folder and pull the newest updates from the
             * repository
             */
            else {
                LOGGER.info(String.format("Opening git-repository '%s' at '%s'", this.repositoryUri,
                        localFolder.getAbsolutePath()));

                gitRepository = Git.open(localFolder);

                LOGGER.info(String.format("Pulling new commits from git-repository '%s' to '%s'", this.repositoryUri,
                        localFolder.getAbsolutePath()));

                gitRepository.pull().call();
            }

            result = localRepositoryFactory.createInstance(this.localFolder, this.repositoryUri)
                    .fetchInstallableApplications();
        } catch (RepositoryNotFoundException | GitAPIException e) {
            LOGGER.error(String.format("Folder '%s' is no git-repository", localFolder.getAbsolutePath()), e);
        } catch (IOException e) {
            LOGGER.error("An unknown error occurred", e);
        } finally {
            // close repository to free resources
            if (gitRepository != null) {
                gitRepository.close();
            }
        }

        return result;
    }

    @Override
    public void onDelete() {
        try {
            fileUtilities.remove(this.localFolder);

            LOGGER.info(String.format("Deleted git-repository '%s' at '%s'", this.repositoryUri,
                    localFolder.getAbsolutePath()));
        } catch (IOException e) {
            LOGGER.error(String.format("Couldn't delete local git-repository at: '%s'", localFolder.getAbsolutePath()),
                    e);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("repositoryUri", repositoryUri).append("localFolder", localFolder)
                .append("branch", branch).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GitRepository that = (GitRepository) o;

        return new EqualsBuilder()
                .append(repositoryUri, that.repositoryUri)
                .append(localFolder, that.localFolder)
                .append(branch, that.branch)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(repositoryUri)
                .append(localFolder)
                .append(branch)
                .toHashCode();
    }
}
