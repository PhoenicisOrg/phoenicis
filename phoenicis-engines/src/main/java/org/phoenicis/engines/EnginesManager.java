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
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Safe
public class EnginesManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnginesManager.class);
    private final ScriptInterpreter scriptInterpreter;
    private final ObjectMapper objectMapper;

    public EnginesManager(ScriptInterpreter scriptInterpreter, ObjectMapper objectMapper) {
        this.scriptInterpreter = scriptInterpreter;
        this.objectMapper = objectMapper;
    }

    public void getEngine(String engineId, Consumer<Engine> doneCallback, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(
                "include([\"engines\", \"" + engineId + "\", \"engine\", \"java\"]); new Engine();",
                output -> {
                    final Engine engine = (Engine) output;
                    doneCallback.accept(engine);
                }, errorCallback);
    }

    public void fetchAvailableVersions(String engineId, Consumer<EngineCategoryDTO> callback,
            Consumer<Exception> errorCallback) {
        this.getEngine(engineId, engine -> {
            final String engineName = Character.toUpperCase(engineId.charAt(0)) + engineId.substring(1);
            final EngineCategoryDTO engineCategoryDTO = new EngineCategoryDTO.Builder()
                    .withName(engineName)
                    .withDescription(engineName)
                    .withSubCategories(unSerialize(engine.getAvailableVersions()))
                    .build();
            callback.accept(engineCategoryDTO);
        }, errorCallback);
    }

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

    private void throwError(Exception e) {
        throw new IllegalStateException(e);
    }
}
