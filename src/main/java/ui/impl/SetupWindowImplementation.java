package ui.impl;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import api.SetupWindow;
import utils.Message;

import java.io.File;

public class SetupWindowImplementation extends Stage implements SetupWindow {
    private final Scene scene;
    private final Pane root;
    private final String title;

    /**
     * Draw the header at the top of the window
     * @param root
     * @param title
     */
    private void drawHeader(Pane root, String title) {
        Pane header = new Pane();
        header.setPrefSize(520, 65);
        header.setLayoutX(0);
        header.setLayoutY(0);
        header.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(header);
    }

    private void drawFooter(Pane root, EventHandler nextButtonAction) {
        Pane footer = new Pane();
        footer.setPrefSize(520, 45);
        footer.setLayoutX(0);
        footer.setLayoutY(355);
        root.getChildren().add(footer);
        Button nextButton = new Button("Next");
        nextButton.setLayoutX(300);
        footer.getChildren().add(nextButton);
        nextButton.setOnMouseClicked(event -> { nextButton.setDisable(true); nextButtonAction.handle(event); });
    }

    private Pane drawPanelForTopHeader(Pane root) {
        Pane panel = new Pane();
        panel.setPrefSize(520, 270);
        panel.setLayoutX(0);
        panel.setLayoutY(85);
        root.getChildren().add(panel);
        return panel;
    }

    public void clearAll()
    {
        root.getChildren().clear();
    }

    public SetupWindowImplementation(String title) {
        super();
        this.root = new Pane();
        this.scene = new Scene(root, 520, 400);
        this.title = title;

        this.setTitle(title);
        this.setScene(scene);
        this.show();

    }


    @Override
    public void message(Message message, String textToShow) {
        this.clearAll();

        this.drawHeader(this.root, this.title);
        this.drawFooter(this.root, event -> {
            message.setResponse(null);
        });

        Pane contentPanel = this.drawPanelForTopHeader(root);

        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(0);
        textWidget.setLayoutY(0);

        contentPanel.getChildren().add(textWidget);
    }

    public void question() {

    }

    public void textbox(Message message, String textToShow, String defaultValue) {
        defaultValue = defaultValue == null ? "" : defaultValue;
        this.clearAll();
        this.drawHeader(this.root, this.title);

        Pane contentPanel = this.drawPanelForTopHeader(root);

        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(0);
        textWidget.setLayoutY(0);

        TextField textField = new TextField();
        textField.setLayoutX(10);
        textField.setLayoutY(10);

        this.drawFooter(this.root, event -> {
            message.setResponse(textField.getText());
        });

        contentPanel.getChildren().addAll(textWidget, textField);
    }
}
