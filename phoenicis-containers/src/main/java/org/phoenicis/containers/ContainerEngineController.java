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

package org.phoenicis.containers;

import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EnginesManager;

import java.util.HashMap;
import java.util.function.Consumer;

public class ContainerEngineController {
    private EnginesManager enginesManager;

    public ContainerEngineController(EnginesManager enginesManager) {
        this.enginesManager = enginesManager;
    }

    public void runInContainer(ContainerDTO container, String command, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        // TODO: better way to get engine ID
        final String engineId = container.getEngine().toLowerCase();
        this.enginesManager.getEngine(engineId, engine -> {
            engine.setWorkingContainer(container.getName());
            engine.run(command, null, container.getPath(), false, true, new HashMap<>());
            doneCallback.run();
        }, errorCallback);
    }
}
