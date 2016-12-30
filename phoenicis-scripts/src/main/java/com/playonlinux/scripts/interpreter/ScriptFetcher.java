package com.playonlinux.scripts.interpreter;

import com.playonlinux.apps.ApplicationsSource;
import com.playonlinux.apps.dto.ScriptDTO;

import java.util.Arrays;
import java.util.List;

public class ScriptFetcher {
    private final ApplicationsSource appsSource;

    public ScriptFetcher(ApplicationsSource appsSource) {
        this.appsSource = appsSource;
    }

    public String getScript(List<String> path) {
        final ScriptDTO script = appsSource.getScript(path);

        if (script == null) {
            throw new ScriptException("Script not found: " + path);
        }

        return script.getScript();
    }

    public String getScript(String... path) {
        return getScript(Arrays.asList(path));
    }

}
