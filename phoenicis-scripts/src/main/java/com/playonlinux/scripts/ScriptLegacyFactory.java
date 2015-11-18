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

package com.playonlinux.scripts;

import java.util.concurrent.ExecutorService;

import com.playonlinux.core.scripts.Script;
import com.playonlinux.core.scripts.ScriptFactory;

public class ScriptLegacyFactory implements ScriptFactory<ScriptLegacy> {
    private ExecutorService executorService;

    @Override
    public boolean validate(String scriptContent) {
        final String firstLine = scriptContent.split("\n")[0];

        return firstLine.contains("#!/bin/bash") || firstLine.contains("#!/usr/bin/env playonlinux-bash");
    }

    @Override
    public Class<ScriptLegacy> getType() {
        return ScriptLegacy.class;
    }

    @Override
    public Script createInstance(String scriptContent) {
        return new ScriptLegacy(scriptContent, executorService);
    }

    @Override
    public ScriptLegacyFactory withExecutor(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }
}
