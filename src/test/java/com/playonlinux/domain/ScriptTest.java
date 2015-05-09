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

package com.playonlinux.domain;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ScriptTest {


    @Test
    public void testDetectType_passALegacyScript_FormatIsDetected() throws Exception {
        Script legacyScript = new Script(new File(this.getClass().getResource("legacyScriptExemple.sh").getPath()));

        assertEquals(Script.Type.LEGACY, legacyScript.detectScriptType());
    }

    @Test
    public void testDetectType_passARecentScript_FormatIsDetected() throws Exception {
        Script legacyScript = new Script(new File(this.getClass().getResource("scriptExemple.py").getPath()));

        assertEquals(Script.Type.RECENT, legacyScript.detectScriptType());
    }

    @Test
    public void testExtractSignature_bashScriptWithSignature_extracted() throws IOException, PlayOnLinuxError {
        Script legacyScriptWithSignature = new Script(new File(this.getClass()
                .getResource("legacyScriptExempleWithSignature.sh").getPath()));
        String expectedSignture = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
                "Version: GnuPG/MacGPG2 v2.0.17 (Darwin)\n" +
                "\n" +
                "MOCKED SIGNATURE\n" +
                "-----END PGP PUBLIC KEY BLOCK-----";
        assertEquals(expectedSignture, legacyScriptWithSignature.extractSignature());
    }

    @Test(expected = PlayOnLinuxError.class)
    public void testExtractSignature_bashScriptWithNoSignature_exceptionThrown() throws IOException, PlayOnLinuxError {
        Script legacyScriptWithoutSignature = new Script(new File(this.getClass().getResource("legacyScriptExemple.sh").getPath()));
        legacyScriptWithoutSignature.extractSignature();
    }

}