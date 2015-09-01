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

import com.playonlinux.core.injection.AbstractConfiguration;
import com.playonlinux.ui.api.Controller;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.lang.FallbackLanguageBundle;
import com.playonlinux.core.lang.LanguageBundle;
import com.playonlinux.core.injection.Bean;
import com.playonlinux.core.injection.InjectionException;
import com.playonlinux.ui.impl.mockui.MockUIMessageSenderImplementation;
import com.playonlinux.ui.impl.mockui.setupwindow.MockUISetupWindowImplementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultWizardTest {
    private DefaultWizard setupWizard;
    private Controller controllerMock = mock(Controller.class);
    private TestContextConfig testContextConfig = new TestContextConfig();

    class TestContextConfig extends AbstractConfiguration {
        @Bean
        public Controller controller() {
            return controllerMock;
        }

        @Override
        protected String definePackage() {
            return "com.playonlinux";
        }

        @Bean
        protected PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxException, IOException {
            return new PlayOnLinuxContext();
        }

        @Bean
        protected LanguageBundle languageBundle() {
            return FallbackLanguageBundle.getInstance();
        }
    }

    @Before
    public void setUp() throws InjectionException {
        testContextConfig.setStrictLoadingPolicy(false);
        testContextConfig.load();

        when(controllerMock.createSetupWindowGUIInstance("Title")).thenReturn(new MockUISetupWindowImplementation());
        when(controllerMock.createUIMessageSender()).thenReturn(new MockUIMessageSenderImplementation());

        this.setupWizard = new DefaultWizard("Title");
        setupWizard.init();
    }

    @Test
    public void testClose() throws Exception {

    }

    @Test
    public void testMessage() throws Exception {
        this.setupWizard.message("Text to showRightView");
    }

    // TODO
    @Test
    public void testPresentation() throws Exception {

    }

    @Test
    public void testTextbox() throws Exception {
        assertEquals("showTextBoxStep result", this.setupWizard.textbox("Text to showRightView", "Default value"));
    }

    @Test
    public void testMenu() throws Exception {
        ArrayList<String> items = new ArrayList<>();
        items.add("Element 1");
        items.add("Element 2");

        assertEquals("menu result", this.setupWizard.menu("Text to showRightView", items));
    }

    @Test
    public void testWait() throws Exception {
        this.setupWizard.wait("text to showRightView");
    }


    @After
    public void tearDown() throws InjectionException {
        testContextConfig.close();
    }
}