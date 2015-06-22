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

import com.playonlinux.installer.Script;
import com.playonlinux.installer.ScriptFactoryDefaultImplementation;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.*;

public class ScriptTest {


    @Test
    public void testDetectType_passALegacyScript_FormatIsDetected() throws IOException {
        assertEquals(Script.Type.LEGACY, Script.detectScriptType(
                        FileUtils.readFileToString(
                                new File(
                                        this.getClass()
                                                .getResource("legacyScriptExample.sh")
                                                .getPath()
                                )
                        )
                )
        );
    }

    public void testDetectType_passALegacyScriptWithHeader_FormatIsDetected() throws IOException {
        assertEquals(Script.Type.LEGACY, Script.detectScriptType(
                        FileUtils.readFileToString(
                                new File(
                                        this.getClass()
                                                .getResource("legacyScriptExampleWithPlayOnLinuxBashHeader.sh")
                                                .getPath()
                                )
                        )
                )
        );

    }

    @Test
    public void testDetectType_passARecentScript_FormatIsDetected() throws IOException {
        assertEquals(Script.Type.RECENT, Script.detectScriptType(
                        FileUtils.readFileToString(
                                new File(
                                        this.getClass()
                                                .getResource("scriptExample.py")
                                                .getPath()
                                )
                        )
                )
        );
    }

    @Test
    public void testExtractSignature_bashScriptWithSignature_extracted() throws IOException, ParseException {
        Script legacyScriptWithSignature = ScriptFactoryDefaultImplementation.createInstance(new File(this.getClass()
                .getResource("legacyScriptExampleWithSignature.sh").getPath()));
        String expectedSignture = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
                "Version: GnuPG/MacGPG2 v2.0.17 (Darwin)\n" +
                "\n" +
                "MOCKED SIGNATURE\n" +
                "-----END PGP PUBLIC KEY BLOCK-----";
        assertEquals(expectedSignture, legacyScriptWithSignature.extractSignature());
    }

    @Test(expected = ParseException.class)
    public void testExtractSignature_bashScriptWithNoSignature_exceptionThrown() throws IOException, ParseException {
        Script legacyScriptWithoutSignature = ScriptFactoryDefaultImplementation.createInstance(
                new File(this.getClass().getResource("legacyScriptExample.sh").getPath()));
        legacyScriptWithoutSignature.extractSignature();
    }


    @Test
    public void testExtractSignature_pythonScriptWithSignature_extracted() throws IOException, ParseException {
        Script script = ScriptFactoryDefaultImplementation.createInstance(new File(this.getClass()
                .getResource("scriptExampleWithSignature.py").getPath()));
        String expectedSignture = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
                "Version: GnuPG/MacGPG2 v2.0.17 (Darwin)\n" +
                "\n" +
                "MOCKED SIGNATURE (PYTHON)\n" +
                "-----END PGP PUBLIC KEY BLOCK-----";
        assertEquals(expectedSignture, script.extractSignature());
    }

    @Test(expected = ParseException.class)
    public void testExtractSignature_pythonScriptWithNoSignature_exceptionThrown() throws IOException, ParseException {
        Script script = ScriptFactoryDefaultImplementation.createInstance(
                new File(this.getClass().getResource("scriptExample.py").getPath()));
        script.extractSignature();
    }

    @Test(expected = ParseException.class)
    public void testExtractSignature_emptyScript_exceptionThrown() throws IOException, ParseException {
        Script script = ScriptFactoryDefaultImplementation.createInstance(
                new File(this.getClass().getResource("emptyScript").getPath()));
        script.extractSignature();
    }
}