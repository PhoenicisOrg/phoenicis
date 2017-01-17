package com.playonlinux.tests;

import com.playonlinux.scripts.ui.Message;
import com.playonlinux.scripts.ui.ProgressControl;
import com.playonlinux.scripts.ui.SetupWindow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

class TestSetupWindow implements SetupWindow {
    @Override
    public void setTopImage(File topImage) throws IOException {

    }

    @Override
    public void setLeftImageText(String leftImageText) {

    }

    @Override
    public void setTopImage(URL topImage) throws IOException {

    }

    @Override
    public void showSimpleMessageStep(Message<Void> doneCallback, String textToShow) {
        doneCallback.send(null);
    }

    @Override
    public void showYesNoQuestionStep() {

    }

    @Override
    public void showTextBoxStep(Message<String> doneCallback, String textToShow, String defaultValue) {
        doneCallback.send("");
    }

    @Override
    public void showMenuStep(Message<String> doneCallback, String textToShow, List<String> menuItems) {
        doneCallback.send("");
    }

    @Override
    public void showSpinnerStep(Message<Void> message, String textToShow) {
        message.send(null);
    }

    @Override
    public void showProgressBar(Message<ProgressControl> message, String textToShow) {
        message.send(new ProgressControl() {
            @Override
            public void setProgressPercentage(double value) {

            }

            @Override
            public void setText(String text) {

            }
        });
    }

    @Override
    public void showPresentationStep(Message<Void> doneCallback, String textToShow) {
        doneCallback.send(null);
    }

    @Override
    public void showLicenceStep(Message<Void> doneCallback, String textToShow, String licenceText) {
        doneCallback.send(null);
    }

    @Override
    public void showBrowseStep(Message<String> doneCallback, String textToShow, File browseDirectory, List<String> extensions) {
        doneCallback.send(null);
    }

    @Override
    public void close() {

    }
}
