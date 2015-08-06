/*
 * Copyright (C) 2015 Markus Ebner
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

import com.playonlinux.MockContextConfig;
import com.playonlinux.core.injection.AbstractConfiguration;
import com.playonlinux.core.injection.InjectionException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class LegacyWrapperTest {
    @Before
    public void setUp() throws InjectionException {
        AbstractConfiguration testConfigFile = new MockContextConfig();
        testConfigFile.setStrictLoadingPolicy(false);
        testConfigFile.load();
    }

    @Ignore
    @Test
    public void testLegacyWrapper() throws Exception {
        final File tmpFile = File.createTempFile("temporary", "text");
        tmpFile.mkdirs();
        final File scriptFile = File.createTempFile("script", "sh");
        scriptFile.deleteOnExit();
        tmpFile.deleteOnExit();

        final PrintWriter printWriter = new PrintWriter(scriptFile);

        final String scriptContent = "#!/bin/bash\n" +
                "[ \"$PLAYONLINUX\" = \"\" ] && exit 0\n" +
                "source \"$PLAYONLINUX/lib/sources\"\n" +
                "\n" +
                "TITLE=\"Legacy script\"\n" +
                "echo \"Inside bash\"\n" +
                "touch "+tmpFile.getAbsolutePath()+"\n" +
                "\n" +
                "exit";

        printWriter.println(scriptContent);
        printWriter.close();

        if(tmpFile.exists() && !tmpFile.delete()) {
            fail();
        }

        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final ScriptFactory scriptFactory = new ScriptFactoryDefaultImplementation()
                .withExecutor(executorService);

        final Script testScriptWrapper = scriptFactory.createInstance(scriptFile);

        testScriptWrapper.init();

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertTrue(tmpFile.exists());
        tmpFile.delete();
    }



}
