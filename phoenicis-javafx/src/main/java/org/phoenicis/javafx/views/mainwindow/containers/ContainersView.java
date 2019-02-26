/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.phoenicis.containers.ContainerEngineController;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.engines.VerbsManager;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.container.control.ContainerInformationPanel;
import org.phoenicis.javafx.components.container.control.ContainerSidebar;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.javafx.utils.StringBindings;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;
import org.phoenicis.repository.dto.ApplicationDTO;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The containers view is responsible for showing all installed containers.
 * This view is partitioned in three sections:
 *
 * <ul>
 * <li>A sidebar, that allows the user to filter between the container categories</li>
 * <li>A list widget, which contains the installed containers</li>
 * <li>An optional details view showing details about the selected container in the list widget</li>
 * </ul>
 */
public class ContainersView extends MainWindowView<ContainerSidebar> {
    private final ContainersFilter filter;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private final ObjectProperty<ListWidgetType> selectedListWidget;

    private final ObservableList<ContainerCategoryDTO> categories;

    private final ObjectProperty<ContainerDTO> container;

    private final ObjectProperty<ContainerEngineController> containerEngineController;

    private final ObservableList<EngineSetting> engineSettings;

    private final ObjectProperty<VerbsManager> verbsManager;

    private final ObjectProperty<ApplicationDTO> verbs;

    private final ObjectProperty<EngineToolsManager> engineToolsManager;

    private final ObjectProperty<ApplicationDTO> engineTools;

    private final ObjectProperty<Consumer<ContainerDTO>> onDeleteContainer;

    private final ObjectProperty<Consumer<ContainerDTO>> onOpenFileBrowser;

    private final ObjectProperty<Consumer<ContainerDTO>> onSelectContainer;

    private final CombinedListWidget<ContainerDTO> availableContainers;

    /**
     * Constructor
     *
     * @param themeManager The theme manager
     * @param javaFxSettingsManager The javafx settings manager
     */
    public ContainersView(ThemeManager themeManager, JavaFxSettingsManager javaFxSettingsManager) {
        super(tr("Containers"), themeManager);

        this.javaFxSettingsManager = javaFxSettingsManager;

        this.selectedListWidget = new SimpleObjectProperty<>();
        this.categories = FXCollections.observableArrayList();
        this.container = new SimpleObjectProperty<>();
        this.containerEngineController = new SimpleObjectProperty<>();
        this.engineSettings = FXCollections.observableArrayList();
        this.verbsManager = new SimpleObjectProperty<>();
        this.verbs = new SimpleObjectProperty<>();
        this.engineToolsManager = new SimpleObjectProperty<>();
        this.engineTools = new SimpleObjectProperty<>();

        this.onDeleteContainer = new SimpleObjectProperty<>();
        this.onOpenFileBrowser = new SimpleObjectProperty<>();
        this.onSelectContainer = new SimpleObjectProperty<>();

        this.filter = new ContainersFilter();
        this.filter.selectedContainerCategoryProperty().addListener((Observable invalidation) -> closeDetailsView());

        this.availableContainers = createContainerListWidget();

        final ContainerSidebar containerSidebar = createContainersSidebar();

        setSidebar(containerSidebar);

        this.content.rightProperty().bind(Bindings.when(Bindings.isNotNull(containerProperty()))
                .then(createContainerDetailsPanel()).otherwise(new SimpleObjectProperty<>()));
    }

