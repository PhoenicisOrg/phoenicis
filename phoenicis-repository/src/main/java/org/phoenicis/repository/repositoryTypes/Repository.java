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

import org.phoenicis.repository.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface Repository {
    Logger LOGGER = LoggerFactory.getLogger(Repository.class);

    RepositoryDTO fetchInstallableApplications();

    /**
     * A safe repository contains scripts that is not constraint by the @Safe annotation
     * TODO: Implement this behaviour
     * @return true if the repository is safe
     */
    default boolean isSafe() {
        return false;
    }

    default void onDelete() {
        // do nothing
    }

    default void fetchInstallableApplications(Consumer<RepositoryDTO> callback, Consumer<Exception> errorCallback) {
        try {
            callback.accept(fetchInstallableApplications());
        } catch (Exception e) {
            LOGGER.error("Fetching installable applications failed!", e);
            errorCallback.accept(e);
        }
    }

    default TypeDTO getType(List<String> path) {
        final Optional<TypeDTO> typeDTO = fetchInstallableApplications().getTypes().stream()
                .filter(type -> path.get(0).equals(type.getId())).findFirst();
        return typeDTO.orElse(null);
    }

    default CategoryDTO getCategory(List<String> path) {
        final TypeDTO typeDTO = getType(path);
        if (typeDTO == null) {
            return null;
        }

        final Optional<CategoryDTO> categoryDTO = typeDTO.getCategories().stream()
                .filter(category -> path.get(1).equals(category.getId())).findFirst();
        return categoryDTO.orElse(null);
    }

    default ApplicationDTO getApplication(List<String> path) {
        final CategoryDTO categoryDTO = getCategory(path);
        if (categoryDTO == null) {
            return null;
        }

        final Optional<ApplicationDTO> applicationDTO = categoryDTO.getApplications().stream()
                .filter(application -> path.get(2).equals(application.getName())).findFirst();
        return applicationDTO.orElse(null);
    }

    default ScriptDTO getScript(List<String> path) {
        final ApplicationDTO applicationDTO = getApplication(path);

        if (applicationDTO != null) {
            for (ScriptDTO scriptDTO : applicationDTO.getScripts()) {
                if (path.get(3).equals(scriptDTO.getScriptName())) {
                    return scriptDTO;
                }
            }
        }

        return null;
    }

    default void getScript(List<String> path, Consumer<ScriptDTO> callback, Consumer<Exception> errorCallback) {
        callback.accept(getScript(path));
    }
}
