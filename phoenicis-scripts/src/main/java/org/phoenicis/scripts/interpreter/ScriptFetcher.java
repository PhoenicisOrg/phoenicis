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

package org.phoenicis.scripts.interpreter;

import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.ScriptDTO;

import java.util.Arrays;
import java.util.List;

public class ScriptFetcher {
    private final RepositoryManager repositoryManager;

    public ScriptFetcher(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    public String getScript(List<String> path) {
        final ScriptDTO script = repositoryManager.getScript(path);

        if (script == null) {
            throw new ScriptException("Script not found: " + path);
        }

        return script.getScript();
    }

    public String getScript(String... path) {
        return getScript(Arrays.asList(path));
    }

}
