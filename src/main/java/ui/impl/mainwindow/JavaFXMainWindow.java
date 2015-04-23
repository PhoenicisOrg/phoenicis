package ui.impl.mainwindow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class JavaFXMainWindow extends Application {
    private static ui.api.EventHandler eventHandler;
    private Stage stage;
    private Scene scene;



    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        BorderPane pane = new BorderPane();
        this.scene = new Scene(pane, 600, 400);

        VBox topContainer = new VBox();

        MenuBar menuBar = new MenuBar(stage);
        menuBar.setEvents(eventHandler);
        topContainer.getChildren().add(menuBar);

        topContainer.getChildren().add(new ToolBar());

        VBox bottomContainer = new VBox();
        bottomContainer.getChildren().add(new StatusBar(stage, scene));

        pane.setTop(topContainer);
        pane.setBottom(bottomContainer);


        primaryStage.setScene(scene);
        primaryStage.setTitle("PlayOnLinux");
        primaryStage.show();
    }

    public static void injectEventHandler(ui.api.EventHandler eventHandler) {
        JavaFXMainWindow.eventHandler = eventHandler;
    }
}
