package ui.impl.mainwindow;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.api.EventHandler;

import java.io.File;


class MenuBar extends javafx.scene.control.MenuBar {
    private Stage stage;
    private MenuItem openScript;

    MenuBar(Stage stage) {
        this.stage = stage;

        buildFileMenu();
        buildToolsMenu();
        buildHelpMenu();

        this.useSystemMenuBarProperty().set(true);
    }

    private void buildFileMenu() {
        final Menu fileMenu = new Menu("File");
        this.getMenus().add(fileMenu);
    }

    private void buildToolsMenu() {
        final Menu toolsMenu = new Menu("Tools");
        openScript = new MenuItem("Run a local script");

        toolsMenu.getItems().addAll(openScript);
        this.getMenus().add(toolsMenu);
    }

    private void buildHelpMenu() {
        final Menu helpMenu = new Menu("Help");

        this.getMenus().add(helpMenu);
    }

    void setEvents(EventHandler eventHandler) {
        openScript.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a script");
            File scriptToRun = fileChooser.showOpenDialog(stage);

            eventHandler.runLocalScript(scriptToRun);
        });
    }
}
