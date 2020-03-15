package org.phoenicis.javafx.components.installation.control;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.actions.None;
import org.phoenicis.javafx.components.common.actions.OpenDetailsPanel;
import org.phoenicis.javafx.components.common.control.FeaturePanel;
import org.phoenicis.javafx.components.installation.skin.InstallationsFeaturePanelSkin;
import org.phoenicis.javafx.components.installation.utils.InstallationsUtils;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.mainwindow.installations.InstallationsFilter;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;

import java.util.List;
import java.util.Optional;

/**
 * The component shown inside the Phoenicis "Installations" tab
 */
public class InstallationsFeaturePanel extends FeaturePanel<InstallationsFeaturePanel, InstallationsFeaturePanelSkin> {
    /**
     * The installations filter
     */
    private final ObjectProperty<InstallationsFilter> filter;

    /**
     * The JavaFX settings manager
     */
    private final ObjectProperty<JavaFxSettingsManager> javaFxSettingsManager;

    /**
     * The shown installation categories
     */
    private final ObservableList<InstallationCategoryDTO> installationCategories;

    /**
     * Callback for when a new installation is added
     */
    private final ObjectProperty<Runnable> onInstallationAdded;

    /**
     * The currently selected installation category
     */
    private final ObjectProperty<InstallationDTO> selectedInstallation;

    /**
     * The currently selected installation details panel
     */
    private final ObjectProperty<OpenDetailsPanel> openedDetailsPanel;

    /**
     * Constructor
     */
    public InstallationsFeaturePanel() {
        super();

        this.filter = new SimpleObjectProperty<>();
        this.javaFxSettingsManager = new SimpleObjectProperty<>();
        this.installationCategories = FXCollections.observableArrayList();
        this.onInstallationAdded = new SimpleObjectProperty<>();
        this.selectedInstallation = new SimpleObjectProperty<>();
        this.openedDetailsPanel = new SimpleObjectProperty<>(new None());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstallationsFeaturePanelSkin createSkin() {
        return new InstallationsFeaturePanelSkin(this);
    }

    /**
     * Adds a new installation to the {@link FeaturePanel} and selects it
     *
     * @param installationDTO The new installation
     */
    public void addInstallation(InstallationDTO installationDTO) {
        final List<InstallationCategoryDTO> installationCategories = InstallationsUtils
                .addInstallationToList(getInstallationCategories(), installationDTO);

        Platform.runLater(() -> {
            // update the shown installations
            getInstallationCategories().setAll(installationCategories);
            // select the added installation
            setSelectedInstallation(installationDTO);
        });

        Optional.ofNullable(getOnInstallationAdded()).ifPresent(Runnable::run);
    }

    /**
     * Removes an installation (if it exists) from the {@link FeaturePanel}
     *
     * @param installationDTO The installation to be removed
     */
    public void removeInstallation(InstallationDTO installationDTO) {
        final List<InstallationCategoryDTO> installationCategories = InstallationsUtils
                .removeInstallationFromList(getInstallationCategories(), installationDTO);

        Platform.runLater(() -> {
            getInstallationCategories().setAll(installationCategories);

            if (getSelectedInstallation().equals(installationDTO)) {
                setSelectedInstallation(null);
            }
        });
    }

    public InstallationsFilter getFilter() {
        return this.filter.get();
    }

    public ObjectProperty<InstallationsFilter> filterProperty() {
        return this.filter;
    }

    public void setFilter(InstallationsFilter filter) {
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

    public ObservableList<InstallationCategoryDTO> getInstallationCategories() {
        return this.installationCategories;
    }

    public Runnable getOnInstallationAdded() {
        return this.onInstallationAdded.get();
    }

    public ObjectProperty<Runnable> onInstallationAddedProperty() {
        return this.onInstallationAdded;
    }

    public void setOnInstallationAdded(Runnable onInstallationAdded) {
        this.onInstallationAdded.set(onInstallationAdded);
    }

    public InstallationDTO getSelectedInstallation() {
        return this.selectedInstallation.get();
    }

    public ObjectProperty<InstallationDTO> selectedInstallationProperty() {
        return this.selectedInstallation;
    }

    public void setSelectedInstallation(InstallationDTO selectedInstallation) {
        this.selectedInstallation.set(selectedInstallation);
    }

    public OpenDetailsPanel getOpenedDetailsPanel() {
        return this.openedDetailsPanel.get();
    }

    public ObjectProperty<OpenDetailsPanel> openedDetailsPanelProperty() {
        return this.openedDetailsPanel;
    }

    public void setOpenedDetailsPanel(OpenDetailsPanel openDetailsPanel) {
        this.openedDetailsPanel.set(openDetailsPanel);
    }

    /**
     * Closes the currently opened details panel
     */
    public void closeDetailsPanel() {
        // deselect the currently selected installation
        setSelectedInstallation(null);
        // close the details panel
        setOpenedDetailsPanel(new None());
    }
}
