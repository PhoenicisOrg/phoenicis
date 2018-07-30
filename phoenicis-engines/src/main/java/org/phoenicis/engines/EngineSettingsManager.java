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

import org.phoenicis.repository.dto.*;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * manages the engine settings
 */
public class EngineSettingsManager {
    private final ScriptInterpreter scriptInterpreter;

    /**
     * constructor
     * @param scriptInterpreter
     */
    public EngineSettingsManager(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
    }

    /**
     * fetches the required setting
     * @param engineId setting ID (e.g. "glsl")
     * @param settingId setting ID (e.g. "glsl")
     * @param doneCallback callback which will be executed with the fetched setting
     * @param errorCallback callback which will be executed if an error occurs
     */
    public void getSetting(String engineId, String settingId, Consumer<EngineSetting> doneCallback,
            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(
                "include([\"engines\", \"" + engineId + "\", \"settings\", \"" + settingId + "\"]); new Setting();",
                output -> {
                    final EngineSetting setting = (EngineSetting) output;
                    doneCallback.accept(setting);
                }, errorCallback);
    }

    /**
     * fetches the available engine settings
     * @param repositoryDTO
     * @param callback
     * @param errorCallback callback which will be executed if an error occurs
     */
    public void fetchAvailableEngineSettings(RepositoryDTO repositoryDTO,
            Consumer<Map<String, List<EngineSetting>>> callback, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(this.createFetchScript(repositoryDTO),
                output -> callback.accept((Map<String, List<EngineSetting>>) output), errorCallback);
    }

    /**
     * retrieves a Javascript string which can be used to fetch the available settings
     * @param repositoryDTO repository containing the settings
     * @return Javascript
     */
    private String createFetchScript(RepositoryDTO repositoryDTO) {
        // get engine CategoryDTOs
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (TypeDTO typeDTO : repositoryDTO.getTypes()) {
            if (typeDTO.getId().equals("engines")) {
                categoryDTOS = typeDTO.getCategories();
            }
        }
        StringBuilder script = new StringBuilder();
        script.append("(function () {\n");
        script.append("var settings = {};\n");
        for (CategoryDTO engine : categoryDTOS) {
            final String engineId = engine.getId();
            for (ApplicationDTO applicationDTO : engine.getApplications()) {
                if (applicationDTO.getId().equals("settings")) {
                    for (ScriptDTO scriptDTO : applicationDTO.getScripts()) {
                        script.append("include([\"engines\", \"" + engineId + "\", \"settings\", \"" + scriptDTO.getId()
                                + "\"]);\n");
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
