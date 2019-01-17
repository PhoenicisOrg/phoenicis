package org.phoenicis.javafx.components.setting.skin;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.setting.control.RepositoriesPanel;
import org.phoenicis.javafx.dialogs.ConfirmDialog;
import org.phoenicis.javafx.views.mainwindow.settings.addrepository.AddRepositoryDialog;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.types.Repository;

import java.util.List;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class RepositoriesPanelSkin extends SkinBase<RepositoriesPanel, RepositoriesPanelSkin> {
    private static final DataFormat repositoryLocationFormat = new DataFormat("application/x-java-serialized-object");

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public RepositoriesPanelSkin(RepositoriesPanel control) {
        super(control);
    }

    @Override
    public void initialise() {
        final Text title = new Text(tr("Repository Settings"));
        title.getStyleClass().add("title");

        final TableView<RepositoryLocation<? extends Repository>> repositoryLocationTable = createRepositoryLocationTable();

        VBox.setVgrow(repositoryLocationTable, Priority.ALWAYS);

        final HBox repositoryButtons = createRepositoryButtons(repositoryLocationTable);

        final HBox refreshContainer = createRefreshButtonContainer();

        final VBox container = new VBox(title, repositoryLocationTable, repositoryButtons, refreshContainer);

        container.getStyleClass().add("containerConfigurationPane");

        getChildren().addAll(container);
    }

    private TableView<RepositoryLocation<? extends Repository>> createRepositoryLocationTable() {
        final TableView<RepositoryLocation<? extends Repository>> repositoryLocationTable = new TableView<>();

        final TableColumn<RepositoryLocation<? extends Repository>, Integer> priorityColumn = new TableColumn<>(
                tr("Priority"));
        priorityColumn.setSortable(false);
        priorityColumn.setReorderable(false);
        priorityColumn.setCellValueFactory(cellDataFeatures -> new SimpleObjectProperty<>(
                getControl().getRepositoryLocations().indexOf(cellDataFeatures.getValue()) + 1));

        final TableColumn<RepositoryLocation<? extends Repository>, String> nameColumn = new TableColumn<>(
                tr("Repository name"));
        nameColumn.setSortable(false);
        nameColumn.setReorderable(false);
        nameColumn.setCellValueFactory(
                cellDataFeatures -> new SimpleObjectProperty<>(cellDataFeatures.getValue().toDisplayString()));

        repositoryLocationTable.getColumns().setAll(priorityColumn, nameColumn);

        repositoryLocationTable.setRowFactory(tv -> {
            final TableRow<RepositoryLocation<? extends Repository>> row = new TableRow<>();

            final Tooltip repositoryLocationTooltip = new Tooltip(
                    tr("Move the repository up or down to change its priority"));
            // ensure that the tooltip is only shown for non empty rows
            row.emptyProperty().addListener((Observable invalidation) -> updateTooltipInstallation(!row.isEmpty(), row,
                    repositoryLocationTooltip));
            // ensure that the tooltip is correctly initialized
            updateTooltipInstallation(!row.isEmpty(), row, repositoryLocationTooltip);

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    int index = row.getIndex();
                    Dragboard dragboard = row.startDragAndDrop(TransferMode.MOVE);

                    ClipboardContent content = new ClipboardContent();
                    content.put(repositoryLocationFormat, index);
                    dragboard.setContent(content);

                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard dragboard = event.getDragboard();

                if (dragboard.hasContent(repositoryLocationFormat)) {
                    if (row.getIndex() != (Integer) dragboard.getContent(repositoryLocationFormat)) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();

                if (db.hasContent(repositoryLocationFormat)) {
                    int draggedIndex = (Integer) db.getContent(repositoryLocationFormat);
                    RepositoryLocation<? extends Repository> draggedRepositoryLocation = getControl()
                            .getRepositoryLocations().remove(draggedIndex);

                    int dropIndex = row.isEmpty() ? getControl().getRepositoryLocations().size() : row.getIndex();

                    getControl().getRepositoryLocations().add(dropIndex, draggedRepositoryLocation);

                    event.setDropCompleted(true);
                    event.consume();
                }
            });

            return row;
        });

        Bindings.bindContent(repositoryLocationTable.getItems(), getControl().getRepositoryLocations());

        return repositoryLocationTable;
    }

    private void updateTooltipInstallation(boolean condition, Node component, Tooltip tooltip) {
        if (condition) {
            Tooltip.install(component, tooltip);
        } else {
            Tooltip.uninstall(component, tooltip);
        }
    }

    private HBox createRepositoryButtons(TableView<RepositoryLocation<? extends Repository>> repositoryLocationTable) {
        final Button addButton = new Button(tr("Add"));
        addButton.setOnAction((ActionEvent event) -> {
            AddRepositoryDialog dialog = new AddRepositoryDialog();
            dialog.initOwner(getControl().getScene().getWindow());

            Optional<RepositoryLocation<? extends Repository>> successResult = dialog.showAndWait();

            successResult
                    .ifPresent(repositoryLocation -> getControl().getRepositoryLocations().add(repositoryLocation));
        });

        final Button removeButton = new Button(tr("Remove"));
        removeButton.setOnAction((ActionEvent event) -> {
            List<RepositoryLocation<? extends Repository>> toRemove = repositoryLocationTable.getSelectionModel()
                    .getSelectedItems();

            getControl().getRepositoryLocations().removeAll(toRemove);
        });

        final Button restoreDefault = new Button(tr("Restore defaults"));
        restoreDefault.setOnAction(event -> {
            final ConfirmDialog dialog = ConfirmDialog.builder()
                    .withTitle(tr("Restore default repositories"))
                    .withMessage(tr("Are you sure you want to restore the default repositories?"))
                    .withYesCallback(() -> Platform.runLater(() -> getControl().getRepositoryLocations().setAll(
                            getControl().getRepositoryLocationLoader().getDefaultRepositoryLocations())))
                    .withOwner(getControl().getScene().getWindow())
                    .withResizable(true)
                    .build();

            dialog.showAndCallback();
        });

        return new HBox(addButton, removeButton, restoreDefault);
    }

    private HBox createRefreshButtonContainer() {
        final Label refreshRepositoriesLabel = new Label(
                tr("Fetch updates from the repositories to retrieve latest script versions"));
        refreshRepositoriesLabel.setWrapText(true);

        HBox.setHgrow(refreshRepositoriesLabel, Priority.ALWAYS);

        final Button refreshRepositoriesButton = new Button(tr("Refresh Repositories"));
        refreshRepositoriesButton.setOnAction(
                event -> Optional.ofNullable(getControl().getOnRepositoryRefresh()).ifPresent(Runnable::run));

        return new HBox(refreshRepositoriesLabel, refreshRepositoriesButton);
    }
}
