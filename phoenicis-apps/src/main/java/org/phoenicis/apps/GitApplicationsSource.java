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

package org.phoenicis.apps;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.phoenicis.apps.dto.CategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GitApplicationsSource implements ApplicationsSource {
	private final static Logger LOGGER = LoggerFactory.getLogger(GitApplicationsSource.class);

	private final String gitRepositoryURL;
	private final LocalApplicationsSource.Factory localAppsSourceFactory;
	private List<CategoryDTO> cache;

	private static final String cacheDirectoryPath = System.getProperty("java.io.tmpdir");

	GitApplicationsSource(String gitRepositoryURL, LocalApplicationsSource.Factory localAppsSourceFactory) {
		this.gitRepositoryURL = gitRepositoryURL;
		this.localAppsSourceFactory = localAppsSourceFactory;
	}

	@Override
	public synchronized List<CategoryDTO> fetchInstallableApplications() {
		if (cache != null) {
			return cache;
		}

		File gitRepositoryLocation = new File(
				GitApplicationsSource.cacheDirectoryPath + "/git" + this.gitRepositoryURL.hashCode());

		LOGGER.info(String.format("Git-repository '%s' will be saved in '%s'", this.gitRepositoryURL,
				gitRepositoryLocation.getAbsolutePath()));

		/*
		 * This value is true if a folder for the git repository exists,
		 * otherwise it is false
		 */
		boolean folderExists = true;

		// check that the repository folder exists
		if (!gitRepositoryLocation.exists()) {
			folderExists = false;

			LOGGER.info(String.format("Creating new folder '%s' for git-repository '%s'",
					gitRepositoryLocation.getAbsolutePath(), this.gitRepositoryURL));

			if (!gitRepositoryLocation.mkdir()) {
				LOGGER.error(String.format("Couldn't create folder for git repository '%s' at '%s'",
						this.gitRepositoryURL, gitRepositoryLocation.getAbsolutePath()));

				return Collections.emptyList();
			}
		}

		try {
			Git gitRepository = null;

			/*
			 * if the repository folder previously didn't exist, clone the
			 * repository now
			 */
			if (!folderExists) {
				LOGGER.info(String.format("Cloning git-repository '%s' to '%s'", this.gitRepositoryURL, gitRepositoryLocation.getAbsolutePath()));

				gitRepository = Git.cloneRepository().setURI(this.gitRepositoryURL).setDirectory(gitRepositoryLocation)
						.call();
			}
			/*
			 * otherwise open the folder and pull the newest updates from the
			 * repository
			 */
			else {
				LOGGER.info(String.format("Opening git-repository '%s' at '%s'", this.gitRepositoryURL,
						gitRepositoryLocation.getAbsolutePath()));

				gitRepository = Git.open(gitRepositoryLocation);

				LOGGER.info(String.format("Pulling new commits from git-repository '%s' to '%s'", this.gitRepositoryURL,
						gitRepositoryLocation.getAbsolutePath()));

				gitRepository.pull().call();
			}

			// close repository to free resources
			gitRepository.close();

			this.cache = localAppsSourceFactory.createInstance(gitRepositoryLocation.getAbsolutePath())
					.fetchInstallableApplications();
		} catch (RepositoryNotFoundException | GitAPIException e) {
			LOGGER.error(String.format("Folder '%s' is no git-repository", gitRepositoryLocation.getAbsolutePath()), e);

			this.cache = Collections.emptyList();
		} catch (IOException e) {
			LOGGER.error(String.format("An unknown error occured.", e));

			this.cache = Collections.emptyList();
		}

		return this.cache;
	}
}
