package com.playonlinux.ui.impl.mockui.setupwindow;

import com.playonlinux.api.ProgressStep;
import com.playonlinux.api.SetupWindow;
import com.playonlinux.utils.messages.CancelerSynchroneousMessage;
import com.playonlinux.utils.messages.InterrupterAsynchroneousMessage;
import com.playonlinux.utils.messages.InterrupterSynchroneousMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MockUISetupWindowImplementation implements SetupWindow {

    @Override
    public void setTopImage(File topImage) throws MalformedURLException { }

    @Override
    public void setLeftImage(File leftImage) throws MalformedURLException { }

    @Override
    public void showSimpleMessageStep(CancelerSynchroneousMessage message, String textToShow) {
        assertEquals("Text to show", textToShow);
        message.setResponse(null);
    }

    @Override
    public void showYesNoQuestionStep() {

    }

    @Override
    public void showTextBoxStep(CancelerSynchroneousMessage message, String textToShow, String defaultValue) {
        assertEquals("Text to show", textToShow);
        assertEquals("Default value", defaultValue);
        message.setResponse("showTextBoxStep result");
    }

    @Override
    public void showMenuStep(CancelerSynchroneousMessage message, String textToShow, List<String> menuItems) {
        assertEquals("Text to show", textToShow);
        assertEquals("Element 1", menuItems.get(0));
        assertEquals("Element 2", menuItems.get(1));

        message.setResponse("menu result");
    }

    @Override
    public void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow) {
        assertEquals("Text to show", textToShow);
    }

    @Override
    public ProgressStep showProgressBar(InterrupterSynchroneousMessage message, String textToShow) {
        return null;
    }

    @Override
    public void showPresentationStep(CancelerSynchroneousMessage message, String textToShow) {

    }

    @Override
    public void close() {

    }
}
