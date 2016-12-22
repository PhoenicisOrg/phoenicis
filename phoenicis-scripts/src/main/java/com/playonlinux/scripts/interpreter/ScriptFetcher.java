package com.playonlinux.scripts.interpreter;

import com.playonlinux.apps.AppsManager;
import com.playonlinux.apps.dto.ScriptDTO;

import java.util.Arrays;
import java.util.List;

public class ScriptFetcher {
    private final AppsManager appsManager;

    public ScriptFetcher(AppsManager appsManager) {
        this.appsManager = appsManager;
    }

    public String getScript(List<String> path) {
        final ScriptDTO script = appsManager.getScript(path);

        if (script == null) {
            throw new ScriptException("Script not found: " + path);
        }

        return script.getScript();
    }

    public String getScript(String... path) {
        return getScript(Arrays.asList(path));
    }

}
