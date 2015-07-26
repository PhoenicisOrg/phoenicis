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

import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.*;

public class WineTest {

    @Test
    public void testFindAllExecutables() throws Exception {
        File temporaryWinePrefix = Files.createTempDir();
        File driveC = new File(temporaryWinePrefix, "drive_c");
        File programFiles = new File(driveC, "Program Files");
        // TODO: Check the symbolic link test case
        File internetExplorer = new File(programFiles, "Internet Explorer");

        internetExplorer.mkdirs();

        File iexplore = new File(internetExplorer, "iexplore.exe");
        assertTrue(iexplore.createNewFile());

        File foo = new File(programFiles, "foo.exe");
        assertTrue(foo.createNewFile());

        File bar = new File(programFiles, "bar.exe");
        assertTrue(bar.createNewFile());

        WinePrefix winePrefix = new WinePrefix(temporaryWinePrefix);
        Collection<File> executables = winePrefix.findAllExecutables();

        assertTrue(executables.contains(bar));
        assertTrue(executables.contains(foo));
        assertFalse(executables.contains(iexplore));
    }
}