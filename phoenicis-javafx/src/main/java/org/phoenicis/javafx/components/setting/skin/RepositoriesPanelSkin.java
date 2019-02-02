package org.phoenicis.javafx.components.setting.skin;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.setting.control.RepositoriesPanel;
import org.phoenicis.javafx.dialogs.ConfirmDialog;
import org.phoenicis.javafx.views.mainwindow.settings.addrepository.AddRepositoryDialog;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.types.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link RepositoriesPanel} component
 */
public class RepositoriesPanelSkin extends SkinBase<RepositoriesPanel, RepositoriesPanelSkin> {
    /**
     * A {@link DataFormat} for dragged row indices
     */
    private static final DataFormat repositoryLocationFormat = new DataFormat("application/x-java-serialized-object");

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public RepositoriesPanelSkin(RepositoriesPanel control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Text title = new Text(tr("Repository Settings"));
        title.getStyleClass().add("title");

        final TableView<RepositoryLocation<? extends Repository>> repositoryLocationTable = createRepositoryLocationTable();

        VBox.setVgrow(repositoryLocationTable, Priority.ALWAYS);

        final HBox repositoryButtons = createRepositoryButtons(repositoryLocationTable);

        final HBox refreshContainer = createRefreshButtonContainer();

        final VBox container = new VBox(title, repositoryLocationTable, repositoryButtons, refreshContainer);

        container.getStyleClass().addAll("settings-tab", "repositories-panel");

        getChildren().addAll(container);
    }

    /**
     * Creates a {@link TableView} containing the {@link RepositoryLocation} objects
     *
     * @return A {@link TableView} containing the {@link RepositoryLocation} objects
     */
    private TableView<RepositoryLocation<? extends Repository>> createRepositoryLocationTable() {
        final TableView<RepositoryLocation<? extends Repository>> repositoryLocationTable = new TableView<>();
        repositoryLocationTable.getStyleClass().add("repositories-table");

        // add the priority column
        repositoryLocationTable.getColumns().add(createColumn(tr("Priority"),
                repositoryLocation -> getControl().getRepositoryLocations().indexOf(repositoryLocation) + 1));

        // add the repository name column
        repositoryLocationTable.getColumns().add(createColumn(
                tr("Repository name"), RepositoryLocation::toDisplayString));

        repositoryLocationTable.setRowFactory(tv -> {
            final TableRow<RepositoryLocation<? extends Repository>> row = new TableRow<>();
            row.getStyleClass().add("repository-row");

            final Tooltip repositoryLocationTooltip = new Tooltip(
                    tr("Move the repository up or down to change its priority"));
            // ensure that the tooltip is only shown for non empty rows
            row.emptyProperty().addListener((Observable invalidation) -> {
                if (row.isEmpty()) {
                    Tooltip.uninstall(row, repositoryLocationTooltip);
                } else {
                    Tooltip.install(row, repositoryLocationTooltip);
                }
            });

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    final int index = row.getIndex();
                    final Dragboard dragboard = row.startDragAndDrop(TransferMode.MOVE);

                    // create a preview image
                    final RepositoryLocation<? extends Repository> repositoryLocation = getControl()
                            .getRepositoryLocations().get(index);
                    dragboard.setDragView(createPreviewImage(repositoryLocation));

                    // save the dragged repository index
                    final ClipboardContent content = new ClipboardContent();
                    content.put(repositoryLocationFormat, index);
                    dragboard.setContent(content);

                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                final Dragboard dragboard = event.getDragboard();

                if (dragboard.hasContent(repositoryLocationFormat)
                        && row.getIndex() != (Integer) dragboard.getContent(repositoryLocationFormat)) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(event -> {
                final Dragboard dragboard = event.getDragboard();

                if (dragboard.hasContent(repositoryLocationFormat)) {
                    final int draggedIndex = (Integer) dragboard.getContent(repositoryLocationFormat);

                    final List<RepositoryLocation<? extends Repository>> workingCopy = new ArrayList<>(
                            getControl().getRepositoryLocations());

                    final RepositoryLocation<? extends Repository> draggedRepositoryLocation = workingCopy
                            .remove(draggedIndex);

                    final int dropIndex = row.isEmpty() ? workingCopy.size() : row.getIndex();

                    workingCopy.add(dropIndex, draggedRepositoryLocation);

                    getControl().getRepositoryLocations().setAll(workingCopy);

                    event.setDropCompleted(true);
                    event.consume();
                }
            });

            return row;
        });

        Bindings.bindContent(repositoryLocationTable.getItems(), getControl().getRepositoryLocations());

        return repositoryLocationTable;
    }

