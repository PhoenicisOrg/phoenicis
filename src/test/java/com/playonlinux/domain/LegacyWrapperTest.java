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

package com.playonlinux.domain;

import com.playonlinux.TestContextConfig;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.MockPlayOnLinuxContext;
import com.playonlinux.injection.AbstractConfigFile;
import com.playonlinux.injection.Bean;
import com.playonlinux.injection.InjectionException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class LegacyWrapperTest {


    @Before
    public void setUp() throws InjectionException {
        AbstractConfigFile testConfigFile = new TestContextConfig();
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
        Script testScriptWrapper = Script.createInstance(testScript);
        testScriptWrapper.executeInterpreter();
        //file should exist now
        assertTrue(tmpFile.exists());
    }

}
