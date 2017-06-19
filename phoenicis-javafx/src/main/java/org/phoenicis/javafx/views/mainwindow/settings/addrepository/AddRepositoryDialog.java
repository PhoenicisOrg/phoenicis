package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import org.phoenicis.repository.location.ClasspathRepositoryLocation;
import org.phoenicis.repository.location.GitRepositoryLocation;
import org.phoenicis.repository.location.LocalRepositoryLocation;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.function.Supplier;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A dialog for the addition of a new repository to Phoenicis.
 * This dialog contains a wizard with two steps:
 * <ol>
 * <li>a selection step, to select the correct repository type</li>
 * <li>a details step, to input specific repository type related information</li>
 * </ol>
 *
 * @author marc
 * @since 12.06.17
 */
public class AddRepositoryDialog extends Dialog<RepositoryLocation<? extends Repository>> {
    private final static Logger LOGGER = LoggerFactory.getLogger(AddRepositoryDialog.class);

    private ChooseRepositoryTypePanel chooseRepositoryTypePanel;

    private RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> repositoryDetailsPanel;

    private ButtonType finishButtonType;

    /**
     * Constructor
     */
    public AddRepositoryDialog() {
        super();

        this.setResizable(true);
        this.getDialogPane().setPrefSize(550, 200);
        this.setTitle(tr("Add a new Repository"));

        this.chooseRepositoryTypePanel = new ChooseRepositoryTypePanel();
        this.chooseRepositoryTypePanel.setOnRepositoryTypeSelection(repositoryType -> {
            this.repositoryDetailsPanel = repositoryType.getRepositoryDetailsPanel();

            this.setHeaderText(repositoryDetailsPanel.getHeader());
            this.getDialogPane().setContent(repositoryDetailsPanel);
            this.getDialogPane().getButtonTypes().setAll(ButtonType.FINISH, ButtonType.CANCEL);
        });

        this.setHeaderText(chooseRepositoryTypePanel.getHeader());

        this.setResultConverter(dialogButton -> {
            if (dialogButton == finishButtonType && repositoryDetailsPanel != null) {
                return repositoryDetailsPanel.createRepositoryLocation();
            }
            return null;
        });

        this.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL);
        this.getDialogPane().setContent(chooseRepositoryTypePanel);
    }
}
