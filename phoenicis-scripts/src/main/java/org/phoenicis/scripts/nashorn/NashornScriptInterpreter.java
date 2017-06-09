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

package org.phoenicis.scripts.nashorn;

import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.dto.TranslationDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

public class NashornScriptInterpreter implements ScriptInterpreter {
    private final NashornEngineFactory nashornEngineFactory;
    private Set<TranslationDTO> translations = Collections.emptySet();

    public NashornScriptInterpreter(NashornEngineFactory nashornEngineFactory) {
        this.nashornEngineFactory = nashornEngineFactory;
    }

    @Override
    public void setRepository(RepositoryDTO repositoryDTO) {
        this.translations = repositoryDTO.getTranslations();
    }

    @Override
    public void runScript(String scriptContent, Consumer<Exception> errorCallback) {
        final String scriptContenti18n = "var tr = Packages.org.phoenicis.configuration.localisation.Localisation.tr;"
                + scriptContent;
        nashornEngineFactory.createEngine().eval(scriptContenti18n, errorCallback);
    }

    @Override
    public InteractiveScriptSession createInteractiveSession() {
        return new NashornInteractiveSession(nashornEngineFactory);
    }

}
