package ui.impl.setupwindow.steps;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ui.impl.setupwindow.SetupWindowImplementation;
import utils.InterrupterAsynchroneousMessage;

public class StepSpin extends AbstractStepWithHeader {
    String textToShow;
    ImageView spinnerImage;
    int currentAngle = 1;

    public StepSpin(SetupWindowImplementation parent, InterrupterAsynchroneousMessage messageWaitingForResponse,
                    String textToShow) {
        super(parent, messageWaitingForResponse);
        this.textToShow = textToShow;
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
        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(10);
        textWidget.setLayoutY(20);
        textWidget.setWrappingWidth(500);
        textWidget.prefWidth(500);

        spinnerImage.setLayoutX(230);
        spinnerImage.setLayoutY(100);

        this.addToContentPanel(textWidget);
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
