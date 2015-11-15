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
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

import org.apache.commons.io.FileUtils;


public class AnyScriptFactoryImplementation implements AnyScriptFactory {
    private final Collection<ScriptFactory<?>> scriptFactories;

    ExecutorService executorService;

    public AnyScriptFactoryImplementation() {
        this.scriptFactories = new ArrayList<>();
    }

    @Override
    public Script createInstanceFromFile(File scriptFile) throws ScriptFailureException {
        try {
            return createInstance(FileUtils.readFileToString(scriptFile));
        } catch (IOException e) {
            throw new ScriptFailureException(e);
        }
    }

    @Override
    public boolean validate(String scriptContent) {
        for(ScriptFactory<?> scriptFactory: scriptFactories) {
            if(scriptFactory.validate(scriptContent)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Class<Script> getType() {
        return Script.class;
    }

    @Override
    public Script createInstance(String scriptContent) {
        for(ScriptFactory<?> scriptFactory: scriptFactories) {
            if(scriptFactory.validate(scriptContent)) {
                return scriptFactory.withExecutor(executorService).createInstance(scriptContent);
            }
        }
        throw new IllegalArgumentException("Unknown script type");
    }

    @Override
    public AnyScriptFactoryImplementation withExecutor(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public AnyScriptFactoryImplementation withScriptFactory(ScriptFactory<?> scriptFactory) {
        this.scriptFactories.add(scriptFactory);
        return this;
    }

}
