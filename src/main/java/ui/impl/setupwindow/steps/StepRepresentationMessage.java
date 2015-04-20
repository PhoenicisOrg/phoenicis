package ui.impl.setupwindow.steps;

import javafx.scene.text.Text;
import ui.impl.setupwindow.JavaFXSetupWindowImplementation;
import utils.messages.CancelerMessage;
import utils.messages.CancelerSynchroneousMessage;

public class StepRepresentationMessage extends AbstractStepRepresentationWithHeader {
    String textToShow;

    public StepRepresentationMessage(JavaFXSetupWindowImplementation parent, CancelerMessage message, String textToShow) {
        super(parent, message);
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
