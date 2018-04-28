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
import java.util.Map;
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
                "include([\"engines\", \"" + engineId + "\", \"engine\", \"object\"]); new Engine();",
                output -> {
                    final Engine engine = (Engine) output;
                    doneCallback.accept(engine);
                }, errorCallback);
    }

    public void fetchAvailableEngines(List<CategoryDTO> categoryDTOS, Consumer<List<EngineCategoryDTO>> callback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        StringBuilder includesBuilder = new StringBuilder();
        StringBuilder constructorsBuilder = new StringBuilder();
        constructorsBuilder.append("function fetchEngines() {\n");
        constructorsBuilder.append("var engines = [];\n");
        for (CategoryDTO categoryDTO : categoryDTOS) {
            final String engineId = categoryDTO.getId();
            includesBuilder.append("include([\"engines\", \"" + engineId + "\", \"engine\", \"java\"]);\n");
            final String engineName = categoryDTO.getName();
            constructorsBuilder
                    .append("engines[\"" + engineName + "\"] = new Engine().getAvailableVersions();\n");
        }
        constructorsBuilder.append("return engines;\n");
        constructorsBuilder.append("}\n");
        constructorsBuilder.append("fetchEngines();");
        interactiveScriptSession.eval(includesBuilder.toString(),
                ignored -> interactiveScriptSession.eval(constructorsBuilder.toString(),
                        output -> {
                            List<EngineCategoryDTO> engines = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : ((Map<String, Object>) output).entrySet()) {
                                final EngineCategoryDTO engineCategoryDTO = new EngineCategoryDTO.Builder()
                                        .withName(entry.getKey())
                                        .withDescription(entry.getKey())
                                        .withSubCategories(unSerialize(entry.getValue()))
                                        .build();
                                engines.add(engineCategoryDTO);
                            }
                            callback.accept(engines);
                        }, this::throwError),
                this::throwError);
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
