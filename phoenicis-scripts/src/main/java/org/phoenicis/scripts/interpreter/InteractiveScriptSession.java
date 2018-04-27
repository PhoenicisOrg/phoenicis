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

import java.util.function.Consumer;

public interface InteractiveScriptSession {
    void eval(String evaluation, Consumer<Object> responseCallback, Consumer<Exception> errorCallback);

    /**
     * Evaluates the given script {@link String evaluation}.
     * The resulting json object of the script evaluation is used to create a new Java object of type {@link T}, which is
     * passed to the {@link Consumer<T> responseCallback} function.
     * <p>
     * If an error occurs during this process, the corresponding exception gets passed to the {@link Consumer<Exception> errorCallback} function.
     *
     * @param evaluation       The script to be evaluated. This script needs to return a json object
     * @param responseType     The return class of the script
     * @param responseCallback The response callback method
     * @param errorCallback    The error callback method
     * @param <T>              The return type of the script
     */
    <T> void eval(String evaluation, Class<T> responseType, Consumer<T> responseCallback,
            Consumer<Exception> errorCallback);
}
