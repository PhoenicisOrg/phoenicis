package ui.impl;


import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import api.SetupWindow;
import utils.CancelableMessage;

public class SetupWindowImplementation extends Stage implements SetupWindow {
    private final Scene scene;
    private final Pane root;
    private final String title;
    private CancelableMessage currentMessage = null;

    /**
     * Draw the header at the top of the window
     * @param root parent root
     * @param title title of of the window
     */
    private void drawHeader(Pane root, String title) {
        Pane header = new Pane();
        header.setId("header");
        header.setPrefSize(522, 65);
        header.setLayoutX(-1);
        header.setLayoutY(-1);
        header.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        ImageView topImage = new ImageView(this.createTopImage());
        topImage.setLayoutX(426);

        header.getChildren().add(topImage);
        root.getChildren().add(header);
    }

    private Image createTopImage() {
        return new Image(this.getClass().getResource("logo96.png").toExternalForm());
    }

    private void drawFooter(Pane root, EventHandler nextButtonAction) {
        Pane footer = new Pane();
        footer.setPrefSize(522, 45);
        footer.setLayoutX(-1);
        footer.setLayoutY(356);
        footer.setId("footer");
        root.getChildren().add(footer);
        Button nextButton = new Button("Next");
        Button cancelButton = new Button("Cancel");

        nextButton.setLayoutY(9);
        cancelButton.setLayoutY(9);

        nextButton.setLayoutX(355);
        cancelButton.setLayoutX(435);

        nextButton.setPrefSize(70, 28);
        cancelButton.setPrefSize(70, 28);

        footer.getChildren().addAll(nextButton, cancelButton);
        nextButton.setOnMouseClicked(event -> {
            nextButton.setDisable(true);
            nextButtonAction.handle(event);
        });
        cancelButton.setOnMouseClicked(event -> {
            nextButton.setDisable(true);
            nextButtonAction.handle(event);
        });

        nextButton.setOnMouseClicked(event -> { nextButton.setDisable(true); nextButtonAction.handle(event); });
        cancelButton.setOnMouseClicked(event -> {
            cancelButton.setDisable(true);
            currentMessage.setCancel();
            this.close();
        });
    }

    private Pane drawPanelForTopHeader(Pane root) {
        Pane panel = new Pane();
        panel.setId("panelForTopheader");
        panel.setPrefSize(522, 294);
        panel.setLayoutX(-1);
        panel.setLayoutY(63);
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
        this.scene.getStylesheets().add(this.getClass().getResource("setupWindow.css").toExternalForm());

        this.title = title;

        this.setTitle(title);
        this.setScene(scene);
        this.show();

        this.setOnCloseRequest(event -> {
            if (this.currentMessage != null) {
                this.currentMessage.setCancel();
            }
        });
    }


    public void message(CancelableMessage message, String textToShow) {
        currentMessage = message;
        this.clearAll();

        this.drawHeader(this.root, this.title);
        this.drawFooter(this.root, event -> {
            message.setResponse(null);
        });

        Pane contentPanel = this.drawPanelForTopHeader(root);

        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(10);
        textWidget.setLayoutY(20);
        textWidget.setWrappingWidth(500);
        textWidget.prefWidth(500);

        contentPanel.getChildren().add(textWidget);
    }

    public void question() {

    }

    public void textbox(CancelableMessage message, String textToShow, String defaultValue) {
        currentMessage = message;

        defaultValue = defaultValue == null ? "" : defaultValue;
        this.clearAll();
        this.drawHeader(this.root, this.title);

        Pane contentPanel = this.drawPanelForTopHeader(root);

        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(10);
        textWidget.setLayoutY(10);

        TextField textField = new TextField();

        textField.setLayoutX(10);
        textField.setLayoutY(20);

        this.drawFooter(this.root, event -> {
            message.setResponse(textField.getText());
        });

        contentPanel.getChildren().addAll(textWidget, textField);
    }
}
