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
import org.phoenicis.scripts.engine.PhoenicisScriptEngineFactory;
import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Manages the engine settings
 */
public class EngineSettingsManager {
    private final PhoenicisScriptEngineFactory phoenicisScriptEngineFactory;
    private final ExecutorService executorService;

    /**
     * Constructor
     *
     * @param phoenicisScriptEngineFactory The used script engine factory
     * @param executorService The executor service to allow for parallelization
     */
    public EngineSettingsManager(PhoenicisScriptEngineFactory phoenicisScriptEngineFactory,
            ExecutorService executorService) {
        super();

        this.phoenicisScriptEngineFactory = phoenicisScriptEngineFactory;
        this.executorService = executorService;
    }

    /**
     * Fetches the available engine settings
     *
     * @param repositoryDTO The repository containing the engine settings
     * @param callback The callback which recieves the found engine settings
     * @param errorCallback The callback which will be executed if an error occurs
     */
    public void fetchAvailableEngineSettings(RepositoryDTO repositoryDTO,
            Consumer<Map<String, List<EngineSetting>>> callback, Consumer<Exception> errorCallback) {
        executorService.execute(() -> {
            final List<SettingConfig> configurations = fetchSettingConfigurations(repositoryDTO);

            // the script engine needs to be created inside the correct thread otherwise GraalJS throwa an error
            final PhoenicisScriptEngine phoenicisScriptEngine = phoenicisScriptEngineFactory.createEngine();

            final Map<String, List<EngineSetting>> result = configurations.stream()
                    .collect(Collectors.groupingBy(
                            configuration -> configuration.engineId,
                            Collectors.mapping(configuration -> {
                                final String include = String.format("include(\"engines.%s.settings.%s\");",
                                        configuration.engineId, configuration.settingId);

                                final Value settingClass = (Value) phoenicisScriptEngine.evalAndReturn(include,
                                        errorCallback);

                                return settingClass.newInstance().as(EngineSetting.class);
                            }, Collectors.toList())));

            callback.accept(result);
        });
    }

    /**
     * Fetches a list of the setting parameters requires to fetch all settings in the given repository
     *
     * @param repositoryDTO The repository containing the settings
     * @return A list containing all necessary setting parameters
     */
    private List<SettingConfig> fetchSettingConfigurations(RepositoryDTO repositoryDTO) {
        // get engine CategoryDTOs
        List<CategoryDTO> categoryDTOs = repositoryDTO.getTypes().stream()
                .filter(typeDTO -> typeDTO.getId().equals("engines"))
                .flatMap(typeDTO -> typeDTO.getCategories().stream())
                .collect(Collectors.toList());

        return categoryDTOs.stream()
                // map category -> (engineId, application)
                .flatMap(engine -> {
                    final String engineId = engine.getId().replaceAll("^.*\\.", "");

                    return engine.getApplications().stream()
                            .map(application -> new EngineInformation(engineId, application));
                })
                // filter to setting applications
                .filter(engineInformation -> {
                    final String applicationId = engineInformation.application.getId();
                    final String settingIdPrefix = String.format("engines.%s.settings", engineInformation.engineId);

                    return applicationId.equals(settingIdPrefix);
                })
                // map (engineId, application) -> (engineId, settingId)
                .flatMap(engineInformation -> engineInformation.application.getScripts().stream()
                        .map(script -> {
                            final String settingId = script.getId().replaceAll("^.*\\.", "");

                            return new SettingConfig(engineInformation.engineId, settingId);
                        }))
                .collect(Collectors.toList());
    }

    private class EngineInformation {
        public final String engineId;

        public final ApplicationDTO application;

        private EngineInformation(String engineId, ApplicationDTO application) {
            this.engineId = engineId;
            this.application = application;
        }
    }

    private class SettingConfig {
        public final String engineId;

        public final String settingId;

        public SettingConfig(String engineId, String settingId) {
            this.engineId = engineId;
            this.settingId = settingId;
        }
    }
}
