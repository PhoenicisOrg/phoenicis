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
import org.phoenicis.repository.dto.RepositoryDTO;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class TeeRepository extends MergeableRepository {
    private final Repository leftRepository;
    private final Repository rightRepository;

    /**
     * merges fetched applications from two sources
     * If an application is found in both sources, the leftRepository will be used.
     * @param leftRepository
     * @param rightRepository
     */
    public TeeRepository(Repository leftRepository, Repository rightRepository) {
        this.leftRepository = leftRepository;
        this.rightRepository = rightRepository;
    }

    @Override
    public RepositoryDTO fetchInstallableApplications() {
        final Map<Repository, RepositoryDTO> repositoriesMap = Arrays.asList(leftRepository, rightRepository).stream()
                .collect(Collectors.toMap(source -> source, Repository::fetchInstallableApplications));

        return mergeRepositories(repositoriesMap, Arrays.asList(leftRepository, rightRepository));
    }

    @Override
    public void onDelete() {
        this.leftRepository.onDelete();
        this.rightRepository.onDelete();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeeRepository that = (TeeRepository) o;

        return new EqualsBuilder()
                .append(leftRepository, that.leftRepository)
                .append(rightRepository, that.rightRepository)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(leftRepository)
                .append(rightRepository)
                .toHashCode();
    }
}
