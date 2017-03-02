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

package org.phoenicis.repository;

import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface RepositorySource {
    List<RepositoryDTO> fetchRepositories();

    default void fetchRepositories(Consumer<List<RepositoryDTO>> callback, Consumer<Exception> errorCallback) {
        try {
            callback.accept(Collections.unmodifiableList(fetchRepositories()));
        } catch(Exception e) {
            errorCallback.accept(e);
        }
    }

    default ScriptDTO getScript(List<String> path) {
        final ApplicationDTO applicationDTO = getApplication(path);

        if(applicationDTO != null) {
            for (ScriptDTO scriptDTO : applicationDTO.getScripts()) {
                if (path.get(3).equals(scriptDTO.getName())) {
                    return scriptDTO;
                }
            }
        }

        return null;
    }

    default void setFilter(CombinedAppsFilter filter) {}

    default void getScript(List<String> path, Consumer<ScriptDTO> callback, Consumer<Exception> errorCallback) {
        callback.accept(getScript(path));
    }

    default RepositoryDTO getRepository(List<String> path) {
        final Optional<RepositoryDTO> repositoryDTO = fetchRepositories().stream().filter(repository -> path.get(0).equals(repository.getName())).findFirst();
        return repositoryDTO.isPresent() ? repositoryDTO.get() : null;
    }

    default CategoryDTO getCategory(List<String> path) {
        final RepositoryDTO repositoryDTO = getRepository(path);
        if(repositoryDTO == null) {
            return null;
        }

        final Optional<CategoryDTO> categoryDTO = repositoryDTO.getCategories().stream().filter(category -> path.get(1).equals(category.getName())).findFirst();
        return categoryDTO.isPresent() ? categoryDTO.get() : null;
    }

    default ApplicationDTO getApplication(List<String> path) {
        final CategoryDTO categoryDTO = getCategory(path);
        if(categoryDTO == null) {
            return null;
        }

        final Optional<ApplicationDTO> applicationDTO = categoryDTO.getApplications().stream().filter(application -> path.get(2).equals(application.getName())).findFirst();
        return applicationDTO.isPresent() ? applicationDTO.get() : null;
    }
}
