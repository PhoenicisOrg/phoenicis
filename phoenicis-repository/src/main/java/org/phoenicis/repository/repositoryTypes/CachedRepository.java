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

public class CachedRepository implements Repository {
    private final Repository repository;
    private RepositoryDTO cache;

    public CachedRepository(Repository repository) {
        this.repository = repository;
    }

    @Override
    public synchronized RepositoryDTO fetchInstallableApplications() {
        if (cache == null) {
            cache = repository.fetchInstallableApplications();
        }

        return cache;
    }

    @Override
    public boolean isSafe() {
        return repository.isSafe();
    }

    @Override
    public void onDelete() {
        this.repository.onDelete();
    }

    public void clearCache() {
        this.cache = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CachedRepository that = (CachedRepository) o;

        EqualsBuilder builder = new EqualsBuilder();

        builder.append(repository, that.repository);

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(repository);

        return builder.toHashCode();
    }
}
