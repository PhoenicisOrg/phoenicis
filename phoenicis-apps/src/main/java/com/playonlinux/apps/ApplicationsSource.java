package com.playonlinux.apps;

import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ScriptDTO;

import java.util.*;
import java.util.function.Consumer;

public interface ApplicationsSource {
    SortedMap<String, CategoryDTO> fetchInstallableApplications();

    default void fetchInstallableApplications(Consumer<List<CategoryDTO>> callback, Consumer<Exception> errorCallback) {
        try {
            callback.accept(new ArrayList<CategoryDTO>(fetchInstallableApplications().values()));
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

    default ApplicationDTO getApplication(List<String> path) {
        CategoryDTO categoryDTO = fetchInstallableApplications().get(path.get(0));
        ApplicationDTO applicationDTO = categoryDTO.getApplications().get(path.get(1));
        return applicationDTO;
    }
}
