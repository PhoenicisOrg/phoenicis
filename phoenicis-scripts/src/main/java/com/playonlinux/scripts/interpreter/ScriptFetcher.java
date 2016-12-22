package com.playonlinux.scripts.interpreter;

import com.playonlinux.apps.AppsManager;
import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ScriptDTO;

import java.util.Arrays;
import java.util.List;

public class ScriptFetcher {
    private final AppsManager appsManager;

    public ScriptFetcher(AppsManager appsManager) {
        this.appsManager = appsManager;
    }

    public String getScript(List<String> path) {
        for (CategoryDTO categoryDTO : appsManager.fetchInstallableApplications()) {
            if(path.get(0).equals(categoryDTO.getName())) {
                for (ApplicationDTO applicationDTO : categoryDTO.getApplications()) {
                    if(path.get(1).equals(applicationDTO.getName())) {
                        for (ScriptDTO scriptDTO : applicationDTO.getScripts()) {
                            if(path.get(2).equals(scriptDTO.getName())) {
                                return scriptDTO.getScript();
                            }
                        }
                    }
                }
            }
        }

        throw new ScriptException("Script not found: " + path);
    }

    public String getScript(String... path) {
        return getScript(Arrays.asList(path));
    }

}
