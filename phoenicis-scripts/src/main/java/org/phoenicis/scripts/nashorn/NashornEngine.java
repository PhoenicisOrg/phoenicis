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

import javax.script.ScriptEngine;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class NashornEngine {
    private final ScriptEngine scriptEngine;
    private final List<Consumer<Exception>> errorHandlers = new ArrayList<>();

    NashornEngine(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    public void eval(InputStreamReader inputStreamReader, Consumer<Exception> errorCallback) {
        try {
            this.scriptEngine.eval(inputStreamReader);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    public void eval(String script, Consumer<Exception> errorCallback) {
        eval(script, Optional.empty(), errorCallback);
    }

    public void eval(String script, Optional<Runnable> doneCallback, Consumer<Exception> errorCallback) {
        try {
            this.scriptEngine.eval(script);
            doneCallback.ifPresent(Runnable::run);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    Object evalAndReturn(String line, Consumer<Exception> errorCallback) {
        try {
            final Object evaluation = this.scriptEngine.eval(line);
            if (evaluation == null) {
                return null;
            }
            return evaluation;
        } catch (Exception e) {
            handleError(errorCallback, e);
            return "";
        }
    }

    private void handleError(Consumer<Exception> errorCallback, Exception e) {
        for (Consumer<Exception> errorHandler : errorHandlers) {
            errorHandler.accept(e);
        }
        errorCallback.accept(e);
    }

    public void put(String name, Object object, Consumer<Exception> errorCallback) {
        try {
            this.scriptEngine.put(name, object);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    public void addErrorHandler(Consumer<Exception> errorHandler) {
        errorHandlers.add(errorHandler);
    }

}
