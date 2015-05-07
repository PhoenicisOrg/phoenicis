package com.playonlinux.ui.impl.javafx.setupwindow;

import javafx.scene.control.ProgressIndicator;
import com.playonlinux.utils.messages.InterrupterAsynchroneousMessage;

public class StepRepresentationSpin extends StepRepresentationMessage {
    public StepRepresentationSpin(SetupWindowJavaFXImplementation parent, InterrupterAsynchroneousMessage messageWaitingForResponse,
                                  String textToShow) {
        super(parent, messageWaitingForResponse, textToShow);
    }


    @Override
    protected void drawStepContent() {
        super.drawStepContent();

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setLayoutX(230);
        progressIndicator.setLayoutY(100);

        this.addToContentPanel(progressIndicator);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonEnabled(false);
    }

}
