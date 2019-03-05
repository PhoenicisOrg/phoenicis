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

import org.apache.commons.io.IOUtils;
import org.graalvm.polyglot.Context;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PhoenicisScriptContext {
    private final Context context;
    private final List<Consumer<Exception>> errorHandlers = new ArrayList<>();
    private final static String LANGUAGE_ID = "js";

    PhoenicisScriptContext() {
        this.context = Context.newBuilder()
                .option("js.nashorn-compat", "true")
                .allowHostAccess(true)
                .build();
    }

    public void eval(InputStreamReader inputStreamReader, Consumer<Exception> errorCallback) {
        try {
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(inputStreamReader, stringWriter);
            String streamAsString = stringWriter.toString();
            this.context.eval(LANGUAGE_ID, streamAsString);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    public void eval(String script, Consumer<Exception> errorCallback) {
        eval(script, () -> {
        }, errorCallback);
    }

    public void eval(String script, Runnable doneCallback, Consumer<Exception> errorCallback) {
        try {
            this.context.eval(LANGUAGE_ID, script);
            doneCallback.run();
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    Object evalAndReturn(String line, Consumer<Exception> errorCallback) {
        try {
            final Object evaluation = this.context.eval(LANGUAGE_ID, line);
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
            this.context.getBindings(LANGUAGE_ID).putMember(name, object);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    public void addErrorHandler(Consumer<Exception> errorHandler) {
        errorHandlers.add(errorHandler);
    }

}
