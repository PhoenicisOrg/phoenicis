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

package org.phoenicis.engines;

import org.graalvm.polyglot.Value;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.phoenicis.scripts.session.InteractiveScriptSession;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * manages the engine settings
 */
public class EngineSettingsManager {
    private final ScriptInterpreter scriptInterpreter;

    /**
     * constructor
     *
     * @param scriptInterpreter
     */
    public EngineSettingsManager(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
    }

    /**
     * fetches the required setting
     *
     * @param engineId setting ID (e.g. "glsl")
     * @param settingId setting ID (e.g. "glsl")
     * @param doneCallback callback which will be executed with the fetched setting
     * @param errorCallback callback which will be executed if an error occurs
     */
    public void getSetting(String engineId, String settingId, Consumer<EngineSetting> doneCallback,
            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include(\"engines." + engineId + ".settings." + settingId + "\");",
                output -> {
                    final Value settingClass = (Value) output;

                    final EngineSetting setting = settingClass.newInstance().as(EngineSetting.class);

                    doneCallback.accept(setting);
                },
                errorCallback);
    }

    /**
     * fetches the available engine settings
     *
     * @param repositoryDTO
     * @param callback
     * @param errorCallback callback which will be executed if an error occurs
     */
    public void fetchAvailableEngineSettings(RepositoryDTO repositoryDTO,
            Consumer<Map<String, List<EngineSetting>>> callback, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(this.createFetchScript(repositoryDTO),
                output -> callback.accept(((Value) output).as(Map.class)),
                errorCallback);
    }

    /**
     * retrieves a Javascript string which can be used to fetch the available settings
     *
     * @param repositoryDTO repository containing the settings
     * @return Javascript
     */
    private String createFetchScript(RepositoryDTO repositoryDTO) {
        // get engine CategoryDTOs
        List<CategoryDTO> categoryDTOS = repositoryDTO.getTypes().stream()
                .filter(typeDTO -> typeDTO.getId().equals("engines"))
                .flatMap(typeDTO -> typeDTO.getCategories().stream())
                .collect(Collectors.toList());

        StringBuilder script = new StringBuilder();
        script.append("(function () {\n");
        script.append("const settings = {};\n");
        script.append("let Setting = null;\n");
        for (CategoryDTO engine : categoryDTOS) {
            final String engineId = engine.getId().replaceAll("^.*\\.", "");
            for (ApplicationDTO applicationDTO : engine.getApplications()) {
                if (applicationDTO.getId().equals("engines." + engineId + ".settings")) {
                    for (ScriptDTO scriptDTO : applicationDTO.getScripts()) {
                        final String settingsInclude = String.format("include(\"engines.%s.settings.%s\")",
                                engineId, scriptDTO.getId().replaceAll("^.*\\.", ""));

                        script.append(String.format("Setting = %s;\n", settingsInclude));
                        script.append("if (!(\"" + engineId + "\" in settings))\n");
                        script.append("{\n");
                        script.append("settings[\"" + engineId + "\"] = new java.util.ArrayList();\n");
                        script.append("}\n");
                        script.append("settings[\"" + engineId + "\"].add(new Setting());\n");
                    }
                }
            }
        }
        script.append("return settings;\n");
        script.append("})();\n");

        return script.toString();
    }
}
