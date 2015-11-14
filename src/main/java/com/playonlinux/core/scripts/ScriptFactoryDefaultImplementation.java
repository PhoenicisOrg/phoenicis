/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.core.scripts;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.apache.commons.io.FileUtils;

public class ScriptFactoryDefaultImplementation implements ScriptFactory {
    ExecutorService executorService;

    @Override
    public Script createInstance(File scriptFile) throws ScriptFailureException {
        try {
            return createInstance(FileUtils.readFileToString(scriptFile));
        } catch (IOException e) {
            throw new ScriptFailureException(e);
        }
    }

    @Override
    public Script createInstance(String scriptContent) {
        switch (Script.detectScriptType(scriptContent)) {
            case LEGACY:
                return new ScriptLegacy(scriptContent, executorService);
            case RECENT:
            default:
                return new ScriptRecent(scriptContent, executorService);
        }
    }

    @Override
    public ScriptFactoryDefaultImplementation withExecutor(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

}
