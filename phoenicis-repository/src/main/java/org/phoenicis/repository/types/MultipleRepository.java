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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class MultipleRepository extends MergeableRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleRepository.class);

    private final List<Repository> repositories;

    public MultipleRepository(Repository... repositories) {
        this.repositories = new ArrayList<>(Arrays.asList(repositories));
    }

    public MultipleRepository(List<Repository> repositories) {
        this.repositories = new ArrayList<>(repositories);
    }

    @Override
    public RepositoryDTO fetchInstallableApplications() {
        LOGGER.info(String.format("Fetching applications for: %s", this.toString()));

        final List<Repository> repositoriesSnapshot = getRepositoriesSnapshot();

        /*
         * This step is needed because we need a mapping between the CategoryDTO
         * list and its application source, to preserve the order in the
         * reduction step
         */
        final Map<Repository, RepositoryDTO> repositoriesMap = repositoriesSnapshot.stream().parallel()
                .collect(Collectors.toConcurrentMap(source -> source, Repository::fetchInstallableApplications));

        return mergeRepositories(repositoriesMap, repositoriesSnapshot);
    }

    @Override
    public void onDelete() {
        getRepositoriesSnapshot().forEach(Repository::onDelete);
    }

    public int size() {
        synchronized (this.repositories) {
            return this.repositories.size();
        }
    }

    public void moveRepository(Repository repository, int toIndex) {
        synchronized (this.repositories) {
            int oldIndex = this.repositories.indexOf(repository);

            Collections.swap(this.repositories, oldIndex, toIndex);
        }
    }

    public void addRepository(Repository repository) {
        synchronized (this.repositories) {
            this.repositories.add(repository);
        }
    }

    public void addRepository(int index, Repository repository) {
        synchronized (this.repositories) {
            this.repositories.add(index, repository);
        }
    }

    public void removeRepository(Repository repository) {
        synchronized (this.repositories) {
            this.repositories.remove(repository);
        }
    }

    private List<Repository> getRepositoriesSnapshot() {
        synchronized (this.repositories) {
            return new ArrayList<>(this.repositories);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("repositories", getRepositoriesSnapshot()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MultipleRepository that = (MultipleRepository) o;

        return new EqualsBuilder()
                .append(getRepositoriesSnapshot(), that.getRepositoriesSnapshot())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getRepositoriesSnapshot())
                .toHashCode();
    }
}
