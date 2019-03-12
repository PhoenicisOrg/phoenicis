package org.phoenicis.javafx.components.container.control;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.phoenicis.containers.ContainerEngineController;
import org.phoenicis.containers.ContainersManager;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.engines.VerbsManager;
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

public class ContainersFeaturePanel extends FeaturePanel<ContainersFeaturePanel, ContainersFeaturePanelSkin> {
    private final ObjectProperty<ContainersFilter> filter;

    private final ObjectProperty<JavaFxSettingsManager> javaFxSettingsManager;

    private final ObservableList<ContainerCategoryDTO> categories;

    private final ObjectProperty<ContainersManager> containersManager;

    private final ObjectProperty<ContainerEngineController> containerEngineController;

    private final ObjectProperty<VerbsManager> verbsManager;

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

    private final ObjectProperty<ContainerDTO> selectedContainer;

    public ContainersFeaturePanel() {
        super();

        this.filter = new SimpleObjectProperty<>();
        this.javaFxSettingsManager = new SimpleObjectProperty<>();
        this.categories = FXCollections.observableArrayList();
        this.containersManager = new SimpleObjectProperty<>();
        this.containerEngineController = new SimpleObjectProperty<>();
        this.verbsManager = new SimpleObjectProperty<>();
        this.engineToolsManager = new SimpleObjectProperty<>();
        this.engineSettings = FXCollections.observableHashMap();
        this.verbs = FXCollections.observableHashMap();
        this.engineTools = FXCollections.observableHashMap();
        this.selectedContainer = new SimpleObjectProperty<>();
    }

    @Override
    public ContainersFeaturePanelSkin createSkin() {
        return new ContainersFeaturePanelSkin(this);
    }

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
        return filter.get();
    }

    public ObjectProperty<ContainersFilter> filterProperty() {
        return filter;
    }

    public void setFilter(ContainersFilter filter) {
        this.filter.set(filter);
    }

    public JavaFxSettingsManager getJavaFxSettingsManager() {
        return javaFxSettingsManager.get();
    }

    public ObjectProperty<JavaFxSettingsManager> javaFxSettingsManagerProperty() {
        return javaFxSettingsManager;
    }

    public void setJavaFxSettingsManager(JavaFxSettingsManager javaFxSettingsManager) {
        this.javaFxSettingsManager.set(javaFxSettingsManager);
    }

    public ObservableList<ContainerCategoryDTO> getCategories() {
        return categories;
    }

    public ContainersManager getContainersManager() {
        return containersManager.get();
    }

    public ObjectProperty<ContainersManager> containersManagerProperty() {
        return containersManager;
    }

    public void setContainersManager(ContainersManager containersManager) {
        this.containersManager.set(containersManager);
    }

    public ContainerEngineController getContainerEngineController() {
        return containerEngineController.get();
    }

    public ObjectProperty<ContainerEngineController> containerEngineControllerProperty() {
        return containerEngineController;
    }

    public void setContainerEngineController(ContainerEngineController containerEngineController) {
        this.containerEngineController.set(containerEngineController);
    }

    public VerbsManager getVerbsManager() {
        return verbsManager.get();
    }

    public ObjectProperty<VerbsManager> verbsManagerProperty() {
        return verbsManager;
    }

    public void setVerbsManager(VerbsManager verbsManager) {
        this.verbsManager.set(verbsManager);
    }

    public EngineToolsManager getEngineToolsManager() {
        return engineToolsManager.get();
    }

    public ObjectProperty<EngineToolsManager> engineToolsManagerProperty() {
        return engineToolsManager;
    }

    public void setEngineToolsManager(EngineToolsManager engineToolsManager) {
        this.engineToolsManager.set(engineToolsManager);
    }

    public ObservableMap<String, List<EngineSetting>> getEngineSettings() {
        return engineSettings;
    }

    public ObservableMap<String, ApplicationDTO> getVerbs() {
        return verbs;
    }

    public ObservableMap<String, ApplicationDTO> getEngineTools() {
        return engineTools;
    }

    public ContainerDTO getSelectedContainer() {
        return selectedContainer.get();
    }

    public ObjectProperty<ContainerDTO> selectedContainerProperty() {
        return selectedContainer;
    }

    public void setSelectedContainer(ContainerDTO selectedContainer) {
        this.selectedContainer.set(selectedContainer);
    }
}
