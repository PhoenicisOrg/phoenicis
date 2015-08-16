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

package com.playonlinux.configuration;

import com.playonlinux.app.MockIntegrationContext;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.injection.AbstractConfiguration;
import com.playonlinux.core.injection.Bean;
import com.playonlinux.core.log.LoggerFactory;
import com.playonlinux.core.python.DefaultJythonJythonInterpreterFactory;
import com.playonlinux.core.python.JythonInterpreterFactory;
import com.playonlinux.core.scripts.ScriptFactory;
import com.playonlinux.core.scripts.ScriptFactoryDefaultImplementation;
import com.playonlinux.core.services.manager.PlayOnLinuxServicesManager;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.mock.MockIntegrationUI;
import com.playonlinux.ui.api.Controller;
import com.playonlinux.ui.api.SetupWindow;
import com.playonlinux.ui.api.UIMessageSender;
import com.playonlinux.ui.impl.cli.UIMessageSenderCLIImplementation;

import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntegrationContextConfig extends AbstractConfiguration {
    @Bean
    protected PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxException, IOException {
        return new MockIntegrationContext();
    }

    @Bean
    protected JythonInterpreterFactory jythonInterpreterFactory() {
        return new DefaultJythonJythonInterpreterFactory();
    }

    @Bean
    protected LoggerFactory logStreamFactory() {
        return new LoggerFactory();
    }

    @Bean
    protected ScriptFactory scriptFactory() {
        return new ScriptFactoryDefaultImplementation();
    }

    @Bean
    protected ServiceManager serviceManager() throws ServiceInitializationException {
        return new PlayOnLinuxServicesManager();
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }

    @Bean
    protected Controller controller() {
        Controller mockController = mock(Controller.class);
        SetupWindow mockSetupWindow = mock(SetupWindow.class);
        when(mockController.createSetupWindowGUIInstance(anyString())).thenReturn(mockSetupWindow);
        UIMessageSender<Object> mockUIMessageSender = new UIMessageSenderCLIImplementation();
        when(mockController.createUIMessageSender()).thenReturn(mockUIMessageSender);
        when(mockController.createSetupWindowGUIInstance(anyString())).thenReturn(new MockIntegrationUI());
        return mockController;
    }
}