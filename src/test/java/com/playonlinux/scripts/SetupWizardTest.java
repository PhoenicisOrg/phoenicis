package com.playonlinux.scripts;

import com.playonlinux.api.Controller;
import com.playonlinux.api.ProgressStep;
import com.playonlinux.api.SetupWindow;
import com.playonlinux.api.UIMessageSender;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.impl.mockui.MockUIMessageSenderImplementation;
import com.playonlinux.ui.impl.mockui.setupwindow.MockUISetupWindowImplementation;
import com.playonlinux.utils.messages.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SetupWizardTest {
    private SetupWizard setupWizard;

    @Before
    public void setUp() {
        final Controller controllerMock = mock(Controller.class);
        when(controllerMock.createSetupWindowGUIInstance("Title")).thenReturn(new MockUISetupWindowImplementation());
        when(controllerMock.createUIMessageSender()).thenReturn(new MockUIMessageSenderImplementation());

        SetupWizard.injectMainController(controllerMock);
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