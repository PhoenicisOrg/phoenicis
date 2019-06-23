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

package org.phoenicis.scripts.engine;

import org.phoenicis.scripts.engine.implementation.PhoenicisSandbox;
import org.phoenicis.scripts.engine.injectors.EngineInjector;
import org.phoenicis.scripts.engine.implementation.PhoenicisScriptEngine;

import java.util.List;

public class PhoenicisScriptEngineFactory {
    private final ScriptEngineType type;
    private final PhoenicisSandbox phoenicisSandbox;
    private final List<EngineInjector> engineInjectors;

    public PhoenicisScriptEngineFactory(PhoenicisSandbox phoenicisSandbox, ScriptEngineType type,
            List<EngineInjector> engineInjectors) {
        this.type = type;
        this.engineInjectors = engineInjectors;
        this.phoenicisSandbox = phoenicisSandbox;
    }

    public PhoenicisScriptEngine createEngine() {
        final PhoenicisScriptEngine phoenicisScriptEngine = type.createScriptEngine(this.phoenicisSandbox);

        engineInjectors.forEach(engineInjector -> engineInjector.injectInto(phoenicisScriptEngine));

        return phoenicisScriptEngine;
    }
}
