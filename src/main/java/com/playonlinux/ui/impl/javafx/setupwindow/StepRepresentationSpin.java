package com.playonlinux.ui.impl.javafx.setupwindow;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import com.playonlinux.utils.messages.InterrupterAsynchroneousMessage;

public class StepRepresentationSpin extends StepRepresentationMessage {
    ImageView spinnerImage;
    int currentAngle = 1;

    public StepRepresentationSpin(JavaFXSetupWindowImplementation parent, InterrupterAsynchroneousMessage messageWaitingForResponse,
                                  String textToShow) {
        super(parent, messageWaitingForResponse, textToShow);
        spinnerImage = new ImageView();

    }

    private Image getSpinnerByAngle(int angle) {
        if(angle < 1) {
            angle = 1;
        }
        if(angle > 12) {
            angle = 12;
        }
        return new Image(this.getClass().getResource("spin/" + Integer.toString(angle) + ".png").toExternalForm());
    }


    @Override
    protected void drawStepContent() {
        super.drawStepContent();

        spinnerImage.setLayoutX(230);
        spinnerImage.setLayoutY(100);

        this.addToContentPanel(spinnerImage);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonEnabled(false);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(50),
                ae -> {
                    this.currentAngle = (this.currentAngle % 12) + 1;
                    this.spinnerImage.setImage(getSpinnerByAngle(currentAngle));
                }
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
