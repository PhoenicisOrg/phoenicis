package com.playonlinux.scripts;

import org.junit.Test;

import java.io.File;

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


}