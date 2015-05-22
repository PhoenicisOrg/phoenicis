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

package com.playonlinux.framework;

import com.playonlinux.api.ui.Controller;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.injection.AbstractConfigFile;
import com.playonlinux.injection.Bean;
import com.playonlinux.injection.InjectionException;
import com.playonlinux.ui.impl.mockui.MockUIMessageSenderImplementation;
import com.playonlinux.ui.impl.mockui.setupwindow.MockUISetupWindowImplementation;
import com.playonlinux.domain.CancelException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SetupWizardTest {
    private SetupWizard setupWizard;
    private Controller controllerMock = mock(Controller.class);

    class TestContextConfig extends AbstractConfigFile {
        @Bean
        public Controller controller() {
            return controllerMock;
        }

        @Override
        protected String definePackage() {
            return "com.playonlinux";
        }

        @Bean
        protected PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxError, IOException {
            return new PlayOnLinuxContext();
        }
    }

    @Before
    public void setUp() throws InjectionException {
        TestContextConfig testContextConfig = new TestContextConfig();
        testContextConfig.setStrictLoadingPolicy(false);
        testContextConfig.load();

        when(controllerMock.createSetupWindowGUIInstance("Title")).thenReturn(new MockUISetupWindowImplementation());
        when(controllerMock.createUIMessageSender()).thenReturn(new MockUIMessageSenderImplementation());

        this.setupWizard = new SetupWizard("Title");
    }

    @Test
    public void testClose() throws Exception {

    }

    @Test
    public void testMessage() throws Exception, CancelException {
        this.setupWizard.message("Text to show");
    }

    // TODO
    @Test
    public void testPresentation() throws Exception {

    }

    @Test
    public void testTextbox() throws Exception, CancelException {
        assertEquals("showTextBoxStep result", this.setupWizard.textbox("Text to show", "Default value"));
    }

    @Test
    public void testMenu() throws Exception, CancelException {
        ArrayList<String> items = new ArrayList<>();
        items.add("Element 1");
        items.add("Element 2");

        assertEquals("menu result", this.setupWizard.menu("Text to show", items));
    }

    @Test
    public void testWait() throws Exception {
        this.setupWizard.wait("text to show");
    }


}