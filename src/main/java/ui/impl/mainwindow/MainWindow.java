package ui.impl.mainwindow;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainWindow extends Stage {

    @Autowired
    MenuBar menuBar;

    public void setUpWindow() {
        BorderPane pane = new BorderPane();

        Scene scene = new Scene(pane, 600, 400);

        VBox topContainer = new VBox();

        topContainer.getChildren().add(menuBar);
        topContainer.getChildren().add(new ToolBar());

        VBox bottomContainer = new VBox();
        bottomContainer.getChildren().add(new StatusBar(this, scene));

        pane.setTop(topContainer);
        pane.setBottom(bottomContainer);

        this.setScene(scene);
        this.setTitle("PlayOnLinux");
        this.show();
    }

}