    /**
     * Creates an {@link Image} object visualizing a dragged repository location
     *
     * @param repositoryLocation The dragged repository location
     * @return An {@link Image} object visualizing a dragged repository location
     */
    private Image createPreviewImage(RepositoryLocation<? extends Repository> repositoryLocation) {
        final Text text = new Text(repositoryLocation.toDisplayString());

        // force the layout to be able to create a snapshot
        new Scene(new Group(text));

        final SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);

        return text.snapshot(snapshotParameters, null);
    }

    /**
     * Creates a new {@link TableColumn} object for the given {@link String columnHeader}
     *
     * @param columnHeader The column header text
     * @param converter A converter function used to convert a {@link RepositoryLocation} object to the type of the
     *            column
     * @param <E> The type of the column
     * @return A new {@link TableColumn} object for the given {@link String columnHeader}
     */
    private <E> TableColumn<RepositoryLocation<? extends Repository>, E> createColumn(String columnHeader,
            Function<RepositoryLocation<? extends Repository>, E> converter) {
        final TableColumn<RepositoryLocation<? extends Repository>, E> column = new TableColumn<>(columnHeader);

        column.setCellValueFactory(cdf -> new SimpleObjectProperty<>(converter.apply(cdf.getValue())));
        column.setSortable(false);
        column.setReorderable(false);

        return column;
    }

    /**
     * Creates a new container for the repository buttons.
     * These buttons consist of:
     * - an add button
     * - a delete button
     * - a restore defaults button
     *
     * @param repositoryLocationTable The repository location table
     * @return A new container for the repository buttons
     */
    private HBox createRepositoryButtons(TableView<RepositoryLocation<? extends Repository>> repositoryLocationTable) {
        final Button addButton = new Button(tr("Add"));
        addButton.getStyleClass().add("repositories-add");
        addButton.setOnAction((ActionEvent event) -> {
            final AddRepositoryDialog dialog = new AddRepositoryDialog();
            dialog.initOwner(getControl().getScene().getWindow());

            final Optional<RepositoryLocation<? extends Repository>> successResult = dialog.showAndWait();

            successResult
                    .ifPresent(repositoryLocation -> getControl().getRepositoryLocations().add(0, repositoryLocation));
        });

        final Button removeButton = new Button(tr("Remove"));
        removeButton.getStyleClass().add("repositories-remove");
        removeButton.setOnAction((ActionEvent event) -> {
            final List<RepositoryLocation<? extends Repository>> toRemove = repositoryLocationTable
                    .getSelectionModel().getSelectedItems();

            getControl().getRepositoryLocations().removeAll(toRemove);
        });

        final Button restoreDefault = new Button(tr("Restore defaults"));
        restoreDefault.getStyleClass().add("repositories-restore");
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

        final HBox container = new HBox(addButton, removeButton, restoreDefault);

        container.getStyleClass().add("repositories-buttons-container");

        return container;
    }

    /**
     * Creates a container for the refresh button
     *
     * @return A container with the refresh button
     */
    private HBox createRefreshButtonContainer() {
        final Label refreshRepositoriesLabel = new Label(
                tr("Fetch updates from the repositories to retrieve latest script versions"));
        refreshRepositoriesLabel.getStyleClass().add("repositories-refresh-label");

        HBox.setHgrow(refreshRepositoriesLabel, Priority.ALWAYS);

        final Button refreshRepositoriesButton = new Button(tr("Refresh Repositories"));
        refreshRepositoriesButton.setOnAction(
                event -> Optional.ofNullable(getControl().getOnRepositoryRefresh()).ifPresent(Runnable::run));

        final HBox container = new HBox(refreshRepositoriesLabel, refreshRepositoriesButton);

        container.getStyleClass().add("repositories-refresh-container");

        return container;
    }
}
