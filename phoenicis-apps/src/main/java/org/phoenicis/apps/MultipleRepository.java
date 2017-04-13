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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.apps.dto.CategoryDTO;

class MultipleRepository extends MergeableRepository {
	private Repository[] repositories;

	MultipleRepository(Repository... repositories) {
		this.repositories = repositories;
	}

	MultipleRepository(List<Repository> repositories) {
		this(repositories.toArray(new Repository[0]));
	}

	@Override
	public List<CategoryDTO> fetchInstallableApplications() {
		/*
		 * This step is needed because we need a mapping between the CategoryDTO
		 * list and its application source, to preserve the order in the
		 * reduction step
		 */
		final Map<Repository, List<CategoryDTO>> categoriesMap = Arrays.stream(this.repositories)
				.parallel().collect(
						Collectors.toConcurrentMap(source -> source, Repository::fetchInstallableApplications));

		return mergeRepositories(categoriesMap, repositories);
	}

	@Override
	public void onDelete() {
		Arrays.stream(repositories).forEach(Repository::onDelete);
	}

	public int size() {
		return this.repositories.length;
	}

	public void moveRepository(Repository repository, int toIndex) {
		List<Repository> newRepositories = Arrays.asList(this.repositories);

		int oldIndex = newRepositories.indexOf(repository);

		if (oldIndex >= 0 && toIndex >= 0 && toIndex < newRepositories.size()) {
			Collections.swap(newRepositories, oldIndex, toIndex);

			this.repositories = newRepositories.toArray(new Repository[0]);
		}
	}

	public void addRepository(Repository repository) {
		Repository[] newRepositories = Arrays.copyOf(this.repositories, this.repositories.length + 1);

		newRepositories[this.repositories.length] = repository;

		this.repositories = newRepositories;
	}

	public void addRepository(int index, Repository repository) {
		Repository[] newRepositories = new Repository[this.repositories.length + 1];

		System.arraycopy(this.repositories, 0, newRepositories, 0, index);
		System.arraycopy(this.repositories, index, newRepositories, index + 1, this.repositories.length - index);

		newRepositories[index] = repository;

		this.repositories = newRepositories;
	}

	public void removeRepository(Repository repository) {
		int index = Arrays.asList(this.repositories).indexOf(repository);

		if (index >= 0) {
			Repository[] newRepositories = new Repository[this.repositories.length - 1];

			System.arraycopy(this.repositories, 0, newRepositories, 0, index);
			System.arraycopy(this.repositories, index + 1, newRepositories, index, this.repositories.length - 1 - index);

			this.repositories = newRepositories;
		}
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

		EqualsBuilder builder = new EqualsBuilder();

		builder.append(repositories, that.repositories);

		return builder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();

		builder.append(repositories);

		return builder.toHashCode();
	}
}
