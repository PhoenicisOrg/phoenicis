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

import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptException;

import java.util.function.Consumer;

public class NashornInteractiveSession implements InteractiveScriptSession {
    private final NashornEngine nashornEngine;

    public NashornInteractiveSession(NashornEngineFactory nashornEngineFactory) {
        this.nashornEngine = nashornEngineFactory.createEngine();
    }

    @Override
    public void eval(String evaluation, Consumer<Object> responseCallback, Consumer<Exception> errorCallback) {
        responseCallback.accept(nashornEngine.evalAndReturn(evaluation, errorCallback));
    }

    @Override
    public <T> void eval(String evaluation, Class<T> responseType, Consumer<T> responseCallback,
            Consumer<Exception> errorCallback) {
        // execute the given script and save the returned json object in a jsonResult variable
        final Object jsonResult = nashornEngine.evalAndReturn(evaluation, errorCallback);

        // bind the returned json object to a new variable called "jsonObject"
        nashornEngine.put("jsonObject", jsonResult, errorCallback);

        /*
         * create an anonymous class, which extends the given response type and
         * uses the implementation contained in jsonObject as its body
         */
        nashornEngine.eval(
                String.format("val InheritedClass = Java.extend(\"%s\", jsonObject)", responseType.getName()),
                errorCallback);

        // create a new instance of the anonymous class and return it
        Object result = nashornEngine.evalAndReturn("new InheritedClass()", errorCallback);
        if (responseType.isInstance(result)) {
            responseCallback.accept((T) result);
        } else {
            errorCallback.accept(new ScriptException(
                    "The resulting object from the script is not of type " + responseType.getSimpleName()));
        }
    }
}
