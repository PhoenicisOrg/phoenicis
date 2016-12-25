package com.playonlinux.cli.setupwindow;

import com.playonlinux.scripts.ui.Message;
import com.playonlinux.scripts.ui.ProgressControl;
import com.playonlinux.scripts.ui.SetupWindow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

class SetupWindowCLIImplementation implements SetupWindow {
    private final String title;
    private final boolean interactive;
    private final boolean verbose;

    SetupWindowCLIImplementation(String title, boolean interactive, boolean verbose) {
        this.title = title;
        this.interactive = interactive;
        this.verbose = verbose;

        printIfVerbose(title);
        printIfVerbose("-----------------");
    }

    @Override
    public void setTopImage(File topImage) throws IOException {
        // Do nothing
    }

    @Override
    public void setLeftImage(File leftImage) throws IOException {
        // Do nothing
    }

    @Override
    public void setTopImage(URL topImage) throws IOException {
        // Do nothing
    }

    @Override
    public void setLeftImage(URL leftImage) throws IOException {
        // Do nothing
    }

    @Override
    public void showSimpleMessageStep(Message<Void> doneCallback, String textToShow) {
        printIfVerbose(textToShow);
        pauseIfInteractive();

        doneCallback.send(null);
    }

    @Override
    public void showYesNoQuestionStep() {
        throw new UnsupportedOperationException("FIXME");
    }

    @Override
    public void showTextBoxStep(Message<String> doneCallback, String textToShow, String defaultValue) {
        throw new UnsupportedOperationException("FIXME");
    }

    @Override
    public void showMenuStep(Message<String> doneCallback, String textToShow, List<String> menuItems) {
        throw new UnsupportedOperationException("FIXME");
    }

    @Override
    public void showSpinnerStep(Message<Void> message, String textToShow) {
        printIfVerbose(textToShow);
        message.send(null);
    }

    @Override
    public void showProgressBar(Message<ProgressControl> message, String textToShow) {
        printIfVerbose(textToShow);

        message.send(new ProgressControl() {
            private double percentage = 0;
            private String text = "";

            @Override
            public void setProgressPercentage(double value) {
                percentage = min(100, max(0, value));
                printIfVerbose("["+percentage+"] " + textToShow + " : " +text);
            }

            @Override
            public void setText(String text) {
                this.text = text;
                printIfVerbose("["+percentage+"] " + textToShow + " : " +text);
            }
        });
    }

    @Override
    public void showPresentationStep(Message<Void> doneCallback, String textToShow) {
        showSimpleMessageStep(doneCallback, textToShow);
    }

    @Override
    public void showLicenceStep(Message<Void> doneCallback, String textToShow, String licenceText) {
        throw new UnsupportedOperationException("FIXME");
    }

    @Override
    public void showBrowseStep(Message<String> doneCallback, String textToShow, File browseDirectory, List<String> extensions) {
        throw new UnsupportedOperationException("FIXME");
    }

    @Override
    public void close() {
        // Do nothing
    }

    private void printIfVerbose(String textToShow) {
        if (verbose) {
            System.out.println(textToShow);
        }
    }

    private void pause() {
        try {
            System.in.read();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void pauseIfInteractive() {
        if (interactive) {
            pause();
        }
    }
}
