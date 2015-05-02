package com.playonlinux.scripts;

import com.playonlinux.api.Controller;
import com.playonlinux.injection.AbstractConfigFile;
import com.playonlinux.injection.Bean;
import com.playonlinux.injection.InjectionException;
import com.playonlinux.ui.impl.mockui.MockUIMessageSenderImplementation;
import com.playonlinux.ui.impl.mockui.setupwindow.MockUISetupWindowImplementation;
import org.junit.Before;
import org.junit.Test;

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