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

package com.playonlinux;

import com.playonlinux.app.MockPlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.injection.AbstractConfiguration;
import com.playonlinux.core.injection.Bean;
import com.playonlinux.core.log.LoggerFactory;
import com.playonlinux.core.scripts.ScriptFactory;
import com.playonlinux.core.scripts.ScriptFactoryDefaultImplementation;
import com.playonlinux.core.python.InterpreterFactory;
import com.playonlinux.core.python.JythonInterpreterFactory;
import com.playonlinux.core.services.manager.ServiceManager;

import java.io.IOException;

import static org.mockito.Mockito.mock;

public class MockContextConfig extends AbstractConfiguration {
    @Bean
    protected PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxException, IOException {
        return new MockPlayOnLinuxContext();
    }

    @Bean
    protected ServiceManager mockBackgroundServiceManager() {
        return mock(ServiceManager.class);
    }

    @Bean
    protected InterpreterFactory jythonInterpreterFactory() {
        return new JythonInterpreterFactory();
    }

    @Bean
    protected LoggerFactory logStreamFactory() {
        return new LoggerFactory();
    }

    @Bean
    protected ScriptFactory scriptFactory() {
        return new ScriptFactoryDefaultImplementation();
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }

    @Override
    public void close() {

    }
}