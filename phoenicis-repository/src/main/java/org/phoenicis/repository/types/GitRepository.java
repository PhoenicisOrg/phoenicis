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

package org.phoenicis.repository.types;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.phoenicis.repository.RepositoryException;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.tools.files.FileUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Semaphore;

public class GitRepository implements Repository {
    private final static Logger LOGGER = LoggerFactory.getLogger(GitRepository.class);

    private final FileUtilities fileUtilities;

    private final URI repositoryUri;
    private final String branch;

    private final LocalRepository.Factory localRepositoryFactory;

    private final File localFolder;
    private static Semaphore mutex = new Semaphore(1);

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
        int hashcode = new HashCodeBuilder().append(this.repositoryUri).append(this.branch).toHashCode();

        return new File(cacheDirectoryPath + "/git" + hashcode);
    }

    @Override
    public synchronized RepositoryDTO fetchInstallableApplications() {
        RepositoryDTO result = null;
        try {
            mutex.acquire();
            try {
                LOGGER.info("Begin fetching process of " + this);

                boolean folderExists = this.localFolder.exists();

                // check that the repository folder exists
                if (!folderExists) {
                    LOGGER.info("Creating local folder for " + this);

                    if (!this.localFolder.mkdirs()) {
                        throw new RepositoryException("Couldn't create local folder for " + this);
                    }
                }
                Git gitRepository = null;

                try {
                    /*
                     * if the repository folder previously didn't exist, clone the
                     * repository now and checkout the correct branch
                     */
                    if (!folderExists) {
                        LOGGER.info("Cloning " + this);

                        gitRepository = Git.cloneRepository().setURI(this.repositoryUri.toString())
                                .setDirectory(this.localFolder)
                                .setBranch(this.branch).call();
                    }
                    /*
                     * otherwise open the folder and pull the newest updates from the
                     * repository
                     */
                    else {
                        LOGGER.info("Opening " + this);

                        gitRepository = Git.open(localFolder);

                        LOGGER.info("Pulling new commits from " + this);

                        gitRepository.pull().call();
                    }

                    result = localRepositoryFactory.createInstance(this.localFolder, this.repositoryUri)
                            .fetchInstallableApplications();
                } catch (RepositoryNotFoundException | GitAPIException e) {
                    throw new RepositoryException(
                            String.format("Folder '%s' is no git-repository", this.localFolder.getAbsolutePath()), e);
                } catch (IOException e) {
                    throw new RepositoryException("An unknown error occurred", e);
                } finally {
                    // close repository to free resources
                    if (gitRepository != null) {
                        gitRepository.close();
                    }
                }
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            throw new RepositoryException("InterruptedException occurred", e);
        }

        return result;
    }

    @Override
    public void onDelete() {
        try {
            fileUtilities.remove(this.localFolder);

            LOGGER.info("Deleted " + this);
        } catch (IOException e) {
            LOGGER.error(String.format("Couldn't delete " + this), e);
        }
    }

    @Override
    public String toString() {
        return String.format("git-repository %s, local folder: %s, branch: %s", this.repositoryUri,
                this.localFolder.getAbsolutePath(), this.branch);
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
                .append(this.repositoryUri, that.repositoryUri)
                .append(this.localFolder, that.localFolder)
                .append(this.branch, that.branch)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.repositoryUri)
                .append(this.localFolder)
                .append(this.branch)
                .toHashCode();
    }
}
