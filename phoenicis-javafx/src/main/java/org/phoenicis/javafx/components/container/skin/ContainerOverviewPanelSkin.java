package org.phoenicis.javafx.components.container.skin;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.container.control.ContainerOverviewPanel;
import org.phoenicis.javafx.utils.StringBindings;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link ContainerOverviewPanel} component
 */
public class ContainerOverviewPanelSkin extends SkinBase<ContainerOverviewPanel, ContainerOverviewPanelSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ContainerOverviewPanelSkin(ContainerOverviewPanel control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Text title = new Text(tr("Information"));
        title.getStyleClass().add("title");

        final GridPane overviewGrid = new GridPane();
        overviewGrid.getStyleClass().add("information-grid");

        addName(overviewGrid);
        addPath(overviewGrid);
        addInstalledShortcuts(overviewGrid);
        // TODO: find generic solution which works for all container types
        addVersion(overviewGrid);
        // TODO: find generic solution which works for all container types
        addArchitecture(overviewGrid);
        // TODO: find generic solution which works for all container types
        addDistribution(overviewGrid);

        final HBox managementButtonContainer = createManagementButtonContainer();
        managementButtonContainer.getStyleClass().add("container-management-button-container");

        final VBox container = new VBox(title, overviewGrid, managementButtonContainer);
        container.getStyleClass().addAll("container-details-panel", "container-overview-panel");

        getChildren().addAll(container);
    }

    /**
     * Creates a {@link HBox} containing the management buttons for the container
     *
     * @return The created {@link HBox}
     */
    private HBox createManagementButtonContainer() {
        final Button deleteButton = new Button(tr("Delete container"));
        deleteButton.setOnMouseClicked(event -> Optional.ofNullable(getControl().getOnDeleteContainer())
                .ifPresent(onDeleteContainer -> onDeleteContainer.accept(getControl().getContainer())));

        final Button changeEngineButton = new Button(tr("Change engine version"));
        changeEngineButton.setOnMouseClicked(event -> Optional.ofNullable(getControl().getOnChangeEngine())
                .ifPresent(onChangeEngine -> onChangeEngine.accept(getControl().getContainer())));

        final Button openFileBrowserButton = new Button(tr("Open in file browser"));
        openFileBrowserButton.setOnMouseClicked(event -> Optional.ofNullable(getControl().getOnOpenFileBrowser())
                .ifPresent(onOpenFileBrowser -> onOpenFileBrowser.accept(getControl().getContainer())));

        return new HBox(deleteButton, changeEngineButton, openFileBrowserButton);
    }

    /**
     * Adds the name information of the container to the {@link GridPane overviewGrid}
     *
     * @param overviewGrid The grid containing the overview information
     */
    private void addName(final GridPane overviewGrid) {
        final int row = overviewGrid.getRowCount();

        final Text nameDescription = new Text(tr("Name:"));
        nameDescription.getStyleClass().add("captionTitle");

        final Label nameOutput = new Label();
        nameOutput.textProperty().bind(StringBindings.map(getControl().containerProperty(), ContainerDTO::getName));
        nameOutput.setWrapText(true);

        overviewGrid.addRow(row, nameDescription, nameOutput);
    }

    /**
     * Adds the path information of the container to the {@link GridPane overviewGrid}
     *
     * @param overviewGrid The grid containing the overview information
     */
    private void addPath(final GridPane overviewGrid) {
        final int row = overviewGrid.getRowCount();

        final Text pathDescription = new Text(tr("Path:"));
        pathDescription.getStyleClass().add("captionTitle");

        final Label pathOutput = new Label();
        pathOutput.textProperty().bind(StringBindings.map(getControl().containerProperty(), ContainerDTO::getPath));
        pathOutput.setWrapText(true);

        overviewGrid.addRow(row, pathDescription, pathOutput);
    }

    /**
     * Adds the installed shortcuts of the container to the {@link GridPane overviewGrid}
     *
     * @param overviewGrid The grid containing the overview information
     */
    private void addInstalledShortcuts(final GridPane overviewGrid) {
        final int row = overviewGrid.getRowCount();

        final Text installedShortcutsDescription = new Text(tr("Installed shortcuts:"));
        installedShortcutsDescription.getStyleClass().add("captionTitle");

        final Label installedShortcutsOutput = new Label();
        installedShortcutsOutput.textProperty()
                .bind(StringBindings.map(getControl().containerProperty(),
                        container -> container.getInstalledShortcuts().stream()
                                .map(shortcutDTO -> shortcutDTO.getInfo().getName())
                                .collect(Collectors.joining("; "))));
        installedShortcutsOutput.setWrapText(true);

        overviewGrid.addRow(row, installedShortcutsDescription, installedShortcutsOutput);
    }

    /**
     * Adds the wine version information of the container to the {@link GridPane overviewGrid}
     *
     * @param overviewGrid The grid containing the overview information
     */
    private void addVersion(final GridPane overviewGrid) {
        final int row = overviewGrid.getRowCount();

        final Text versionDescription = new Text(tr("Wine version:"));
        versionDescription.getStyleClass().add("captionTitle");

        final Label versionOutput = new Label();
        versionOutput.textProperty()
                .bind(StringBindings.map(getControl().containerProperty(), WinePrefixContainerDTO::getVersion));
        versionOutput.setWrapText(true);

        overviewGrid.addRow(row, versionDescription, versionOutput);
    }

    /**
     * Adds the wine architecture information of the container to the {@link GridPane overviewGrid}
     *
     * @param overviewGrid The grid containing the overview information
     */
    private void addArchitecture(final GridPane overviewGrid) {
        final int row = overviewGrid.getRowCount();

        final Text architectureDescription = new Text(tr("Wine architecture:"));
        architectureDescription.getStyleClass().add("captionTitle");

        final Label architectureOutput = new Label();
        architectureOutput.textProperty()
                .bind(StringBindings.map(getControl().containerProperty(), WinePrefixContainerDTO::getArchitecture));
        architectureOutput.setWrapText(true);

        overviewGrid.addRow(row, architectureDescription, architectureOutput);
    }

    /**
     * Adds the wine distribution information of the container to the {@link GridPane overviewGrid}
     *
     * @param overviewGrid The grid containing the overview information
     */
    private void addDistribution(final GridPane overviewGrid) {
        final int row = overviewGrid.getRowCount();

        final Text distributionDescription = new Text(tr("Wine distribution:"));
        distributionDescription.getStyleClass().add("captionTitle");

        final Label distributionOutput = new Label();
        distributionOutput.textProperty()
                .bind(StringBindings.map(getControl().containerProperty(), WinePrefixContainerDTO::getDistribution));
        distributionOutput.setWrapText(true);

        overviewGrid.addRow(row, distributionDescription, distributionOutput);
    }
}