    private ContainerSidebar createContainersSidebar() {
        /*
         * initialize the container categories by sorting them
         */
        final SortedList<ContainerCategoryDTO> sortedCategories = this.categories
                .sorted(Comparator.comparing(ContainerCategoryDTO::getName));

        final ContainerSidebar sidebar = new ContainerSidebar(this.filter, sortedCategories, this.selectedListWidget);

        // set the default selection
        sidebar.setSelectedListWidget(javaFxSettingsManager.getContainersListType());

        // save changes to the list widget selection to the hard drive
        sidebar.selectedListWidgetProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                javaFxSettingsManager.setContainersListType(newValue);
                javaFxSettingsManager.save();
            }
        });

        return sidebar;
    }

    private CombinedListWidget<ContainerDTO> createContainerListWidget() {
        /*
         * initialize the container lists by:
         * 1. sorting the containers by their name
         * 2. filtering the containers
         */
        final FilteredList<ContainerDTO> filteredContainers = ConcatenatedList
                .create(new MappedList<>(
                        this.categories.sorted(Comparator.comparing(ContainerCategoryDTO::getName)),
                        ContainerCategoryDTO::getContainers))
                .sorted(Comparator.comparing(ContainerDTO::getName))
                .filtered(this.filter::filter);

        filteredContainers.predicateProperty().bind(
                Bindings.createObjectBinding(() -> this.filter::filter, this.filter.searchTermProperty()));

        final ObservableList<ListWidgetElement<ContainerDTO>> listWidgetEntries = new MappedList<>(filteredContainers,
                ListWidgetElement::create);

        final CombinedListWidget<ContainerDTO> listWidget = new CombinedListWidget<>(listWidgetEntries);

        listWidget.selectedListWidgetProperty().bind(this.selectedListWidget);

        listWidget.selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showContainerDetails(newValue.getItem());
            }
        });

        return listWidget;
    }

    private DetailsPanel createContainerDetailsPanel() {
        final ContainerInformationPanel containerInformationPanel = new ContainerInformationPanel();

        containerInformationPanel.containerProperty().bind(containerProperty());
        containerInformationPanel.containerEngineControllerProperty().bind(containerEngineControllerProperty());
        Bindings.bindContent(containerInformationPanel.getEngineSettings(), getEngineSettings());
        containerInformationPanel.verbsManagerProperty().bind(verbsManagerProperty());
        containerInformationPanel.verbsProperty().bind(verbsProperty());
        containerInformationPanel.engineToolsManagerProperty().bind(engineToolsManagerProperty());
        containerInformationPanel.engineToolsProperty().bind(engineToolsProperty());

        containerInformationPanel.onDeleteContainerProperty().bind(onDeleteContainerProperty());
        containerInformationPanel.onOpenFileBrowserProperty().bind(onOpenFileBrowserProperty());

        final DetailsPanel containerDetailsPanel = new DetailsPanel();

        containerDetailsPanel.titleProperty().bind(StringBindings.map(containerProperty(), ContainerDTO::getName));
        containerDetailsPanel.setContent(containerInformationPanel);
        containerDetailsPanel.setOnClose(this::closeDetailsView);

        containerDetailsPanel.prefWidthProperty().bind(content.widthProperty().divide(3));

        return containerDetailsPanel;
    }

    /**
     * Populate with a list of container categories
     *
     * @param categories ContainerCategoryDTO
     */
    public void populate(List<ContainerCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);

            closeDetailsView();
            setCenter(this.availableContainers);
        });
    }

    /**
     * closes the details view
     */
    @Override
    public void closeDetailsView() {
        setContainer(null);
    }

    /**
     * Displays the details view for a given container.
     *
     * @param container The container, whose details should be shown.
     */
    private void showContainerDetails(ContainerDTO container) {
        // TODO: separate details panel and controller
        Optional.ofNullable(getOnSelectContainer()).ifPresent(consumer -> consumer.accept(container));

        setContainer((WinePrefixContainerDTO) container);
    }

    /**
     * Sets the callback, which is called when a container has been selected
     *
     * @param onSelectContainer The callback to be called when a container has been selected
     */
    public void setOnSelectContainer(Consumer<ContainerDTO> onSelectContainer) {
        this.onSelectContainer.setValue(onSelectContainer);
    }

    public ContainerDTO getContainer() {
        return this.container.get();
    }

    public ObjectProperty<ContainerDTO> containerProperty() {
        return this.container;
    }

    public void setContainer(ContainerDTO container) {
        this.container.set(container);
    }

    public ContainerEngineController getContainerEngineController() {
        return this.containerEngineController.get();
    }

    public ObjectProperty<ContainerEngineController> containerEngineControllerProperty() {
        return this.containerEngineController;
    }

    public void setContainerEngineController(ContainerEngineController containerEngineController) {
        this.containerEngineController.set(containerEngineController);
    }

    public ObservableList<EngineSetting> getEngineSettings() {
        return this.engineSettings;
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

    public ApplicationDTO getVerbs() {
        return this.verbs.get();
    }

    public ObjectProperty<ApplicationDTO> verbsProperty() {
        return this.verbs;
    }

    public void setVerbs(ApplicationDTO verbs) {
        this.verbs.set(verbs);
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

    public ApplicationDTO getEngineTools() {
        return this.engineTools.get();
    }

    public ObjectProperty<ApplicationDTO> engineToolsProperty() {
        return this.engineTools;
    }

    public void setEngineTools(ApplicationDTO engineTools) {
        this.engineTools.set(engineTools);
    }

    public Consumer<ContainerDTO> getOnDeleteContainer() {
        return this.onDeleteContainer.get();
    }

    public ObjectProperty<Consumer<ContainerDTO>> onDeleteContainerProperty() {
        return this.onDeleteContainer;
    }

    public void setOnDeleteContainer(Consumer<ContainerDTO> onDeleteContainer) {
        this.onDeleteContainer.set(onDeleteContainer);
    }

    public Consumer<ContainerDTO> getOnOpenFileBrowser() {
        return this.onOpenFileBrowser.get();
    }

    public ObjectProperty<Consumer<ContainerDTO>> onOpenFileBrowserProperty() {
        return this.onOpenFileBrowser;
    }

    public void setOnOpenFileBrowser(Consumer<ContainerDTO> onOpenFileBrowser) {
        this.onOpenFileBrowser.set(onOpenFileBrowser);
    }

    public Consumer<ContainerDTO> getOnSelectContainer() {
        return this.onSelectContainer.get();
    }

    public ObjectProperty<Consumer<ContainerDTO>> onSelectContainerProperty() {
        return this.onSelectContainer;
    }
}
