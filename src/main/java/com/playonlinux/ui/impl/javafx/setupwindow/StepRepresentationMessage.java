package com.playonlinux.ui.impl.javafx.setupwindow;

import javafx.scene.text.Text;
import com.playonlinux.utils.messages.CancelerMessage;
import com.playonlinux.utils.messages.CancelerSynchroneousMessage;

public class StepRepresentationMessage extends AbstractStepRepresentationWithHeader {
    String textToShow;

    public StepRepresentationMessage(SetupWindowJavaFXImplementation parent, CancelerMessage message, String textToShow) {
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
        this.setNextButtonAction(event ->
            ((CancelerSynchroneousMessage) this.getMessageAwaitingForResponse()).setResponse(null)
        );
    }

}
