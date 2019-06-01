package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import org.phoenicis.repository.location.LocalRepositoryLocation;

import java.io.File;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A {@link RepositoryDetailsPanel} used to specify the details of a {@link LocalRepositoryLocation}.
 *
 * @author marc
 * @since 19.06.17
 */
public class LocalRepositoryDetailsPanel extends RepositoryDetailsPanel<LocalRepositoryLocation> {
    /**
     * The path leading to the repository
     */
    private TextField pathField;

    /**
     * A button opening a {@link DirectoryChooser} used to select the path leading to the repository through a GUI
     */
    private Button openBrowser;

    /**
     * Constructor
     */
    public LocalRepositoryDetailsPanel() {
        super();

        this.populate();
    }

    /**
     * Populates the repository details step for the local repository
     */
    private void populate() {
        this.pathField = new TextField();

        this.openBrowser = new Button(tr("Choose Directory..."));
        openBrowser.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();

            File directory = chooser.showDialog(null);

            pathField.setText(directory.toString());
        });

        HBox content = new HBox(pathField, openBrowser);
        content.setId("addLocalRepository");
        HBox.setHgrow(pathField, Priority.ALWAYS);

        this.setCenter(content);
    }

    @Override
    public String getHeader() {
        return tr("Choose the location of the local repository");
    }

    @Override
    public LocalRepositoryLocation createRepositoryLocation() {
        return new LocalRepositoryLocation(new File(pathField.getText()));
    }
}
