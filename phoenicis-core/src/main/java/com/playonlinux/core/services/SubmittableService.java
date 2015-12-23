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

package com.playonlinux.core.services;

import java.util.function.Consumer;

import com.playonlinux.core.services.manager.Service;

/**
 * Represents a service that can receive taskes
 * @param <T> The type of the tasks
 * @param <U> The type of the callbacks
 */
public interface SubmittableService<T, U>
        extends Service {

    /**
     * Submit a task
     * @param task The task to submit
     * @param callback The callback if the the action is successful
     * @param error The callback if any error occur
     */
    void submit(T task, U callback, Consumer<Exception> error);
}
