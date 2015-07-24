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

package com.playonlinux.framework;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class SystemUtilitiesTest {
    @Test
    public void testGetFreeSpace_compareWithDf() throws IOException, InterruptedException {
        String directory = "/";

        String command = "df -P -k "+directory+"";
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        long actualValue = SystemUtilities.getFreeSpace(directory);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        reader.readLine();
        String firstList = reader.readLine();

        long expectedValue = Integer.valueOf(firstList.split("[ .]+")[3]);


        assertEquals((double)expectedValue, (double)actualValue, 500.);
    }
}