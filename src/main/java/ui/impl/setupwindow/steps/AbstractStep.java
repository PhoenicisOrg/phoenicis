package ui.impl.setupwindow.steps;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import ui.impl.setupwindow.JavaFXSetupWindowImplementation;
import utils.messages.CancelerMessage;

abstract class AbstractStep {
    private final CancelerMessage messageWaitingForResponse;
    private final JavaFXSetupWindowImplementation parent;
    private Button nextButton;

    AbstractStep(JavaFXSetupWindowImplementation parent, CancelerMessage messageWaitingForResponse) {
        this.parent = parent;
        this.messageWaitingForResponse = messageWaitingForResponse;
    }

    protected JavaFXSetupWindowImplementation getParent() {
        return this.parent;
    }

    protected Pane getParentRoot() {
        return this.parent.getRoot();
    }

    protected void addToStep(Node widgetToAdd) {
        this.parent.addNode(widgetToAdd);
    }

    public String getParentWizardTitle() {
        return this.parent.getWizardTitle();
    }

    public void clearAllOnParent() {
        this.parent.clearAll();
    }

    protected CancelerMessage getMessageAwaitingForResponse() {
        return messageWaitingForResponse;
    }

    protected void drawFooter() {
        Pane footer = new Pane();
        footer.setPrefSize(522, 45);
        footer.setLayoutX(-1);
        footer.setLayoutY(356);
        footer.setId("footer");
        this.addToStep(footer);

        nextButton = new Button("Next");
        nextButton.setLayoutY(9);
        nextButton.setLayoutX(435);
        nextButton.setPrefSize(70, 28);

        Button cancelButton = new Button("Cancel");
        cancelButton.setLayoutY(9);
        cancelButton.setLayoutX(355);
        cancelButton.setPrefSize(70, 28);

        footer.getChildren().addAll(nextButton, cancelButton);

        cancelButton.setOnMouseClicked(event -> {
            cancelButton.setDisable(true);
            messageWaitingForResponse.sendCancelSignal();
            this.parent.close();
        });
    }

    protected void setNextButtonAction(EventHandler nextButtonAction) {
        nextButton.setOnMouseClicked(event -> {
            nextButton.setDisable(true);
            nextButtonAction.handle(event);
        });
    }

    protected void setNextButtonEnabled(Boolean nextEnabled) {
        nextButton.setDisable(!nextEnabled);
    }


    protected abstract void drawStepContent();

    protected abstract void setStepEvents();

    public void installStep() {
        this.parent.clearAll();
        this.drawFooter();
        this.setStepEvents();
        this.drawStepContent();

    }


}
