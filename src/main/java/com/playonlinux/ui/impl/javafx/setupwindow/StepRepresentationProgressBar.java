package com.playonlinux.ui.impl.javafx.setupwindow;

import com.playonlinux.api.ProgressStep;
import com.playonlinux.ui.impl.javafx.UIMessageSenderJavaFXImplementation;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import com.playonlinux.utils.messages.AsynchroneousMessage;
import com.playonlinux.utils.messages.InterrupterSynchroneousMessage;
import com.playonlinux.utils.messages.Message;

public class StepRepresentationProgressBar extends StepRepresentationMessage implements ProgressStep {
    ProgressBar progressBar = new ProgressBar();
    Text progressText = new Text("");

    public StepRepresentationProgressBar(SetupWindowJavaFXImplementation parent, InterrupterSynchroneousMessage messageWaitingForResponse,
                                         String textToShow) {
        super(parent, messageWaitingForResponse, textToShow);
        progressBar.setProgress(0.0);
    }


    @Override
    protected void drawStepContent() {
        super.drawStepContent();
        progressBar.setLayoutY(60);
        progressBar.setLayoutX(30);
        progressBar.setPrefSize(460, 30);
        this.addToContentPanel(progressBar);

        progressText.setLayoutX(10);
        progressText.setLayoutY(120);
        progressText.setWrappingWidth(500);
        progressText.prefWidth(500);
        this.addToContentPanel(progressText);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonEnabled(false);
    }

    @Override
    public void setProgressPercentage(double value) {
        UIMessageSenderJavaFXImplementation messageSender = new UIMessageSenderJavaFXImplementation();
        messageSender.asynchroneousSend(new AsynchroneousMessage() {
                                            @Override
                                            public void execute(Message message) {
                                                progressBar.setProgress(value / 100.);
                                            }
                                        }
        );
    }

    @Override
    public double getProgressPercentage() {
        return this.progressBar.getProgress() * 100;
    }


    @Override
    public void setText(String text) {
        UIMessageSenderJavaFXImplementation messageSender = new UIMessageSenderJavaFXImplementation();
        messageSender.asynchroneousSend(new AsynchroneousMessage() {
                                            @Override
                                            public void execute(Message message) {
                                                progressText.setText(text);
                                            }
                                        }
        );
    }

}
