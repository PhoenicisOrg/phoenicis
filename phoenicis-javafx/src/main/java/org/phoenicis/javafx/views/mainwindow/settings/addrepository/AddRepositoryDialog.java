package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.types.Repository;

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

    /**
     * The repository type selection step
     */
    private ChooseRepositoryTypePanel chooseRepositoryTypePanel;

    /**
     * The repository details selection step
     */
    private RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> repositoryDetailsPanel;

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
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.FINISH && repositoryDetailsPanel != null) {
                return repositoryDetailsPanel.createRepositoryLocation();
            }
            return null;
        });

        this.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL);
        this.getDialogPane().setContent(chooseRepositoryTypePanel);
    }
}
