package ui.impl;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class MainWindow extends Application {
    private static ui.api.EventHandler eventHandler;
    private Stage stage;

    public ToolBar generateToolbar() {
        Button run = new Button("Run", new ImageView(this.getClass().getResource("toolbar/run.png").toExternalForm()));
        run.setContentDisplay(ContentDisplay.TOP);

        Button stop = new Button("Stop", new ImageView(this.getClass().getResource("toolbar/stop.png").toExternalForm()));
        stop.setContentDisplay(ContentDisplay.TOP);

        Button install = new Button("Install", new ImageView(this.getClass().getResource("toolbar/install.png").toExternalForm()));
        install.setContentDisplay(ContentDisplay.TOP);

        Button remove = new Button("Install", new ImageView(this.getClass().getResource("toolbar/delete.png").toExternalForm()));
        remove.setContentDisplay(ContentDisplay.TOP);

        Button configure = new Button("Configure", new ImageView(this.getClass().getResource("toolbar/configure.png").toExternalForm()));
        configure.setContentDisplay(ContentDisplay.TOP);

        TextField searchField = new TextField();

        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(
                run,
                stop,
                new Separator(),
                install,
                remove,
                new Separator(),
                configure,
                new Separator(),
                searchField
        );

        return toolbar;
    }


    public MenuBar generateMenuBar() {
        final Menu fileMenu = new Menu("File");
        final Menu toolsMenu = new Menu("Tools");
        MenuItem openScript = new MenuItem("Run a local script");
        openScript.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a script");
            File scriptToRun = fileChooser.showOpenDialog(stage);

            eventHandler.runLocalScript(scriptToRun);
        });
        toolsMenu.getItems().addAll(openScript);

        final Menu helpMenu = new Menu("Help");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, toolsMenu, helpMenu);
        menuBar.useSystemMenuBarProperty().set(true);

        return menuBar;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        VBox topContainer = new VBox();
        topContainer.getChildren().add(this.generateMenuBar());
        topContainer.getChildren().add(this.generateToolbar());

        BorderPane pane = new BorderPane();
        pane.setTop(topContainer);

        Scene scene = new Scene(pane, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("PlayOnLinux");
        primaryStage.show();
    }


    public static void defineStaticEventHandler(ui.api.EventHandler eventHandler) {
        MainWindow.eventHandler = eventHandler;
    }
}
