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

package org.phoenicis.tools.system.opener;

import org.phoenicis.configuration.security.Safe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Safe
public class OpenerProcessImplementation implements Opener {
    private static final Set<String> ALLOWED_COMMANDS = new HashSet<>(Arrays.asList("xdg-open", "open", "gio"));
    private final String commandName;

    public OpenerProcessImplementation(String commandName) {
        if (!ALLOWED_COMMANDS.contains(commandName)) {
            throw new IllegalArgumentException("Unsupported opener command: " + commandName);
        }

        this.commandName = commandName;
    }

    @Override
    public void open(String file) {
        final String sanitizedPath = sanitizePath(file);
        final ProcessBuilder processBuilder = new ProcessBuilder(commandName, sanitizedPath);
        try {
            processBuilder.start().waitFor();
        } catch (InterruptedException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String sanitizePath(String file) {
        try {
            final Path candidate = Paths.get(file).toAbsolutePath().normalize();
            if (!Files.exists(candidate)) {
                throw new IllegalArgumentException("File does not exist: " + candidate);
            }
            return candidate.toString();
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Invalid file path", e);
        }
    }

}
