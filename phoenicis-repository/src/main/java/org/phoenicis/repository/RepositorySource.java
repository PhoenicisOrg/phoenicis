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

import org.phoenicis.repository.dto.ApplicationCategoryDTO;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface RepositorySource {
    List<ApplicationCategoryDTO> fetchInstallableApplications();

    default void fetchInstallableApplications(Consumer<List<ApplicationCategoryDTO>> callback, Consumer<Exception> errorCallback) {
        try {
            callback.accept(Collections.unmodifiableList(fetchInstallableApplications()));
        } catch(Exception e) {
            errorCallback.accept(e);
        }
    }

    default ScriptDTO getScript(List<String> path) {
        final ApplicationDTO applicationDTO = getApplication(path);

        if(applicationDTO != null) {
            for (ScriptDTO scriptDTO : applicationDTO.getScripts()) {
                if (path.get(2).equals(scriptDTO.getName())) {
                    return scriptDTO;
                }
            }
        }

        return null;
    }

    default void getScript(List<String> path, Consumer<ScriptDTO> callback, Consumer<Exception> errorCallback) {
        callback.accept(getScript(path));
    }

    default ApplicationCategoryDTO getCategory(List<String> path) {
        final Optional<ApplicationCategoryDTO> categoryDTO = fetchInstallableApplications().stream().filter(category -> path.get(0).equals(category.getName())).findFirst();
        return categoryDTO.isPresent() ? categoryDTO.get() : null;
    }
    default ApplicationDTO getApplication(List<String> path) {
        final ApplicationCategoryDTO applicationCategoryDTO = getCategory(path);
        if(applicationCategoryDTO == null) {
            return null;
        }

        final Optional<ApplicationDTO> applicationDTO = applicationCategoryDTO.getApplications().stream().filter(application -> path.get(1).equals(application.getName())).findFirst();
        return applicationDTO.isPresent() ? applicationDTO.get() : null;
    }
}
