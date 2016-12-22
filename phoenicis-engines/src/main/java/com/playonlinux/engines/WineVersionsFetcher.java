package com.playonlinux.engines;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.engines.dto.WineVersionDistributionDTO;
import com.playonlinux.scripts.interpreter.InteractiveScriptSession;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WineVersionsFetcher {
    private final ScriptInterpreter scriptInterpreter;
    private final ObjectMapper objectMapper;

    public WineVersionsFetcher(ScriptInterpreter scriptInterpreter, ObjectMapper objectMapper) {
        this.scriptInterpreter = scriptInterpreter;
        this.objectMapper = objectMapper;
    }

    public List<WineVersionDistributionDTO> fetchAvailableWineVersions() {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();
        final List<WineVersionDistributionDTO> wineVersions = new ArrayList<>();
        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);", output -> {}, this::throwError);
        interactiveScriptSession.eval("new Wine().getAvailableVersions()", output -> wineVersions.addAll(unSerialize(output)), this::throwError);
        return wineVersions;
    }

    private Collection<? extends WineVersionDistributionDTO> unSerialize(Object json) {
        try {
            return objectMapper.readValue(json.toString(), new TypeReference<List<WineVersionDistributionDTO>>() {});
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private void throwError(Exception e) {
        throw new IllegalStateException(e);
    }
}
