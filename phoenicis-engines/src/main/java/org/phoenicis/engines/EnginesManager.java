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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.repository.dto.*;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * manages the several engines provided by the repository
 */
@Safe
public class EnginesManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnginesManager.class);
    private final ScriptInterpreter scriptInterpreter;
    private final ObjectMapper objectMapper;

    /**
     * constructor
     * @param scriptInterpreter to access the javascript engine implementation
     * @param objectMapper to parse the available versions
     */
    public EnginesManager(ScriptInterpreter scriptInterpreter, ObjectMapper objectMapper) {
        this.scriptInterpreter = scriptInterpreter;
        this.objectMapper = objectMapper;
    }

    /**
     * fetches the required engine
     * @param engineId engine ID (e.g. "wine")
     * @param doneCallback callback which will be executed with the fetched engine
     * @param errorCallback callback which will be executed if an error occurs
     */
    public void getEngine(String engineId, Consumer<Engine> doneCallback, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(
                "include([\"engines\", \"" + engineId + "\", \"engine\", \"implementation\"]); new Engine();",
                output -> {
                    final Engine engine = (Engine) output;
                    doneCallback.accept(engine);
                }, errorCallback);
    }

    /**
     * fetches the available versions of a certain engine
     * @param engineId engine ID (e.g. "wine")
     * @param callback callback which will be executed with the fetched engine versions
     * @param errorCallback callback which will be executed if an error occurs
     */
    public void fetchAvailableVersions(String engineId, Consumer<List<EngineSubCategoryDTO>> callback,
            Consumer<Exception> errorCallback) {
        this.getEngine(engineId, engine -> callback.accept(unSerialize(engine.getAvailableVersions())), errorCallback);
    }

    /**
     * fetches all available engines from the repository
     * @param categoryDTOS engine categories from the repository
     * @return available engines
     */
    public List<EngineCategoryDTO> getAvailableEngines(List<CategoryDTO> categoryDTOS) {
        List<EngineCategoryDTO> engines = new ArrayList<>();
        for (CategoryDTO categoryDTO : categoryDTOS) {
            final String engineName = categoryDTO.getName();
            final EngineCategoryDTO engineCategoryDTO = new EngineCategoryDTO.Builder()
                    .withName(engineName)
                    .withDescription(engineName)
                    .withSubCategories(new ArrayList<>())
                    .build();
            engines.add(engineCategoryDTO);
        }
        return engines;
    }

    /**
     * reads available engine versions from JSON
     * @param json JSON file
     * @return available engine versions
     */
    private List<EngineSubCategoryDTO> unSerialize(Object json) {
        try {
            return objectMapper.readValue(json.toString(), new TypeReference<List<EngineSubCategoryDTO>>() {
                // Default
            });
        } catch (IOException e) {
            LOGGER.debug("Unable to unserialize engine json");
            return Collections.emptyList();
        }
    }

    /**
     * fetches the available engines
     * @param repositoryDTO
     * @param callback
     * @param errorCallback callback which will be executed if an error occurs
     */
    public void fetchAvailableEngines(RepositoryDTO repositoryDTO,
            Consumer<Map<String, Engine>> callback, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(this.createFetchScript(repositoryDTO),
                output -> callback.accept((Map<String, Engine>) output), errorCallback);
    }

    /**
     * retrieves a Javascript string which can be used to fetch the available engines
     * @param repositoryDTO repository containing the engines
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
        script.append("var engines = {};\n");
        for (CategoryDTO engine : categoryDTOS) {
            final String engineId = engine.getId();
            script.append("include([\"engines\", \"" + engineId + "\", \"engine\", \"implementation\"]);\n");
            script.append("if (!(\"" + engineId + "\" in engines))\n");
            script.append("{\n");
            script.append("engines[\"" + engineId + "\"] = new Engine();\n");
            script.append("}\n");
        }
        script.append("return engines;\n");
        script.append("})();\n");

        return script.toString();
    }

}
