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

package com.phoenicis.scripts.interpreter;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class BackgroundScriptInterpreter implements ScriptInterpreter {
    private final ScriptInterpreter delegated;
    private final ExecutorService executorService;

    public BackgroundScriptInterpreter(ScriptInterpreter delegated, ExecutorService executorService) {
        this.delegated = delegated;
        this.executorService = executorService;
    }

    @Override
    public void runScript(String scriptContent, Consumer<Exception> errorCallback) {
        executorService.execute(() -> delegated.runScript(scriptContent, errorCallback));
    }

    @Override
    public InteractiveScriptSession createInteractiveSession() {
        final InteractiveScriptSession interactiveScriptSession = delegated.createInteractiveSession();

        return (evaluation, responseCallback, errorCallback) -> {
            executorService.execute(() -> interactiveScriptSession.eval(evaluation, responseCallback, errorCallback));
        };
    }
}
