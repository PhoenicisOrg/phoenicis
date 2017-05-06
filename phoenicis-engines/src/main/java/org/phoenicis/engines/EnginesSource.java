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
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class EnginesSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnginesSource.class);
    private final ScriptInterpreter scriptInterpreter;
    private final ObjectMapper objectMapper;

    public EnginesSource(ScriptInterpreter scriptInterpreter, ObjectMapper objectMapper) {
        this.scriptInterpreter = scriptInterpreter;
        this.objectMapper = objectMapper;
    }

    public void fetchAvailableEngines(Consumer<List<EngineCategoryDTO>> callback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        List<EngineCategoryDTO> engines = new ArrayList<>();
        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval("new Wine().getAvailableVersions()", output -> {
                    EngineCategoryDTO wine = new EngineCategoryDTO.Builder().withName("Wine").withDescription("Wine")
                            .withSubCategories(unSerialize(output)).build();
                    engines.add(wine);
                    callback.accept(engines);
                }, this::throwError), this::throwError);

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
