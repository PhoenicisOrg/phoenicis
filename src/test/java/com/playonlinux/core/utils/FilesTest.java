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

package com.playonlinux.core.utils;

import org.junit.Test;

import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FilesTest {
    @Test(expected = IllegalArgumentException.class)
    public void testIntToPosixFilePermission_invalidMode() {
        testOneCaseIntToPosixFilePermission(1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntToPosixFilePermission_invalidModeCase2() {
        testOneCaseIntToPosixFilePermission(590);
    }


    @Test
    public void testIntToPosixFilePermission_allAccess() {
        testOneCaseIntToPosixFilePermission(777,
                PosixFilePermission.GROUP_EXECUTE,
                PosixFilePermission.GROUP_READ,
                PosixFilePermission.GROUP_WRITE,
                PosixFilePermission.OTHERS_EXECUTE,
                PosixFilePermission.OTHERS_READ,
                PosixFilePermission.OTHERS_WRITE,
                PosixFilePermission.OWNER_EXECUTE,
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OTHERS_WRITE);
    }

    @Test
    public void testIntToPosixFilePermission_intermediate() {
        testOneCaseIntToPosixFilePermission(755,
                PosixFilePermission.GROUP_EXECUTE,
                PosixFilePermission.GROUP_READ,
                PosixFilePermission.OTHERS_EXECUTE,
                PosixFilePermission.OTHERS_READ,
                PosixFilePermission.OWNER_EXECUTE,
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE);
    }

    @Test
    public void testIntToPosixFilePermission_noAccess() {
        testOneCaseIntToPosixFilePermission(000);
    }

    private void testOneCaseIntToPosixFilePermission(int mode, PosixFilePermission... permissions) {
        List<PosixFilePermission> permissionsSet = new ArrayList<>(Files.intToPosixFilePermission(mode));

        assertEquals(permissions.length, permissionsSet.size());

        for(PosixFilePermission permission: permissions) {
            assertTrue(permissionsSet.contains(permission));
        }
    }
}