package com.playonlinux.apps;

import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ScriptDTO;

import java.util.*;
import java.util.function.Consumer;

public interface ApplicationsSource {
    List<CategoryDTO> fetchInstallableApplications();

    default void fetchInstallableApplications(Consumer<List<CategoryDTO>> callback, Consumer<Exception> errorCallback) {
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

    default CategoryDTO getCategory(List<String> path) {
        final Optional<CategoryDTO> categoryDTO = fetchInstallableApplications().stream().filter(category -> path.get(0).equals(category.getName())).findFirst();
        return categoryDTO.isPresent() ? categoryDTO.get() : null;
    }
    default ApplicationDTO getApplication(List<String> path) {
        final CategoryDTO categoryDTO = getCategory(path);
        if(categoryDTO == null) {
            return null;
        }

        final Optional<ApplicationDTO> applicationDTO = categoryDTO.getApplications().stream().filter(application -> path.get(1).equals(application.getName())).findFirst();
        return applicationDTO.isPresent() ? applicationDTO.get() : null;
    }
}
