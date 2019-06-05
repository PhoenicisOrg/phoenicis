package org.phoenicis.javafx.components.container.skin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.container.control.*;
import org.phoenicis.javafx.utils.CollectionBindings;
import org.phoenicis.javafx.utils.ObjectBindings;

import java.util.List;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link ContainerInformationPanel} component
 */
public class ContainerInformationPanelSkin extends SkinBase<ContainerInformationPanel, ContainerInformationPanelSkin> {
    /**
     * An {@link ObservableList} containing all shown tabs.
     * This field is required to handle the {@link java.lang.ref.WeakReference} instances used by the {@link Bindings}
     * methods correctly
     */
    private final ObservableList<Tab> concatenatedTabs;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ContainerInformationPanelSkin(ContainerInformationPanel control) {
        super(control);

        // initialise the "Overview" tab
        final ObservableList<Tab> overviewTab = FXCollections.singletonObservableList(createContainerOverviewTab());

        // initialise the "Engine settings" tab
        final ObjectBinding<Tab> engineSettingsBinding = Bindings
                .when(Bindings.isNotEmpty(getControl().getEngineSettings()))
                .then(createContainerEngineSettingsTab()).otherwise(new SimpleObjectProperty<>());
        final ObservableList<Tab> engineSettingsTab = CollectionBindings.mapToList(engineSettingsBinding,
                engineSettings -> Optional.ofNullable(engineSettings).map(List::of).orElse(List.of()));

        // initialise the "Verbs" tab
        final Tab verbsTabInstance = createContainerVerbsTab();
        final ObservableList<Tab> verbsTab = CollectionBindings.mapToList(getControl().verbsProperty(),
                verbs -> verbs != null ? List.of(verbsTabInstance) : List.of());

        // initialise the "Engine tools" tab
        final Tab engineToolsTabInstance = createContainerEngineToolsTab();
        final ObservableList<Tab> engineToolsTab = CollectionBindings.mapToList(getControl().engineToolsProperty(),
                engineTools -> engineTools != null ? List.of(engineToolsTabInstance) : List.of());

        // initialise the "Tools" tab
        final ObservableList<Tab> toolsTab = FXCollections.singletonObservableList(createContainerToolsTab());

        this.concatenatedTabs = ConcatenatedList.create(overviewTab, engineSettingsTab, verbsTab, engineToolsTab,
                toolsTab);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final TabPane container = new TabPane();
        container.getStyleClass().add("container-information-panel");

        Bindings.bindContent(container.getTabs(), this.concatenatedTabs);

        getChildren().addAll(container);
    }

    private Tab createContainerOverviewTab() {
        final ContainerOverviewPanel containerOverviewPanel = new ContainerOverviewPanel();

        containerOverviewPanel.containerProperty().bind(
                ObjectBindings.map(getControl().containerProperty(), container -> (WinePrefixContainerDTO) container));
        containerOverviewPanel.onDeleteContainerProperty().bind(getControl().onDeleteContainerProperty());
        containerOverviewPanel.onChangeEngineProperty().bind(getControl().onChangeEngineProperty());
        containerOverviewPanel.onOpenFileBrowserProperty().bind(getControl().onOpenFileBrowserProperty());

        final Tab containerOverviewTab = new Tab(tr("Information"), containerOverviewPanel);

        containerOverviewTab.setClosable(false);

        return containerOverviewTab;
    }

    private Tab createContainerEngineSettingsTab() {
        final ContainerEngineSettingsPanel containerEngineSettingsPanel = new ContainerEngineSettingsPanel();

        containerEngineSettingsPanel.containerProperty().bind(getControl().containerProperty());
        Bindings.bindContent(containerEngineSettingsPanel.getEngineSettings(), getControl().getEngineSettings());

        final Tab engineSettingsTab = new Tab(tr(tr("Engine Settings")), containerEngineSettingsPanel);

        engineSettingsTab.setClosable(false);

        return engineSettingsTab;
    }

    private Tab createContainerVerbsTab() {
        final ContainerVerbsPanel containerVerbsPanel = new ContainerVerbsPanel();

        containerVerbsPanel.containerProperty().bind(getControl().containerProperty());
        containerVerbsPanel.verbsProperty().bind(getControl().verbsProperty());
        containerVerbsPanel.verbsManagerProperty().bind(getControl().verbsManagerProperty());

        final Tab verbsTab = new Tab(tr(tr("Verbs")), containerVerbsPanel);

        verbsTab.setClosable(false);

        return verbsTab;
    }

    private Tab createContainerEngineToolsTab() {
        final ContainerEngineToolsPanel containerEngineToolsPanel = new ContainerEngineToolsPanel();

        containerEngineToolsPanel.containerProperty().bind(getControl().containerProperty());
        containerEngineToolsPanel.engineToolsProperty().bind(getControl().engineToolsProperty());
        containerEngineToolsPanel.engineToolsManagerProperty().bind(getControl().engineToolsManagerProperty());

        final Tab engineToolsTab = new Tab(tr("Engine tools"), containerEngineToolsPanel);

        engineToolsTab.setClosable(false);

        return engineToolsTab;
    }

    private Tab createContainerToolsTab() {
        final ContainerToolsPanel containerToolsPanel = new ContainerToolsPanel();

        containerToolsPanel.containerProperty().bind(getControl().containerProperty());
        containerToolsPanel.containerEngineControllerProperty().bind(getControl().containerEngineControllerProperty());

        final Tab containerToolsTab = new Tab(tr("Tools"), containerToolsPanel);

        containerToolsTab.setClosable(false);

        return containerToolsTab;
    }
}
