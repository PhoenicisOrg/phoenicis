package com.playonlinux.engines;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.engines.dto.WineVersionDistributionDTO;
import com.playonlinux.scripts.interpreter.InteractiveScriptSession;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class WineVersionsManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(WineVersionsManager.class);
    private final ScriptInterpreter scriptInterpreter;
    private final ObjectMapper objectMapper;

    public WineVersionsManager(ScriptInterpreter scriptInterpreter, ObjectMapper objectMapper) {
        this.scriptInterpreter = scriptInterpreter;
        this.objectMapper = objectMapper;
    }

    public void fetchAvailableWineVersions(Consumer<List<WineVersionDistributionDTO>> callback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();


        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine().getAvailableVersions()",
                        output -> callback.accept(unSerialize(output)),
                        this::throwError
                ),
                this::throwError
        );

    }


    private List<WineVersionDistributionDTO> unSerialize(Object json) {
        try {
            return objectMapper.readValue(json.toString(), new TypeReference<List<WineVersionDistributionDTO>>() {
                // Default
            });
        } catch (IOException e) {
            LOGGER.debug("Unable to serialize wine version json");
            return Collections.emptyList();
        }
    }

    private void throwError(Exception e) {
        throw new IllegalStateException(e);
    }
}
