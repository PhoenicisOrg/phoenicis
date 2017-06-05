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

    private final URI gitRepositoryUri;
    private final LocalRepository.Factory localRepositoryFactory;

    private final File gitRepositoryLocation;

    public GitRepository(URI gitRepositoryUri, String cacheDirectoryPath,
            LocalRepository.Factory localRepositoryFactory, FileUtilities fileUtilities) {
        this.fileUtilities = fileUtilities;
        this.gitRepositoryUri = gitRepositoryUri;
        this.localRepositoryFactory = localRepositoryFactory;
        this.gitRepositoryLocation = new File(cacheDirectoryPath + "/git" + gitRepositoryUri.hashCode());
    }

    @Override
    public synchronized RepositoryDTO fetchInstallableApplications() {
        LOGGER.info(String.format("Begin fetching process of git-repository '%s' in '%s'", this.gitRepositoryUri,
                gitRepositoryLocation.getAbsolutePath()));

        boolean folderExists = gitRepositoryLocation.exists();

        // check that the repository folder exists
        if (!folderExists) {
            LOGGER.info(String.format("Creating new folder '%s' for git-repository '%s'",
                    gitRepositoryLocation.getAbsolutePath(), this.gitRepositoryUri));

            if (!gitRepositoryLocation.mkdirs()) {
                LOGGER.error(String.format("Couldn't create folder for git repository '%s' at '%s'",
                        this.gitRepositoryUri, gitRepositoryLocation.getAbsolutePath()));

                return new RepositoryDTO.Builder().build();
            }
        }

        RepositoryDTO result = null;

        try {
            Git gitRepository = null;

            /*
             * if the repository folder previously didn't exist, clone the
             * repository now
             */
            if (!folderExists) {
                LOGGER.info(String.format("Cloning git-repository '%s' to '%s'", this.gitRepositoryUri,
                        gitRepositoryLocation.getAbsolutePath()));

                gitRepository = Git.cloneRepository().setURI(this.gitRepositoryUri.toString())
                        .setDirectory(gitRepositoryLocation).call();
            }
            /*
             * otherwise open the folder and pull the newest updates from the
             * repository
             */
            else {
                LOGGER.info(String.format("Opening git-repository '%s' at '%s'", this.gitRepositoryUri,
                        gitRepositoryLocation.getAbsolutePath()));

                gitRepository = Git.open(gitRepositoryLocation);

                LOGGER.info(String.format("Pulling new commits from git-repository '%s' to '%s'", this.gitRepositoryUri,
                        gitRepositoryLocation.getAbsolutePath()));

                gitRepository.pull().call();
            }

            // close repository to free resources
            gitRepository.close();

            result = localRepositoryFactory.createInstance(this.gitRepositoryLocation, this.gitRepositoryUri)
                    .fetchInstallableApplications();
        } catch (RepositoryNotFoundException | GitAPIException e) {
            LOGGER.error(String.format("Folder '%s' is no git-repository", gitRepositoryLocation.getAbsolutePath()), e);
        } catch (IOException e) {
            LOGGER.error(String.format("An unknown error occured", e));
        }

        return result;
    }

    @Override
    public void onDelete() {
        try {
            fileUtilities.remove(this.gitRepositoryLocation);

            LOGGER.info(String.format("Deleted git-repository '%s' at '%s'", this.gitRepositoryUri,
                    gitRepositoryLocation.getAbsolutePath()));
        } catch (IOException e) {
            LOGGER.error(String.format("Couldn't delete local git-repository at: '%s'",
                    gitRepositoryLocation.getAbsolutePath()), e);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("gitRepositoryUri", gitRepositoryUri)
                .append("gitRepositoryLocation", gitRepositoryLocation).toString();
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

        EqualsBuilder builder = new EqualsBuilder();

        builder.append(gitRepositoryUri, that.gitRepositoryUri);
        builder.append(gitRepositoryLocation, that.gitRepositoryLocation);

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(gitRepositoryUri);
        builder.append(gitRepositoryLocation);

        return builder.toHashCode();
    }
}
