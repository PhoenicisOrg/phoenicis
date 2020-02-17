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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.phoenicis.repository.RepositoryException;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileLock;

public class GitRepository implements Repository {
    private final static Logger LOGGER = LoggerFactory.getLogger(GitRepository.class);

    private final URI repositoryUri;
    private final String branch;

    private final LocalRepository.Factory localRepositoryFactory;

    private final File localFolder;
    // lock file to avoid concurrent access to the git clone
    private final File lockFile;

    private static final Object mutex = new Object();

    public GitRepository(URI repositoryUri, String branch, String cacheDirectoryPath,
            LocalRepository.Factory localRepositoryFactory) {
        super();

        this.repositoryUri = repositoryUri;
        this.branch = branch == null ? "master" : branch;
        this.localRepositoryFactory = localRepositoryFactory;

        this.localFolder = createRepositoryLocation(cacheDirectoryPath);
        this.lockFile = new File(this.localFolder.getAbsolutePath() + ".lock");
    }

    private File createRepositoryLocation(String cacheDirectoryPath) {
        int hashcode = new HashCodeBuilder().append(this.repositoryUri).append(this.branch).toHashCode();

        return new File(cacheDirectoryPath + "/git" + hashcode);
    }

    private void cloneOrUpdateWithLock() throws RepositoryException {
        synchronized (mutex) {
            try {
                LOGGER.info("Begin fetching process of '{}' to '{}'",
                        this.repositoryUri, this.localFolder.getAbsolutePath());

                boolean lockFileExists = this.lockFile.exists();

                // check that the repository lock file exists
                if (!lockFileExists) {
                    LOGGER.info("Creating lock file '{}' for git-repository '{}'",
                            this.lockFile.getAbsolutePath(), this.repositoryUri);

                    try {
                        this.lockFile.getParentFile().mkdirs();
                        this.lockFile.createNewFile();
                    } catch (IOException e) {
                        final String message = String.format("Couldn't create lock file '%s'",
                                this.lockFile.getAbsolutePath());

                        throw new RepositoryException(message, e);
                    }
                }
                try (FileOutputStream lockFileStream = new FileOutputStream(lockFile, true)) {
                    try (FileLock ignored = lockFileStream.getChannel().lock()) {
                        cloneOrUpdate();
                    }
                }
            } catch (IOException e) {
                throw new RepositoryException("An unknown error occurred", e);
            }
        }
    }

    private void cloneOrUpdate() throws RepositoryException {
        final boolean folderExists = this.localFolder.exists();

        // check that the repository folder exists
        if (!folderExists) {
            LOGGER.info("Creating local folder '{}' for git-repository '{}'",
                    this.localFolder.getAbsolutePath(), this.repositoryUri);

            if (!this.localFolder.mkdirs()) {
                final String message = String.format("Couldn't create local folder '%s' for git-repository '%s'",
                        this.localFolder.getAbsolutePath(), this.repositoryUri);

                throw new RepositoryException(message);
            }
        }

        if (!folderExists) {
            LOGGER.info("Cloning git-repository '{}' to '{}'",
                    this.repositoryUri, this.localFolder.getAbsolutePath());

            try (final Git gitRepository = Git.cloneRepository()
                    .setURI(this.repositoryUri.toString())
                    .setDirectory(this.localFolder)
                    .setBranch(this.branch).call()) {
                LOGGER.info("Finished cloning git-repository '{}' to '{}'", this.repositoryUri, this.localFolder);
            } catch (GitAPIException e) {
                final String message = String.format("Folder '%s' is no git-repository",
                        this.localFolder.getAbsolutePath());

                throw new RepositoryException(message, e);
            }
        } else {
            LOGGER.info("Opening git-repository at '{}'", this.localFolder.getAbsolutePath());

            // if anything doesn't work here, we still have our local checkout
            // e.g. could be that the git repository cannot be accessed, there is not Internet connection etc.
            // TODO: it might make sense to ensure that our local checkout is not empty / a valid git repository
            try (final Git gitRepository = Git.open(this.localFolder)) {
                LOGGER.info("Pulling new commits to '{}'", this.localFolder.getAbsolutePath());

                gitRepository.pull().call();

                LOGGER.info("Finished pulling new commits to '{}'", this.localFolder.getAbsolutePath());
            } catch (IOException | GitAPIException e) {
                LOGGER.warn("Could not update {0}. Local checkout will be used.", e);
            }
        }
    }

    @Override
    public RepositoryDTO fetchInstallableApplications() {
        try {
            cloneOrUpdateWithLock();

            final RepositoryDTO result = this.localRepositoryFactory
                    .createInstance(this.localFolder, this.repositoryUri)
                    .fetchInstallableApplications();

            return result;
        } catch (RepositoryException e) {
            final String message = String.format("Could not fetch installable applications for git-repository %s",
                    this.toString());

            throw new RepositoryException(message, e);
        }
    }

    @Override
    public void onDelete() {
        try {
            FileUtils.deleteDirectory(this.localFolder);

            LOGGER.info("Deleted local folder '{}' for git-repository '{}'",
                    this.localFolder.getAbsolutePath(), this.repositoryUri);
        } catch (IOException e) {
            final String message = String.format("Couldn't delete local folder '%s' for git-repository '%s'",
                    this.localFolder.getAbsolutePath(), this.repositoryUri);

            LOGGER.error(message, e);
        }
    }

    @Override
    public String toString() {
        return String.format("GitRepository(url: '%s', folder: '%s', branch: '%s')",
                this.repositoryUri, this.localFolder.getAbsolutePath(), this.branch);
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
