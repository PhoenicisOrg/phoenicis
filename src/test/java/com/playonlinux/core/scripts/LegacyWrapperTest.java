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
import org.junit.Test;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LegacyWrapperTest {


    @Before
    public void setUp() throws InjectionException {
        AbstractConfiguration testConfigFile = new MockContextConfig();
        testConfigFile.setStrictLoadingPolicy(false);
        testConfigFile.load();
    }

    @Test
    public void testLegacyWrapper() throws Exception {
        File tmpFile = new File("/tmp/POL_WrapperTest");
        //ensure temporary file does not exist before running the testScript
        if(tmpFile.exists()){
            tmpFile.delete();
        }
        File testScript = new File(this.getClass().getResource("wrapperTestScript.sh").getPath());
        ExecutorService mockExecutorService = mock(ExecutorService.class);
        Future mockFuture = mock(Future.class);
        when(mockExecutorService.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Runnable runnable = (Runnable) args[0];
            runnable.run();
            return mockFuture;
        });

        final ScriptFactory scriptFactory = new ScriptFactoryDefaultImplementation()
                .withExecutor(mockExecutorService);

        final Script testScriptWrapper = scriptFactory
                .createInstance(testScript);

        testScriptWrapper.start();
        //file should exist now
        assertTrue(tmpFile.exists());
    }



}
