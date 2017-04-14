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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.apps.dto.CategoryDTO;

import java.util.*;
import java.util.stream.Collectors;

public class MultipleRepository extends MergeableRepository {
	private List<Repository> repositories;

	public MultipleRepository(Repository... repositories) {
		this.repositories = new ArrayList<Repository>(Arrays.asList(repositories));
	}

	public MultipleRepository(List<Repository> repositories) {
		this.repositories = repositories;
	}

	@Override
	public List<CategoryDTO> fetchInstallableApplications() {
		/*
		 * This step is needed because we need a mapping between the CategoryDTO
		 * list and its application source, to preserve the order in the
		 * reduction step
		 */
		final Map<Repository, List<CategoryDTO>> categoriesMap = this.repositories.stream()
				.parallel().collect(
						Collectors.toConcurrentMap(source -> source, Repository::fetchInstallableApplications));

		return mergeRepositories(categoriesMap, repositories);
	}

	@Override
	public void onDelete() {
		repositories.stream().forEach(Repository::onDelete);
	}

	public int size() {
		return this.repositories.size();
	}

	public void moveRepository(Repository repository, int toIndex) {
		int oldIndex = this.repositories.indexOf(repository);

		Collections.swap(this.repositories, oldIndex, toIndex);
	}

	public void addRepository(Repository repository) {
		this.repositories.add(repository);
	}

	public void addRepository(int index, Repository repository) {
		this.repositories.add(index, repository);
	}

	public void removeRepository(Repository repository) {
		this.repositories.remove(repository);
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
