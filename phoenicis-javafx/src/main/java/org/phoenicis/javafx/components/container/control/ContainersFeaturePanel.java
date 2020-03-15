package org.phoenicis.javafx.components.container.control;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.phoenicis.containers.ContainersManager;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.engines.EnginesManager;
import org.phoenicis.engines.VerbsManager;
import org.phoenicis.javafx.components.common.actions.None;
import org.phoenicis.javafx.components.common.actions.OpenDetailsPanel;
import org.phoenicis.javafx.components.common.control.FeaturePanel;
import org.phoenicis.javafx.components.container.skin.ContainersFeaturePanelSkin;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.dialogs.SimpleConfirmDialog;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersFilter;
import org.phoenicis.repository.dto.ApplicationDTO;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The component shown inside the Phoenicis "Containers" tab
 */
public class ContainersFeaturePanel extends FeaturePanel<ContainersFeaturePanel, ContainersFeaturePanelSkin> {
    /**
     * The container filter
     */
    private final ObjectProperty<ContainersFilter> filter;

    /**
     * The JavaFX settings manager
     */
    private final ObjectProperty<JavaFxSettingsManager> javaFxSettingsManager;

    /**
     * A list of all shown {@link ContainerCategoryDTO} objects
     */
    private final ObservableList<ContainerCategoryDTO> categories;

    /**
     * The container manager used to fetch the existing containers
     */
    private final ObjectProperty<ContainersManager> containersManager;

    /**
     * The engines manager
     */
    private final ObjectProperty<EnginesManager> enginesManager;

    /**
     * The verbs manager
     */
    private final ObjectProperty<VerbsManager> verbsManager;

    /**
     * the engine tools manager
     */
    private final ObjectProperty<EngineToolsManager> engineToolsManager;

    /**
     * The engine settings per engine
     */
    private final ObservableMap<String, List<EngineSetting>> engineSettings;

    /**
     * The verbs per engine
     */
    private final ObservableMap<String, ApplicationDTO> verbs;

    /**
     * The engine tools per engine
     */
    private final ObservableMap<String, ApplicationDTO> engineTools;

    /**
     * The currently selected (visible) container
     */
    private final ObjectProperty<ContainerDTO> selectedContainer;

    /**
     * The currently selected details panel action
     */
    private final ObjectProperty<OpenDetailsPanel> openedDetailsPanel;

