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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ScriptRecentTest {


    private AnyScriptFactory factory = new AnyScriptFactoryImplementation()
            .withScriptFactory(new ScriptRecentFactory()
            );


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
    public void testExtractSignature_pythonScriptWithSignature_extracted() throws ScriptFailureException {
        Script script = factory.createInstanceFromFile(new File(this.getClass()
                .getResource("scriptExampleWithSignature.py").getPath()));
        String expectedSignture = "-----BEGIN PGP SIGNATURE-----\n" +
                "Version: GnuPG/MacGPG2 v2.0.17 (Darwin)\n" +
                "\n" +
                "MOCKED SIGNATURE (PYTHON)\n" +
                "-----END PGP SIGNATURE-----";
        assertEquals(expectedSignture, script.extractSignature());
    }


    @Test
    public void testExtractContent_pythonScriptWithSignature_extracted() throws ScriptFailureException {
        final Script script = factory.createInstanceFromFile(new File(this.getClass()
                .getResource("scriptExampleWithSignature.py").getPath()));


        assertEquals("from com.playonlinux.framework import SetupWizard\n" +
                "\n" +
                "setupWindow = SetupWizard(\"TITLE\")\n" +
                "\n" +
                "print \"Hello from python!\"\n" +
                "\n" +
                "# -----BEGIN PGP SIGNATURE-----\n" +
                "# Version: GnuPG/MacGPG2 v2.0.17 (Darwin)\n" +
                "\n" +
                "# MOCKED SIGNATURE (PYTHON)\n" +
                "# -----END PGP SIGNATURE-----\n" +
                "\n" +
                "setupWindow.message(\"Test\\nTest\")\n" +
                "setupWindow.message(\"Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 \" +\n" +
                "                          \"Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 \" +\n" +
                "                          \"Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 \");\n" +
                "\n" +
                "result = setupWindow.textbox(\"Test 3\")\n" +
                "print result\n" +
                "\n" +
                "setupWindow.message(\"Test 4\")\n" +
                "setupWindow.message(\"Test 5\")\n" +
                "\n", script.extractContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractSignature_pythonScriptWithNoSignature_exceptionThrown() throws ScriptFailureException {
        Script script = factory.createInstanceFromFile(
                new File(this.getClass().getResource("scriptExample.py").getPath()));
        script.extractSignature();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractSignature_emptyScript_exceptionThrown() throws ScriptFailureException {
        Script script = factory.createInstanceFromFile(
                new File(this.getClass().getResource("emptyScript").getPath()));
        script.extractSignature();
    }


}