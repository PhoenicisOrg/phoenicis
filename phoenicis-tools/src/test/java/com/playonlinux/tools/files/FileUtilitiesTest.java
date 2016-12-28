package com.playonlinux.tools.files;

import org.junit.Test;

import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileUtilitiesTest {
    private FileUtilities fileUtilities = new FileUtilities();

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
        List<PosixFilePermission> permissionsSet = new ArrayList<>(fileUtilities.intToPosixFilePermission(mode));

        assertEquals(permissions.length, permissionsSet.size());

        for(PosixFilePermission permission: permissions) {
            assertTrue(permissionsSet.contains(permission));
        }
    }
}