package ui.impl.setupwindow;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import api.SetupWindow;
import ui.impl.setupwindow.steps.StepMenu;
import ui.impl.setupwindow.steps.StepMessage;
import ui.impl.setupwindow.steps.StepSpin;
import ui.impl.setupwindow.steps.StepTextBox;
import utils.CancelerMessage;
import utils.CancelerSynchroneousMessage;
import utils.InterrupterAsynchroneousMessage;

import java.util.List;

public class JavaFXSetupWindowImplementation extends Stage implements SetupWindow {
    private final Scene scene;
    private final Pane root;
    private final String wizardTitle;
    private CancelerMessage lastCancelerMessage = null;


    public String getWizardTitle() {
        return wizardTitle;
    }

    public Pane getRoot() {
        return this.root;
    }


    public void clearAll()
    {
        root.getChildren().clear();
    }

    public JavaFXSetupWindowImplementation(String title) {
        super();
        this.root = new Pane();
        this.scene = new Scene(root, 520, 400);
        this.scene.getStylesheets().add(this.getClass().getResource("setupWindow.css").toExternalForm());

        this.wizardTitle = title;

        this.setTitle(title);
        this.setScene(scene);
        this.show();

        this.setOnCloseRequest(event -> {
            if (this.lastCancelerMessage != null) {
                this.lastCancelerMessage.sendCancelSignal();
            }
        });
    }

    public void addNode(Node widgetToAdd) {
        this.root.getChildren().add(widgetToAdd);
    }

    @Override
    public void showSimpleMessageStep(CancelerSynchroneousMessage message, String textToShow) {
        StepMessage stepMessage = new StepMessage(this, message, textToShow);
        stepMessage.installStep();
    }

    @Override
    public void showYesNoQuestionStep() {
        // TODO
    }

    @Override
    public void showTextBoxStep(CancelerSynchroneousMessage message, String textToShow, String defaultValue) {
        StepTextBox stepTextBox = new StepTextBox(this, message, textToShow, defaultValue);
        stepTextBox.installStep();
    }

    @Override
    public void showMenuStep(CancelerSynchroneousMessage message, String textToShow, List<String> menuItems) {
        StepMenu stepMenu = new StepMenu(this, message, textToShow, menuItems);
        stepMenu.installStep();
    }

    @Override
    public void showSpinnerStep(InterrupterAsynchroneousMessage message, String textToShow) {
        StepSpin stepSpin = new StepSpin(this, message, textToShow);
        stepSpin.installStep();
    }
}
