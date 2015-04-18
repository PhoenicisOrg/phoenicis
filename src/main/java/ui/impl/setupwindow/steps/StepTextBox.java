package ui.impl.setupwindow.steps;

import javafx.scene.control.TextField;
import ui.impl.setupwindow.SetupWindowImplementation;
import utils.CancelerSynchroneousMessage;

public class StepTextBox extends StepMessage {
    private final String defaultValue;
    TextField textField;

    public StepTextBox(SetupWindowImplementation parent, CancelerSynchroneousMessage messageWaitingForResponse, String textToShow,
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
