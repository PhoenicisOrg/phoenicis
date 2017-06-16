package org.phoenicis.javafx.views.mainwindow.settings;

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

    /**
     * A list containing all possible repository types to be added with this dialog
     */
    private ObservableList<String> repositoryChoices;

    /**
     * The choice box containing, which repository type the user wants to add
     */
    private ComboBox<String> choiceBox;

    /**
     * The result value of this wizard.
     * This wizard is {@link Optional#empty()} until the user completed the wizard
     */
    private Supplier<Optional<RepositoryLocation<? extends Repository>>> resultSupplier;

    private ButtonType finishButtonType;

    /**
     * Constructor
     */
    public AddRepositoryDialog() {
        super();

        this.repositoryChoices = FXCollections.observableArrayList("Local Repository", "Git Repository",
                "Classpath Repository");
        this.resultSupplier = () -> Optional.empty();

        this.finishButtonType = new ButtonType("Finish", ButtonBar.ButtonData.OK_DONE);
        this.setTitle(tr("Add a new Repository"));

        this.setResizable(true);
        this.getDialogPane().setPrefSize(550, 200);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == finishButtonType) {
                return resultSupplier.get().orElse(null);
            }
            return null;
        });

        this.getDialogPane().getButtonTypes().setAll(ButtonType.CANCEL);
        this.populateTypePage();
    }

    /**
     * Populates the repository selection step
     */
    private void populateTypePage() {
        choiceBox = new ComboBox<>(repositoryChoices);
        choiceBox.setPromptText(tr("Please select the repository type you want to add"));
        choiceBox.setOnAction(event -> {
            switch (choiceBox.getSelectionModel().getSelectedItem()) {
                case "Local Repository":
                    populateLocalRepositoryDetailsPage();
                    break;
                case "Git Repository":
                    populateGitRepositoryDetailsPage();
                    break;
                case "Classpath Repository":
                    populateClasspathRepositoryDetailsPage();
                    break;
                default:
            }
            this.getDialogPane().getButtonTypes().setAll(finishButtonType, ButtonType.CANCEL);
        });

        Label choiceBoxLabel = new Label(tr("Repository type:"));
        choiceBoxLabel.setLabelFor(choiceBox);

        HBox content = new HBox(choiceBoxLabel, choiceBox);
        HBox.setHgrow(choiceBox, Priority.ALWAYS);

        this.setHeaderText(tr("Choose the repository type"));
        this.getDialogPane().setContent(content);
    }

    /**
     * Populates the repository details step for the local repository
     */
    private void populateLocalRepositoryDetailsPage() {
        TextField pathField = new TextField();

        Button openBrowser = new Button(tr("Open directory chooser"));
        openBrowser.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();

            File directory = chooser.showDialog(null);

            pathField.setText(directory.toString());
        });

        HBox content = new HBox(pathField, openBrowser);
        HBox.setHgrow(pathField, Priority.ALWAYS);

        resultSupplier = () -> Optional.of(new LocalRepositoryLocation(new File(pathField.getText())));

        this.setHeaderText(tr("Choose the location of your local repository"));
        this.getDialogPane().setContent(content);
    }

    /**
     * Populates the repository details step for the git repository
     */
    private void populateGitRepositoryDetailsPage() {
        TextField urlField = new TextField();
        TextField branchField = new TextField("master");

        Label urlLabel = new Label("Git-Url:");
        urlLabel.setLabelFor(urlField);
        Label branchLabel = new Label("Branch:");
        branchLabel.setLabelFor(branchField);

        GridPane grid = new GridPane();
        grid.add(urlLabel, 0, 0);
        grid.add(urlField, 1, 0);

        grid.add(branchLabel, 0, 1);
        grid.add(branchField, 1, 1);

        resultSupplier = () -> {
            try {
                return Optional.of(
                        new GitRepositoryLocation.Builder().withGitRepositoryUri(new URL(urlField.getText()).toURI())
                                .withBranch(branchField.getText()).build());
            } catch (MalformedURLException | URISyntaxException e) {
                LOGGER.error(String.format("The given url '%s' is no valid URI or URL", urlField.getText()), e);
                return Optional.empty();
            }
        };

        this.setHeaderText("Choose the location of your git repository");
        this.getDialogPane().setContent(grid);
    }

    /**
     * Populates the repository details step for the classpath repository
     */
    private void populateClasspathRepositoryDetailsPage() {
        TextField classpathField = new TextField();

        Label classpathLabel = new Label(tr("Classpath:"));
        classpathLabel.setLabelFor(classpathField);

        HBox content = new HBox(classpathLabel, classpathField);
        HBox.setHgrow(classpathField, Priority.ALWAYS);

        resultSupplier = () -> Optional.of(new ClasspathRepositoryLocation(classpathField.getText()));

        this.setHeaderText(tr("Choose the location of your classpath repository"));
        this.getDialogPane().setContent(content);
    }
}
