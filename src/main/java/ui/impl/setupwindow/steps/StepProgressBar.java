package ui.impl.setupwindow.steps;

import ui.impl.setupwindow.JavaFXProgressBar;
import ui.impl.setupwindow.JavaFXSetupWindowImplementation;
import utils.messages.InterrupterSynchroneousMessage;

public class StepProgressBar extends AbstractAsynchroneousStepText {
    JavaFXProgressBar progressBar = new JavaFXProgressBar();

    public StepProgressBar(JavaFXSetupWindowImplementation parent, InterrupterSynchroneousMessage messageWaitingForResponse,
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
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonEnabled(false);
    }

    public JavaFXProgressBar getProgressBar() {
        return progressBar;
    }

}
