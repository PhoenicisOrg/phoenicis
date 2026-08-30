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

package org.phoenicis.cli;

import com.github.jankroken.commandline.domain.InvalidCommandLineException;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class PhoenicisCLITest {
    private final PhoenicisCLI phoenicisCLI = new PhoenicisCLI();

    @Test(expected = InvalidCommandLineException.class)
    public void testRunCliInvalidArgumentsExceptionThrown() {
        phoenicisCLI.run(new String[] { "invalid", "arguments" });
    }

    @Test
    public void testBlankScriptNoException() throws IOException {
        File tempRepositoryListFile = File.createTempFile("repositories", ".json");
        tempRepositoryListFile.deleteOnExit();

        Files.write("[{\"type\":\"classpath\",\"packagePath\":\"/org/phoenicis/cli/testRepository\"}]",
                tempRepositoryListFile, Charset.defaultCharset());

        System.setProperty("application.repository.list", tempRepositoryListFile.getPath());
        phoenicisCLI.run(new String[] { "-install", "applications", "graphics", "does-not-exist", "online" });
    }
}