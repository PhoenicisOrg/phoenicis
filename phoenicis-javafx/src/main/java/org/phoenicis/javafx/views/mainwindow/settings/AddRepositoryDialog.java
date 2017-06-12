package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.phoenicis.repository.location.ClasspathRepositoryLocation;
import org.phoenicis.repository.location.GitRepositoryLocation;
import org.phoenicis.repository.location.LocalRepositoryLocation;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A dialog for the addition of a new repository to Phoenicis.
 * This dialog contains a wizard with two steps:
 * <ol>
 *     <li>a selection step, to select the correct repository type</li>
 *     <li>a details step, to input specific repository type related information</li>
 * </ol>
 *
 * @author marc
 * @since 12.06.17
 */
public class AddRepositoryDialog extends Wizard {
    /**
     * A list containing all possible repository types to be added with this dialog
     */
    private ObservableList<String> repositoryChoices;

    /**
     * The wizard repository type selection step
     */
    private WizardPane repositoryTypeSelection;

    /**
     * The choice box containing, which repository type the user wants to add
     */
    private ChoiceBox<String> choiceBox;

    /**
     * The wizard repository details step
     */
    private WizardPane repositoryDetailsSelection;

    /**
     * The result value of this wizard.
     * This wizard is {@link Optional#empty()} until the user completed the wizard
     */
    private Optional<RepositoryLocation<? extends Repository>> resultRepository;

    /**
     * Constructor
     */
    public AddRepositoryDialog() {
        super();

        this.repositoryChoices = FXCollections.observableArrayList("Local Repository", "Git Repository",
                "Classpath Repository");
        this.resultRepository = Optional.empty();

        this.setTitle(tr("Add a new Repository"));

        this.repositoryTypeSelection = new WizardPane();

        this.repositoryDetailsSelection = new WizardPane() {
            @Override
            public void onEnteringPage(Wizard wizard) {
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
            }
        };

        this.populateTypePage();

        this.setFlow(new LinearFlow(repositoryTypeSelection, repositoryDetailsSelection));
    }

    /**
     * Populates the repository selection step
     */
    private void populateTypePage() {
        choiceBox = new ChoiceBox<>(repositoryChoices);
        choiceBox.getSelectionModel().selectFirst();

        Label choiceBoxLabel = new Label(tr("Repository type:"));
        choiceBoxLabel.setLabelFor(choiceBox);

        HBox content = new HBox(choiceBoxLabel, choiceBox);
        HBox.setHgrow(choiceBox, Priority.ALWAYS);

        repositoryTypeSelection.setHeaderText(tr("Choose the repository type"));
        repositoryTypeSelection.setContent(content);
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

        Button finishButton = (Button) repositoryDetailsSelection.lookupButton(ButtonType.FINISH);
        finishButton.setOnAction(event -> {
            resultRepository = Optional.of(new LocalRepositoryLocation(new File(pathField.getText())));
        });

        repositoryDetailsSelection.setHeaderText(tr("Choose the location of your local repository"));
        repositoryDetailsSelection.setContent(content);
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

        Button finishButton = (Button) repositoryDetailsSelection.lookupButton(ButtonType.FINISH);
        finishButton.setOnAction(event -> {
            try {
                resultRepository = Optional.of(new GitRepositoryLocation.Builder()
                        .withGitRepositoryUri(new URL(urlField.getText()).toURI()).build());
            } catch (MalformedURLException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        repositoryDetailsSelection.setHeaderText("Choose the location of your git repository");
        repositoryDetailsSelection.setContent(grid);
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

        Button finishButton = (Button) repositoryDetailsSelection.lookupButton(ButtonType.FINISH);
        finishButton.setOnAction(event -> {
            resultRepository = Optional.of(new ClasspathRepositoryLocation(classpathField.getText()));
        });

        repositoryDetailsSelection.setHeaderText(tr("Choose the location of your classpath repository"));
        repositoryDetailsSelection.setContent(content);
    }

    /**
     * Returns the resulting {@link RepositoryLocation} of this wizard
     * @return The resulting {@link RepositoryLocation} of this wizard,
     * or {@link Optional#empty()}, if the wizard has not been completed
     */
    public Optional<RepositoryLocation<? extends Repository>> getResultRepository() {
        return resultRepository;
    }
}
