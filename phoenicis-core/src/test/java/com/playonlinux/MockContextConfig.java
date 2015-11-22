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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import com.playonlinux.app.MockPlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.log.LoggerFactory;
import com.playonlinux.core.python.DefaultJythonInterpreterFactory;
import com.playonlinux.core.python.JythonInterpreterFactory;
import com.playonlinux.core.scripts.AnyScriptFactoryImplementation;
import com.playonlinux.core.scripts.Script;
import com.playonlinux.core.scripts.ScriptFactory;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.injection.AbstractConfiguration;
import com.playonlinux.injection.Bean;

public class MockContextConfig extends AbstractConfiguration {
    @Bean
    protected PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxException, IOException {
        return new MockPlayOnLinuxContext();
    }

    @Bean
    protected ServiceManager mockBackgroundServiceManager() throws ServiceInitializationException {
        final ServiceManager serviceManager = mock(ServiceManager.class);
        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((Service) args[0]).init();
            return null;
        }).when(serviceManager).register(any(Service.class));

        doAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            ((Service) args[0]).shutdown();
            return null;
        }).when(serviceManager).unregister(any(Service.class));

        return serviceManager;
    }

    @Bean
    protected JythonInterpreterFactory jythonInterpreterFactory() {
        return new DefaultJythonInterpreterFactory();
    }

    @Bean
    protected LoggerFactory logStreamFactory() {
        return new LoggerFactory();
    }

    @Bean
    protected ScriptFactory<Script> scriptFactory() {
        return new AnyScriptFactoryImplementation();
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }
}