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
import org.graalvm.polyglot.Value;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.scripts.engine.PhoenicisScriptEngineFactory;
import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Manages the several engines provided by the repository
 */
@Safe
public class EnginesManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnginesManager.class);
    private final PhoenicisScriptEngineFactory phoenicisScriptEngineFactory;
    private final ExecutorService executorService;
    private final ObjectMapper objectMapper;

    /**
     * Constructor
     *
     * @param phoenicisScriptEngineFactory The used script engine factory
     * @param executorService The executor service to allow for parallelization
     * @param objectMapper to parse the available versions
     */
    public EnginesManager(PhoenicisScriptEngineFactory phoenicisScriptEngineFactory, ExecutorService executorService,
            ObjectMapper objectMapper) {
        super();

        this.phoenicisScriptEngineFactory = phoenicisScriptEngineFactory;
        this.executorService = executorService;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetches the required engine
     *
     * @param engineId The engine ID (e.g. "wine")
     * @param doneCallback The callback which will be executed with the fetched engine
     * @param errorCallback The callback which will be executed if an error occurs
     */
    public void getEngine(String engineId, Consumer<Engine> doneCallback, Consumer<Exception> errorCallback) {
        executorService.execute(() -> {
            final PhoenicisScriptEngine phoenicisScriptEngine = phoenicisScriptEngineFactory.createEngine();

            final String include = String.format("include(\"engines.%s.engine.implementation\");", engineId);

            final Value engineClass = (Value) phoenicisScriptEngine.evalAndReturn(include, errorCallback);

            final Engine engine = engineClass.newInstance().as(Engine.class);

            doneCallback.accept(engine);
        });
    }

    /**
     * Fetches the available versions of a certain engine
     *
     * @param engineId The engine ID (e.g. "wine")
     * @param callback The callback which will be executed with the fetched engine versions
     * @param errorCallback The callback which will be executed if an error occurs
     */
    public void fetchAvailableVersions(String engineId, Consumer<List<EngineSubCategoryDTO>> callback,
            Consumer<Exception> errorCallback) {
        this.getEngine(engineId, engine -> callback.accept(deserialize(engine.getAvailableVersions())), errorCallback);
    }

    /**
     * Fetches all available engines from the repository
     *
     * @param categoryDTOS The engine categories from the repository
     * @return The available engines
     */
    public List<EngineCategoryDTO> getAvailableEngines(List<CategoryDTO> categoryDTOS) {
        return categoryDTOS.stream()
                .map(category -> {
                    final String engineName = category.getName();

                    return new EngineCategoryDTO.Builder()
                            .withName(engineName)
                            .withDescription(engineName)
                            .withSubCategories(new ArrayList<>())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Reads the available engine versions from JSON
     *
     * @param json The JSON file
     * @return The available engine versions
     */
    private List<EngineSubCategoryDTO> deserialize(Object json) {
        try {
            return objectMapper.readValue(json.toString(), new TypeReference<List<EngineSubCategoryDTO>>() {
                // Default
            });
        } catch (IOException e) {
            LOGGER.debug("Unable to deserialize engine json");
            return Collections.emptyList();
        }
    }

    /**
     * Fetches the available engines
     *
     * @param repositoryDTO The repository containing the engines
     * @param callback The callback which receives the fetched engines
     * @param errorCallback The callback which is executed if an error occurs
     */
    public void fetchAvailableEngines(RepositoryDTO repositoryDTO, Consumer<Map<String, Engine>> callback,
            Consumer<Exception> errorCallback) {
        final List<String> engineIds = repositoryDTO.getTypes().stream()
                .filter(type -> type.getId().equals("engines"))
                .flatMap(type -> type.getCategories().stream())
                .map(engine -> engine.getId().replaceAll("^.*\\.", ""))
                .collect(Collectors.toList());

        executorService.execute(() -> {
            final PhoenicisScriptEngine phoenicisScriptEngine = phoenicisScriptEngineFactory.createEngine();

            Map<String, Engine> result = engineIds.stream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            engineId -> {
                                final String include = String
                                        .format("include(\"engines.%s.engine.implementation\");", engineId);

                                final Value engineClass = (Value) phoenicisScriptEngine.evalAndReturn(include,
                                        errorCallback);

                                return engineClass.newInstance().as(Engine.class);
                            }));

            callback.accept(result);
        });
    }
}
