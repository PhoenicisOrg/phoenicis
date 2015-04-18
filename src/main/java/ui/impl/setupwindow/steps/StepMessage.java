package ui.impl.setupwindow.steps;

import javafx.scene.text.Text;
import ui.impl.setupwindow.SetupWindowImplementation;
import utils.CancelerSynchroneousMessage;

public class StepMessage extends AbstractStepWithHeader {
    String textToShow;

    public StepMessage(SetupWindowImplementation parent, CancelerSynchroneousMessage messageWaitingForResponse, String textToShow) {
        super(parent, messageWaitingForResponse);
        this.textToShow = textToShow;
    }

    @Override
    protected void drawStepContent() {
        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(10);
        textWidget.setLayoutY(20);
        textWidget.setWrappingWidth(500);
        textWidget.prefWidth(500);

        this.addToContentPanel(textWidget);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event -> {
            ((CancelerSynchroneousMessage) this.getMessageAwaitingForResponse()).setResponse(null);
        });
    }

}
