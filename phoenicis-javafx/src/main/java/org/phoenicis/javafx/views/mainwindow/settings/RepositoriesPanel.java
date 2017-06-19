package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.javafx.views.mainwindow.settings.addrepository.AddRepositoryDialog;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.phoenicis.settings.SettingsManager;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * This class represents the "Repositories" settings category
 *
 * @author marc
 * @since 23.04.17
 */
public class RepositoriesPanel extends StackPane {
    private SettingsManager settingsManager;
    private RepositoryManager repositoryManager;

    private VBox vBox;
    private Text title;

    private GridPane repositoryGrid;

    private Text repositoryText;
    private VBox repositoryLayout;
    private ListView<RepositoryLocation<? extends Repository>> repositoryListView;
    private HBox repositoryButtonLayout;
    private Button addButton;
    private Button removeButton;

    private Label priorityHint;

    private GridPane refreshLayout;

    private Label refreshRepositoriesLabel;
    private Button refreshRepositoriesButton;

    private VBox overlay;

    private ObservableList<RepositoryLocation<? extends Repository>> repositories;

    /**
     * Constructor
     *
     * @param settingsManager   The settings manager
     * @param repositoryManager The repository manager
     */
    public RepositoriesPanel(SettingsManager settingsManager, RepositoryManager repositoryManager) {
        super();

        this.settingsManager = settingsManager;
        this.repositoryManager = repositoryManager;
        this.repositories = FXCollections.observableArrayList(settingsManager.loadRepositoryLocations());

        this.getStyleClass().add("containerConfigurationPane");

        this.vBox = new VBox();

        this.populateRepositoryGrid();
        this.populateRepositoryLegend();
        this.populateRepositoryRefresh();

        VBox.setVgrow(repositoryGrid, Priority.ALWAYS);

        this.initializeRefreshCallback();

        this.vBox.getChildren().setAll(title, repositoryGrid, priorityHint, refreshLayout);

        // overlay which is shown when repository is refreshed
        ProgressIndicator progressIndicator = new ProgressIndicator();
        this.overlay = new VBox(progressIndicator);
        this.overlay.setAlignment(Pos.CENTER);

        this.getChildren().setAll(this.overlay, this.vBox);
    }

    private void initializeRefreshCallback() {
        repositoryManager.addCallbacks(categories -> {
            Platform.runLater(() -> {
                this.overlay.toBack();
                this.vBox.setDisable(false);
            });
        }, error -> {
        });
    }

    private void populateRepositoryGrid() {
        this.title = new TextWithStyle(tr("Repositories Settings"), "title");

        this.repositoryGrid = new GridPane();
        this.repositoryGrid.getStyleClass().add("grid");

        this.repositoryText = new TextWithStyle(tr("Repository:"), "captionTitle");

        this.repositoryLayout = new VBox();
        this.repositoryLayout.setSpacing(5);

        this.repositoryListView = new ListView<>(repositories);
        this.repositoryListView.setPrefHeight(0);
        this.repositoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.repositoryListView.setEditable(true);
        this.repositoryListView.setCellFactory(param -> new DragableRepositoryListCell((repositoryUrl, toIndex) -> {
            this.repositoryManager.moveRepository(repositoryUrl, toIndex.intValue());

            this.save();
        }));

        this.repositoryButtonLayout = new HBox();
        this.repositoryButtonLayout.setSpacing(5);

        this.addButton = new Button();
        this.addButton.setText(tr("Add"));
        this.addButton.setOnAction((ActionEvent event) -> {
            AddRepositoryDialog dialog = new AddRepositoryDialog();

            Optional<RepositoryLocation<? extends Repository>> successResult = dialog.showAndWait();

            successResult.ifPresent(repositoryLocation -> {
                repositories.add(repositoryLocation);

                this.save();

                repositoryManager.addRepositories(0, repositoryLocation);
            });
        });

        this.removeButton = new Button();
        this.removeButton.setText(tr("Remove"));
        this.removeButton.setOnAction((ActionEvent event) -> {
            RepositoryLocation<? extends Repository>[] toRemove = repositoryListView.getSelectionModel()
                    .getSelectedItems().toArray(new RepositoryLocation[0]);

            repositories.removeAll(toRemove);

            this.save();

            repositoryManager.removeRepositories(toRemove);
        });

        this.repositoryButtonLayout.getChildren().addAll(addButton, removeButton);

        this.repositoryLayout.getChildren().addAll(repositoryListView, repositoryButtonLayout);

        VBox.setVgrow(repositoryListView, Priority.ALWAYS);

        this.repositoryGrid.add(repositoryText, 0, 0);
        this.repositoryGrid.add(repositoryLayout, 1, 0);

        GridPane.setHgrow(repositoryLayout, Priority.ALWAYS);
        GridPane.setVgrow(repositoryLayout, Priority.ALWAYS);

        GridPane.setValignment(repositoryText, VPos.TOP);
    }

    private void populateRepositoryLegend() {
        this.priorityHint = new Label(tr(
                "The value in front of each repository is its priority. The higher the priority is, the more important the scripts inside the repository are."));
        this.priorityHint.setWrapText(true);
        this.priorityHint.setPadding(new Insets(10));
    }

    private void populateRepositoryRefresh() {
        // Refresh Repositories
        this.refreshLayout = new GridPane();
        this.refreshLayout.setHgap(20);
        this.refreshLayout.setVgap(10);

        this.refreshRepositoriesLabel = new Label(
                tr("Fetch updates for the repositories to retrieve the newest script versions"));
        this.refreshRepositoriesLabel.setWrapText(true);

        this.refreshRepositoriesButton = new Button(tr("Refresh Repositories"));
        this.refreshRepositoriesButton.setOnAction(event -> {
            this.vBox.setDisable(true);
            this.overlay.toFront();
            repositoryManager.triggerRepositoryChange();
        });

        this.refreshLayout.add(refreshRepositoriesLabel, 0, 0);
        this.refreshLayout.add(refreshRepositoriesButton, 1, 0);

        GridPane.setHgrow(refreshRepositoriesLabel, Priority.ALWAYS);
    }

    private void save() {
        settingsManager.saveRepositories(repositories);
    }
}
