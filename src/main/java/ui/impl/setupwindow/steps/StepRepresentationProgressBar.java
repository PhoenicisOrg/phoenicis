package ui.impl.setupwindow.steps;

import api.ProgressStep;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import ui.impl.JavaFXMessageSenderImplementation;
import ui.impl.setupwindow.JavaFXSetupWindowImplementation;
import utils.messages.AsynchroneousMessage;
import utils.messages.InterrupterSynchroneousMessage;
import utils.messages.Message;

public class StepRepresentationProgressBar extends StepRepresentationMessage implements ProgressStep {
    ProgressBar progressBar = new ProgressBar();
    Text progressText = new Text("");

    public StepRepresentationProgressBar(JavaFXSetupWindowImplementation parent, InterrupterSynchroneousMessage messageWaitingForResponse,
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
        JavaFXMessageSenderImplementation messageSender = new JavaFXMessageSenderImplementation();
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
        JavaFXMessageSenderImplementation messageSender = new JavaFXMessageSenderImplementation();
        messageSender.asynchroneousSend(new AsynchroneousMessage() {
                                            @Override
                                            public void execute(Message message) {
                                                progressText.setText(text);
                                            }
                                        }
        );
    }

}
