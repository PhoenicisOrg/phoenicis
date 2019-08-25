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
import org.phoenicis.repository.dto.TypeDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.phoenicis.scripts.session.InteractiveScriptSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Manages the engine tools
 */
public class EngineToolsManager {
    private final ScriptInterpreter scriptInterpreter;

    /**
     * Constructor
     *
     * @param scriptInterpreter The underlying script interpreter
     */
    public EngineToolsManager(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
    }

    /**
     * runs a tool in a given container
     *
     * @param engineId ID of the engine which provides the tool (e.g. "Wine")
     * @param container name of the container
     * @param toolId ID of the tool
     * @param doneCallback callback executed after the script ran
     * @param errorCallback callback executed in case of an error
     */
    public void runTool(String engineId, String container, String toolId, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        final String include = String.format("include(\"engines.%s.tools.%s\");", engineId, toolId);

        interactiveScriptSession.eval(include,
                output -> {
                    final Value toolClass = (Value) output;

                    final EngineTool tool = toolClass.newInstance().as(EngineTool.class);

                    tool.run(container);

                    doneCallback.run();
                },
                errorCallback);
    }

    /**
     * Fetches the available engine tools
     *
     * @param repositoryDTO The repository containing the engine tools
     * @param callback The callback taking the fetched engine tools
     */
    public void fetchAvailableEngineTools(RepositoryDTO repositoryDTO, Consumer<Map<String, ApplicationDTO>> callback) {
        // get engine CategoryDTOs
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (TypeDTO typeDTO : repositoryDTO.getTypes()) {
            if (typeDTO.getId().equals("engines")) {
                categoryDTOS = typeDTO.getCategories();
            }
        }

        Map<String, ApplicationDTO> tools = new HashMap<>();
        for (CategoryDTO engine : categoryDTOS) {
            for (ApplicationDTO applicationDTO : engine.getApplications()) {
                if (applicationDTO.getId().equals(engine.getId() + ".tools")) {
                    tools.put(engine.getId().replaceAll("^.*\\.", ""), applicationDTO);
                }
            }
        }
        callback.accept(tools);
    }
}
