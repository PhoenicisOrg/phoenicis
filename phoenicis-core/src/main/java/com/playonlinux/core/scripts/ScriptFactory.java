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
import java.util.concurrent.ExecutorService;

/**
 * {@link Script} factory
 */
public interface ScriptFactory {
    /**
     * Create an instance of a Script
     * @param scriptContent the content of the script
     * @return The script object
     */
    Script createInstance(String scriptContent);

    /**
     * Create an instance of a Script
     * @param scriptPath the path of the script
     * @return The script object
     * @throws ScriptFailureException if the file cannot be opened
     */
    Script createInstance(File scriptPath) throws ScriptFailureException;

    /**
     * Override the default executor
     * @param executorService The new executor
     * @return the same factory
     */
    ScriptFactory withExecutor(ExecutorService executorService);
}
