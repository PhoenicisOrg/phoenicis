package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import org.phoenicis.repository.location.LocalRepositoryLocation;

import java.io.File;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 19.06.17.
 */
public class LocalRepositoryDetailsPanel extends RepositoryDetailsPanel<LocalRepositoryLocation> {
    private TextField pathField;

    private Button openBrowser;

    public LocalRepositoryDetailsPanel() {
        super();

        this.populate();
    }

    /**
     * Populates the repository details step for the local repository
     */
    private void populate() {
        this.pathField = new TextField();

        this.openBrowser = new Button(tr("Open directory chooser"));
        openBrowser.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();

            File directory = chooser.showDialog(null);

            pathField.setText(directory.toString());
        });

        HBox content = new HBox(pathField, openBrowser);
        HBox.setHgrow(pathField, Priority.ALWAYS);

        this.setCenter(content);
    }

    public String getHeader() {
        return tr("Choose the location of your local repository");
    }

    @Override
    public LocalRepositoryLocation createRepositoryLocation() {
        return new LocalRepositoryLocation(new File(pathField.getText()));
    }
}
