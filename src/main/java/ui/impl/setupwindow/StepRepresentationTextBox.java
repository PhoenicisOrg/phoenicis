package ui.impl.setupwindow;

import javafx.scene.control.TextField;
import utils.messages.CancelerSynchroneousMessage;

public class StepRepresentationTextBox extends StepRepresentationMessage {
    private final String defaultValue;
    TextField textField;

    public StepRepresentationTextBox(JavaFXSetupWindowImplementation parent, CancelerSynchroneousMessage messageWaitingForResponse, String textToShow,
                                     String defaultValue) {
        super(parent, messageWaitingForResponse, textToShow);

        this.defaultValue = defaultValue;
    }

    @Override
    protected void drawStepContent() {
        super.drawStepContent();

        textField = new TextField();
        textField.setText(defaultValue);
        textField.setLayoutX(10);
        textField.setLayoutY(40);

        this.addToContentPanel(textField);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event -> {
            ((CancelerSynchroneousMessage) this.getMessageAwaitingForResponse()).setResponse(textField.getText());
        });
    }

}
