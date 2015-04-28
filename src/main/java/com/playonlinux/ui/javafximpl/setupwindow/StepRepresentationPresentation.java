package com.playonlinux.ui.javafximpl.setupwindow;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import com.playonlinux.utils.messages.CancelerMessage;
import com.playonlinux.utils.messages.CancelerSynchroneousMessage;

public class StepRepresentationPresentation extends AbstractStepRepresentation {
    String textToShow;

    public StepRepresentationPresentation(JavaFXSetupWindowImplementation parent, CancelerMessage message, String textToShow) {
        super(parent, message);
        this.textToShow = textToShow;
    }

    private Image createLeftImage() {
        return new Image("file:" + this.getParentLeftImage().getAbsolutePath());
    }

    @Override
    protected void drawStepContent() {
        String title = this.getParentWizardTitle();

        ImageView leftImage = new ImageView(this.createLeftImage());
        leftImage.setLayoutX(0);
        leftImage.setLayoutY(0);

        Pane contentPane = new Pane();
        contentPane.setPrefSize(370, 355);
        contentPane.setLayoutX(151);
        contentPane.setLayoutY(0);
        contentPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        Text titleWidget = new Text(title);
        titleWidget.setLayoutX(10);
        titleWidget.setLayoutY(30);
        titleWidget.setFont(Font.font(null, FontWeight.BOLD, 16));

        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(10);
        textWidget.setLayoutY(80);
        textWidget.setWrappingWidth(350);
        textWidget.prefWidth(350);

        contentPane.getChildren().addAll(titleWidget, textWidget);
        this.addToStep(leftImage);
        this.addToStep(contentPane);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event -> {
            ((CancelerSynchroneousMessage) this.getMessageAwaitingForResponse()).setResponse(null);
        });
    }

}