    /**
     * Constructor
     */
    public ContainersFeaturePanel() {
        super();

        this.filter = new SimpleObjectProperty<>();
        this.javaFxSettingsManager = new SimpleObjectProperty<>();
        this.categories = FXCollections.observableArrayList();
        this.containersManager = new SimpleObjectProperty<>();
        this.enginesManager = new SimpleObjectProperty<>();
        this.verbsManager = new SimpleObjectProperty<>();
        this.engineToolsManager = new SimpleObjectProperty<>();
        this.engineSettings = FXCollections.observableHashMap();
        this.verbs = FXCollections.observableHashMap();
        this.engineTools = FXCollections.observableHashMap();
        this.selectedContainer = new SimpleObjectProperty<>();
        this.openedDetailsPanel = new SimpleObjectProperty<>(new None());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainersFeaturePanelSkin createSkin() {
        return new ContainersFeaturePanelSkin(this);
    }

    /**
     * Deletes a given container
     *
     * @param container The container
     */
    public void deleteContainer(final ContainerDTO container) {
        final SimpleConfirmDialog confirmMessage = SimpleConfirmDialog.builder()
                .withTitle(tr("Delete {0} container", container.getName()))
                .withMessage(tr("Are you sure you want to delete the {0} container?", container.getName()))
                .withOwner(getScene().getWindow())
                .withYesCallback(() -> {
                    getContainersManager().deleteContainer(container,
                            unused -> Platform.runLater(() -> setSelectedContainer(null)),
                            e -> Platform.runLater(() -> {
                                final ErrorDialog errorDialog = ErrorDialog.builder()
                                        .withMessage(tr("Error"))
                                        .withException(e)
                                        .withOwner(getScene().getWindow())
                                        .build();

                                errorDialog.showAndWait();
                            }));

                    getContainersManager().fetchContainers(
                            containerCategories -> Platform.runLater(() -> categories.setAll(containerCategories)),
                            e -> Platform.runLater(() -> {
                                final ErrorDialog errorDialog = ErrorDialog.builder()
                                        .withMessage(tr("Loading containers failed."))
                                        .withException(e)
                                        .withOwner(getScene().getWindow())
                                        .build();

                                errorDialog.showAndWait();
                            }));
                })
                .build();

        confirmMessage.showAndCallback();
    }

    /**
     * Opens a dialog to change the engine version used for a given container
     *
     * @param container The container
     */
    public void changeEngineVersion(final ContainerDTO container) {
        EnginesManager enginesManager = getEnginesManager();

        if (enginesManager != null) {
            final String engineId = container.getEngine().toLowerCase();

            enginesManager.getEngine(engineId,
                    engine -> engine.changeVersion(container.getName()),
                    exception -> Platform.runLater(() -> {
                        final ErrorDialog errorDialog = ErrorDialog.builder()
                                .withMessage(tr("Error during engine engine version change"))
                                .withException(exception)
                                .withOwner(getScene().getWindow())
                                .build();

                        errorDialog.showAndWait();
                    }));
        }
    }

    /**
     * Opens the given container in a file browser
     *
     * @param container The container
     */
    public void openFileBrowser(final ContainerDTO container) {
        try {
            final File containerDir = new File(container.getPath());

            EventQueue.invokeLater(() -> {
                try {
                    Desktop.getDesktop().open(containerDir);
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        final ErrorDialog errorDialog = ErrorDialog.builder()
                                .withMessage(tr("Cannot open container {0} in file browser", container.getPath()))
                                .withException(e)
                                .withOwner(getScene().getWindow())
                                .build();

                        errorDialog.showAndWait();
                    });
                }
            });
        } catch (IllegalArgumentException e) {
            Platform.runLater(() -> {
                final ErrorDialog errorDialog = ErrorDialog.builder()
                        .withMessage(tr("Cannot open container {0} in file browser", container.getPath()))
                        .withException(e)
                        .withOwner(getScene().getWindow())
                        .build();

                errorDialog.showAndWait();
            });
        }
    }

    public ContainersFilter getFilter() {
        return this.filter.get();
    }

    public ObjectProperty<ContainersFilter> filterProperty() {
        return this.filter;
    }

    public void setFilter(ContainersFilter filter) {
        this.filter.set(filter);
    }

    public JavaFxSettingsManager getJavaFxSettingsManager() {
        return this.javaFxSettingsManager.get();
    }

    public ObjectProperty<JavaFxSettingsManager> javaFxSettingsManagerProperty() {
        return this.javaFxSettingsManager;
    }

    public void setJavaFxSettingsManager(JavaFxSettingsManager javaFxSettingsManager) {
        this.javaFxSettingsManager.set(javaFxSettingsManager);
    }

    public ObservableList<ContainerCategoryDTO> getCategories() {
        return this.categories;
    }

    public ContainersManager getContainersManager() {
        return this.containersManager.get();
    }

    public ObjectProperty<ContainersManager> containersManagerProperty() {
        return this.containersManager;
    }

    public void setContainersManager(ContainersManager containersManager) {
        this.containersManager.set(containersManager);
    }

    public EnginesManager getEnginesManager() {
        return this.enginesManager.get();
    }

    public ObjectProperty<EnginesManager> enginesManagerProperty() {
        return this.enginesManager;
    }

    public void setEnginesManager(EnginesManager enginesManager) {
        this.enginesManager.set(enginesManager);
    }

    public VerbsManager getVerbsManager() {
        return this.verbsManager.get();
    }

    public ObjectProperty<VerbsManager> verbsManagerProperty() {
        return this.verbsManager;
    }

    public void setVerbsManager(VerbsManager verbsManager) {
        this.verbsManager.set(verbsManager);
    }

    public EngineToolsManager getEngineToolsManager() {
        return this.engineToolsManager.get();
    }

    public ObjectProperty<EngineToolsManager> engineToolsManagerProperty() {
        return this.engineToolsManager;
    }

    public void setEngineToolsManager(EngineToolsManager engineToolsManager) {
        this.engineToolsManager.set(engineToolsManager);
    }

    public ObservableMap<String, List<EngineSetting>> getEngineSettings() {
        return this.engineSettings;
    }

    public ObservableMap<String, ApplicationDTO> getVerbs() {
        return this.verbs;
    }

    public ObservableMap<String, ApplicationDTO> getEngineTools() {
        return this.engineTools;
    }

    public ContainerDTO getSelectedContainer() {
        return this.selectedContainer.get();
    }

    public ObjectProperty<ContainerDTO> selectedContainerProperty() {
        return this.selectedContainer;
    }

    public void setSelectedContainer(ContainerDTO selectedContainer) {
        this.selectedContainer.set(selectedContainer);
    }

    public OpenDetailsPanel getOpenedDetailsPanel() {
        return this.openedDetailsPanel.get();
    }

    public ObjectProperty<OpenDetailsPanel> openedDetailsPanelProperty() {
        return this.openedDetailsPanel;
    }

    public void setOpenedDetailsPanel(OpenDetailsPanel action) {
        this.openedDetailsPanel.setValue(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeDetailsPanel() {
        // deselect the currently selected container
        setSelectedContainer(null);
        // close the details panel
        setOpenedDetailsPanel(new None());
    }
}
