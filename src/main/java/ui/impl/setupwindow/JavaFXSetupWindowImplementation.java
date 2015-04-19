package ui.impl.setupwindow;

import api.ProgressBar;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import api.SetupWindow;
import ui.impl.setupwindow.steps.*;
import utils.OperatingSystem;
import utils.PlayOnLinuxError;
import utils.messages.CancelerMessage;
import utils.messages.CancelerSynchroneousMessage;
import utils.messages.InterrupterAsynchroneousMessage;
import utils.messages.InterrupterSynchroneousMessage;

import java.io.File;
import java.util.List;

public class JavaFXSetupWindowImplementation extends Stage implements SetupWindow {
    private final Scene scene;
    private final Pane root;
    private final String wizardTitle;
    private CancelerMessage lastCancelerMessage = null;


    private File topImage;
    private File leftImage;


    public String getWizardTitle() {
        return wizardTitle;
    }

    public Pane getRoot() {
        return this.root;
    }

    public void clearAll() {
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

        this.loadImages();
    }

    private void loadImages() {
        this.topImage = new File(this.getClass().getResource("defaultTopImage.png").getPath());
        try {
            switch ( OperatingSystem.fetchCurrentOperationSystem() ) {
                case MACOSX:
                    this.leftImage = new File(this.getClass().getResource("defaultLeftPlayOnMac.jpg").getPath());
                default:
                case LINUX:
                    this.leftImage = new File(this.getClass().getResource("defaultLeftPlayOnLinux.jpg").getPath());
            }
        } catch (PlayOnLinuxError playOnLinuxError) {
            this.leftImage = new File(this.getClass().getResource("defaultLeftPlayOnLinux.jpg").getPath());
        }
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

    @Override
    public ProgressBar showProgressBar(InterrupterSynchroneousMessage message, String textToShow) {
        StepProgressBar stepProgressBar = new StepProgressBar(this, message, textToShow);
        stepProgressBar.installStep();

        return stepProgressBar.getProgressBar();
    }




    @Override
    public void setTopImage(File topImage) {
        this.topImage = topImage;
    }

    @Override
    public void setLeftImage(File leftImage) {
        this.leftImage = leftImage;
    }

    public File getLeftImage() {
        return leftImage;
    }

    public File getTopImage() {
        return topImage;
    }
}
