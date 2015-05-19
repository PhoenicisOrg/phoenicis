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

package com.playonlinux.wine;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WineInstallationTest {

    private WineInstallation wineInstallationToTest;

    @Before
    public void getSystemProperties() throws Exception {
        URL url = this.getClass().getResource(".");
        this.wineInstallationToTest = new WineInstallation.Builder().withPath(new File(url.getPath())).build();
    }

    @Test
    public void testRun_RunWineVersion_ProcessRunsAndReturnsVersion() throws IOException {
        Process wineProcess = this.wineInstallationToTest.run(new File("/tmp"), "--version", null);

        InputStream inputStream = wineProcess.getInputStream();
        String processOutput = IOUtils.toString(inputStream);

        assertEquals("wine-1.7.26\n", processOutput);
    }

    @Test
    public void testRun_RunWineVersionWithArgument_ProcessReturnsHelpMessage() throws IOException {
        List<String> arguments = new ArrayList<>();
        arguments.add("/tmp/unexisting");

        Process wineProcess = this.wineInstallationToTest.run(new File("/tmp"), "--help", null, arguments);

        InputStream inputStream = wineProcess.getInputStream();
        String processOutput = IOUtils.toString(inputStream);

        assertEquals("Usage: wine PROGRAM [ARGUMENTS...]   Run the specified program\n" +
                "       wine --help                   Display this help and exit\n" +
                "       wine --version                Output version information and exit\n", processOutput);
    }

    @Test
    public void testRun_RunWineVersionWithArgument_ProcessDoesNotReturnHepMessage() throws IOException {
        List <String> arguments = new ArrayList<>();
        arguments.add("--help");

        Process wineProcess = this.wineInstallationToTest.run(new File("/tmp"), "/tmp/unexisting", null, arguments);

        InputStream inputStream = wineProcess.getInputStream();
        String processOutput = IOUtils.toString(inputStream);

        assertNotEquals("Usage: wine PROGRAM [ARGUMENTS...]   Run the specified program\n" +
                "       wine --help                   Display this help and exit\n" +
                "       wine --version                Output version information and exit\n", processOutput);
    }



}