package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.phoenicis.apps.RepositoryManager;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.settings.SettingsManager;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.translate;

/**
 * This class represents the "Repositories" settings category
 *
 * @author marc
 * @since 23.04.17
 */
public class RepositoriesPanel extends VBox {
    private SettingsManager settingsManager;
    private RepositoryManager repositoryManager;

    private Text title;

    private GridPane repositoryGrid;

    private Text repositoryText;
    private VBox repositoryLayout;
    private ListView<String> repositoryListView;
    private HBox repositoryButtonLayout;
    private Button addButton;
    private Button removeButton;

    private Label priorityHint;

    private GridPane refreshLayout;

    private Label refreshRepositoriesLabel;
    private Button refreshRepositoriesButton;

    private ObservableList<String> repositories;

    /**
     * Constructor
     *
     * @param settingsManager The settings manager
     * @param repositoryManager The repository manager
     */
    public RepositoriesPanel(SettingsManager settingsManager, RepositoryManager repositoryManager) {
        super();

        this.settingsManager = settingsManager;
        this.repositoryManager = repositoryManager;
        this.repositories = FXCollections.observableArrayList(settingsManager.getRepository().split(";"));

        this.getStyleClass().add("containerConfigurationPane");

        this.populateRepositoryGrid();
        this.populateRepositoryLegend();
        this.populateRepositoryRefresh();

        this.initializeRefreshCallback();

        this.getChildren().setAll(title, repositoryGrid, priorityHint, refreshLayout);
    }

    private void initializeRefreshCallback() {
        repositoryManager.addCallbacks(categories -> {
            Platform.runLater(() -> {
                refreshRepositoriesButton.getStyleClass().remove("refreshIcon");
                refreshRepositoriesButton.setDisable(false);
            });
        }, error -> {
        });
    }

    private void populateRepositoryGrid() {
        this.title = new TextWithStyle(translate("Repositories Settings"), "title");

        this.repositoryGrid = new GridPane();
        this.repositoryGrid.getStyleClass().add("grid");
        this.repositoryGrid.setHgap(20);
        this.repositoryGrid.setVgap(10);

        this.repositoryText = new TextWithStyle(translate("Repository:"), "captionTitle");

        this.repositoryLayout = new VBox();
        this.repositoryLayout.setSpacing(5);

        this.repositoryListView = new ListView<>(repositories);
        this.repositoryListView.setPrefSize(400, 100);
        this.repositoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.repositoryListView.setEditable(true);
        this.repositoryListView.setCellFactory(param -> new DragableRepositoryListCell((repositoryUrl, toIndex) -> {
            this.repositoryManager.moveRepository(repositoryUrl, toIndex.intValue());

            this.save();
        }));

        this.repositoryButtonLayout = new HBox();
        this.repositoryButtonLayout.setSpacing(5);

        this.addButton = new Button();
        this.addButton.setText("Add");
        this.addButton.setOnAction((ActionEvent event) -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.initOwner(getScene().getWindow());
            dialog.setTitle("Add repository");
            dialog.setHeaderText("Add repository");
            dialog.setContentText("Please add the new repository:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newRepository -> {
                repositories.add(0, newRepository);

                this.save();

                repositoryManager.addRepositories(0, newRepository);
            });
        });

        this.removeButton = new Button();
        this.removeButton.setText("Remove");
        this.removeButton.setOnAction((ActionEvent event) -> {
            String[] toRemove = repositoryListView.getSelectionModel().getSelectedItems().toArray(new String[0]);

            repositories.removeAll(toRemove);

            this.save();

            repositoryManager.removeRepositories(toRemove);
        });

        this.repositoryButtonLayout.getChildren().addAll(addButton, removeButton);

        this.repositoryLayout.getChildren().addAll(repositoryListView, repositoryButtonLayout);

        this.repositoryGrid.add(repositoryText, 0, 0);
        this.repositoryGrid.add(repositoryLayout, 1, 0);

        GridPane.setValignment(repositoryText, VPos.TOP);
    }

    private void populateRepositoryLegend() {
        this.priorityHint = new Label(translate("The value in front of each repository is its priority. The higher the priority is, the more important the scripts inside the repository are."));
        this.priorityHint.setWrapText(true);
        this.priorityHint.setPadding(new Insets(10));
    }

    private void populateRepositoryRefresh() {
        // Refresh Repositories
        this.refreshLayout = new GridPane();
        this.refreshLayout.setHgap(20);
        this.refreshLayout.setVgap(10);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);
        this.refreshLayout.getColumnConstraints().add(columnConstraints);

        this.refreshRepositoriesLabel = new Label(translate("Fetch updates for the repositories to retrieve the newest script versions"));
        this.refreshRepositoriesLabel.setWrapText(true);

        this.refreshRepositoriesButton = new Button("Refresh Repositories");
        this.refreshRepositoriesButton.getStyleClass().add("buttonWithIcon");
        this.refreshRepositoriesButton.setOnAction(event -> {
            refreshRepositoriesButton.getStyleClass().add("refreshIcon");
            refreshRepositoriesButton.setDisable(true);
            repositoryManager.triggerRepositoryChange();
        });

        this.refreshLayout.add(refreshRepositoriesLabel, 0, 0);
        this.refreshLayout.add(refreshRepositoriesButton, 1, 0);
    }

    private void save() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String repository : repositories) {
            stringBuilder.append(repository).append(";");
        }
        settingsManager.setRepository(stringBuilder.toString());

        settingsManager.save();
    }
}
