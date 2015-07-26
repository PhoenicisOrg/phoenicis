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

    @Test
    public void testDetectType_passALegacyScriptCRLFSeparator_FormatIsDetected() throws IOException {
        assertEquals(Script.Type.LEGACY, Script.detectScriptType(
                        FileUtils.readFileToString(
                                new File(
                                        this.getClass()
                                                .getResource("legacyScriptExampleCRLF.sh")
                                                .getPath()
                                )
                        )
                )
        );
    }

    @Test
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
    public void testExtractSignature_bashScriptWithSignature_extracted() throws IOException, ParseException, InstallerException {
        Script legacyScriptWithSignature = new ScriptFactoryDefaultImplementation().createInstance(new File(this.getClass()
                .getResource("legacyScriptExampleWithSignature.sh").getPath()));
        String expectedSignature = "-----BEGIN PGP SIGNATURE-----\n" +
                "Version: GnuPG/MacGPG2 v2.0.17 (Darwin)\n" +
                "\n" +
                "MOCKED SIGNATURE\n" +
                "-----END PGP SIGNATURE-----";
        assertEquals(expectedSignature, legacyScriptWithSignature.extractSignature());
    }

    @Test
    public void testExtractContent_bashScriptWithSignature_extracted() throws IOException, ParseException, InstallerException {
        Script legacyScriptWithSignature = new ScriptFactoryDefaultImplementation().createInstance(new File(this.getClass()
                .getResource("legacyScriptExampleWithSignature.sh").getPath()));
        String expectedSignature = "#!/bin/bash\n" +
                "[ \"$PLAYONLINUX\" = \"\" ] && exit 0\n" +
                "source \"$PLAYONLINUX/lib/sources\"\n" +
                "\n" +
                "TITLE=\"Legacy script\"\n" +
                "\n" +
                "POL_SetupWindow_Init\n" +
                "POL_SetupWindow_message \"Test\"\n" +
                "POL_SetupWindow_Close\n" +
                "\n" +
                "exit\n";
        assertEquals(expectedSignature, legacyScriptWithSignature.extractContent());
    }



    @Test
    public void testExtractSignature_bashScriptWithSignatureCRLF_extracted() throws IOException, ParseException, InstallerException {
        Script legacyScriptWithSignature = new ScriptFactoryDefaultImplementation().createInstance(new File(this.getClass()
                .getResource("legacyScriptExampleWithSignatureCRLF.sh").getPath()));
        String expectedSignature = "-----BEGIN PGP SIGNATURE-----\r\n" +
                "Version: GnuPG/MacGPG2 v2.0.17 (Darwin)\r\n" +
                "\r\n" +
                "MOCKED SIGNATURE\r\n" +
                "-----END PGP SIGNATURE-----";
        assertEquals(expectedSignature, legacyScriptWithSignature.extractSignature());
    }

    @Test
    public void testExtractContent_bashScriptWithSignatureCRLF_extracted() throws IOException, ParseException, InstallerException {
        Script legacyScriptWithSignature = new ScriptFactoryDefaultImplementation().createInstance(new File(this.getClass()
                .getResource("legacyScriptExampleWithSignatureCRLF.sh").getPath()));
        String expectedSignature = "#!/bin/bash\r\n" +
                "[ \"$PLAYONLINUX\" = \"\" ] && exit 0\r\n" +
                "source \"$PLAYONLINUX/lib/sources\"\r\n" +
                "\r\n" +
                "TITLE=\"Legacy script\"\r\n" +
                "\r\n" +
                "POL_SetupWindow_Init\r\n" +
                "POL_SetupWindow_message \"Test\"\r\n" +
                "POL_SetupWindow_Close\r\n" +
                "\r\n" +
                "exit\r\n";
        assertEquals(expectedSignature, legacyScriptWithSignature.extractContent());
    }


    @Test(expected = ParseException.class)
    public void testExtractSignature_bashScriptWithNoSignature_exceptionThrown() throws IOException, ParseException, InstallerException {
        Script legacyScriptWithoutSignature = new ScriptFactoryDefaultImplementation().createInstance(
                new File(this.getClass().getResource("legacyScriptExample.sh").getPath()));
        legacyScriptWithoutSignature.extractSignature();
    }


    @Test
    public void testExtractSignature_pythonScriptWithSignature_extracted() throws IOException, ParseException, InstallerException {
        Script script = new ScriptFactoryDefaultImplementation().createInstance(new File(this.getClass()
                .getResource("scriptExampleWithSignature.py").getPath()));
        String expectedSignture = "-----BEGIN PGP SIGNATURE-----\n" +
                "Version: GnuPG/MacGPG2 v2.0.17 (Darwin)\n" +
                "\n" +
                "MOCKED SIGNATURE (PYTHON)\n" +
                "-----END PGP SIGNATURE-----";
        assertEquals(expectedSignture, script.extractSignature());
    }

    @Test(expected = ParseException.class)
    public void testExtractSignature_pythonScriptWithNoSignature_exceptionThrown() throws IOException, ParseException, InstallerException {
        Script script = new ScriptFactoryDefaultImplementation().createInstance(
                new File(this.getClass().getResource("scriptExample.py").getPath()));
        script.extractSignature();
    }

    @Test(expected = ParseException.class)
    public void testExtractSignature_emptyScript_exceptionThrown() throws IOException, ParseException, InstallerException {
        Script script = new ScriptFactoryDefaultImplementation().createInstance(
                new File(this.getClass().getResource("emptyScript").getPath()));
        script.extractSignature();
    }



    @Test
    public void testExtractScript_withRealScript_extracted() throws IOException, ParseException, InstallerException {
        Script legacyScriptWithSignature = new ScriptFactoryDefaultImplementation().createInstance(new File(this.getClass()
                .getResource("realScript.sh").getPath()));
        String expectedScript = "#!/bin/bash\n" +
                "\n" +
                "[ \"$PLAYONLINUX\" = \"\" ] && exit 0\n" +
                "source \"$PLAYONLINUX/lib/sources\"\n" +
                "\n" +
                "\n" +
                "PREFIX=\"JediKnightII\"\n" +
                "TITLE=\"Star wars Jedi Knight II - JediOutcast\"\n" +
                "EDITOR=\"LucasArts\"\n" +
                "EDITOR_URL=\"http://www.lucasarts.com\"\n" +
                "SCRIPTOR=\"Quentin PÂRIS\"\n" +
                "WINEVERSION=\"1.4\"\n" +
                "\n" +
                "POL_SetupWindow_Init\n" +
                "POL_Debug_Init\n" +
                "\n" +
                "#Presentation\n" +
                "POL_SetupWindow_presentation \"$TITLE\" \"$EDITOR\" \"$EDITOR_URL\" \"$SCRIPTOR\" \"$PREFIX\"\n" +
                "\n" +
                "POL_SetupWindow_InstallMethod \"CD,LOCAL\"\n" +
                "\n" +
                "if [ \"$POL_SELECTED_FILE\" ]; then\n" +
                "\tSetupIs=\"$POL_SELECTED_FILE\"\n" +
                "else\n" +
                "\tif [ \"$INSTALL_METHOD\" = \"CD\" ]; then\n" +
                "\t\tPOL_SetupWindow_cdrom\n" +
                "\t\tPOL_SetupWindow_check_cdrom \"GameData/Setup.exe\"\n" +
                "\t\tSetupIs=\"$CDROM/GameData/Setup.exe\"\n" +
                "\tfi\n" +
                "\tif [ \"$INSTALL_METHOD\" = \"LOCAL\" ]; then\n" +
                "\t\tPOL_SetupWindow_browse \"$(eval_gettext 'Please selectPrefix the setup file to run')\" \"$TITLE\"\n" +
                "\t\tSetupIs=\"$APP_ANSWER\"\n" +
                "\tfi\n" +
                "fi\n" +
                "\n" +
                "POL_Wine_SelectPrefix \"$PREFIX\"\n" +
                "POL_Wine_PrefixCreate \"$WINEVERSION\"\n" +
                "\n" +
                "POL_Wine_WaitBefore \"$TITLE\"\n" +
                "[ \"$POL_OS\" = \"Mac\" ] && Set_Managed Off\n" +
                "POL_Wine \"$SetupIs\"\n" +
                "\n" +
                "POL_Shortcut \"JediOutcast.exe\" \"$TITLE\"\n" +
                "\n" +
                "POL_SetupWindow_Close\n" +
                "exit\n";
        assertEquals(expectedScript, legacyScriptWithSignature.extractContent());
    }
}