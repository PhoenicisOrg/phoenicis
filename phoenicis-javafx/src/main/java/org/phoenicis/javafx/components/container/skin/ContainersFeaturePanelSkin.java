package org.phoenicis.javafx.components.container.skin;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.actions.None;
import org.phoenicis.javafx.components.common.actions.OpenDetailsPanel;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.control.SidebarBase;
import org.phoenicis.javafx.components.common.skin.FeaturePanelSkin;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.container.actions.ContainerInformation;
import org.phoenicis.javafx.components.container.control.ContainerInformationPanel;
import org.phoenicis.javafx.components.container.control.ContainerSidebar;
import org.phoenicis.javafx.components.container.control.ContainersFeaturePanel;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.utils.SwitchBinding;
import org.phoenicis.repository.dto.ApplicationDTO;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * A skin implementation for the {@link ContainersFeaturePanel} component
 */
public class ContainersFeaturePanelSkin extends FeaturePanelSkin<ContainersFeaturePanel, ContainersFeaturePanelSkin> {
    /**
     * The currently selected list widget
     */
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ContainersFeaturePanelSkin(ContainersFeaturePanel control) {
        super(control);

        this.selectedListWidget = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<SidebarBase<?, ?, ?>> createSidebar() {
        /*
         * initialize the container categories by sorting them
         */
        final SortedList<ContainerCategoryDTO> sortedCategories = getControl().getCategories()
                .sorted(Comparator.comparing(ContainerCategoryDTO::getName));

        final ContainerSidebar sidebar = new ContainerSidebar(getControl().getFilter(), sortedCategories,
                this.selectedListWidget);

        // set the default selection
        sidebar.setSelectedListWidget(Optional
                .ofNullable(getControl().getJavaFxSettingsManager())
                .map(JavaFxSettingsManager::getContainersListType)
                .orElse(ListWidgetType.ICONS_LIST));

        // save changes to the list widget selection to the hard drive
        sidebar.selectedListWidgetProperty().addListener((observable, oldValue, newValue) -> {
            final JavaFxSettingsManager javaFxSettingsManager = getControl().getJavaFxSettingsManager();

            if (newValue != null) {
                javaFxSettingsManager.setContainersListType(newValue);
                javaFxSettingsManager.save();
            }
        });

        return new SimpleObjectProperty<>(sidebar);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<Node> createContent() {
        /*
         * initialize the container lists by:
         * 1. sorting the containers by their name
         * 2. filtering the containers
         */
        final FilteredList<ContainerDTO> filteredContainers = ConcatenatedList
                .create(new MappedList<>(getControl().getCategories()
                        .sorted(Comparator.comparing(ContainerCategoryDTO::getName)),
                        ContainerCategoryDTO::getContainers))
                .sorted(Comparator.comparing(ContainerDTO::getName))
                .filtered(getControl().getFilter()::filter);

        filteredContainers.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl().getFilter()::filter,
                        getControl().getFilter().searchTermProperty()));

        final ObservableList<ListWidgetElement<ContainerDTO>> listWidgetEntries = new MappedList<>(filteredContainers,
                ListWidgetElement::create);

        final CombinedListWidget<ContainerDTO> combinedListWidget = new CombinedListWidget<>(listWidgetEntries,
                this.selectedListWidget);

        // bind direction: controller property -> skin property
        getControl().selectedContainerProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                combinedListWidget.select(newValue);
            } else {
                combinedListWidget.deselect();
            }
        });

        // bind direction: skin property -> controller properties
        combinedListWidget.selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final ContainerDTO selectedItem = newValue.getItem();

                getControl().setSelectedContainer(selectedItem);
                getControl().setOpenedDetailsPanel(new ContainerInformation(selectedItem));
            } else {
                getControl().setSelectedContainer(null);
                getControl().setOpenedDetailsPanel(new None());
            }
        });

        return new SimpleObjectProperty<>(combinedListWidget);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<DetailsPanel> createDetailsPanel() {
        return SwitchBinding
                .<OpenDetailsPanel, DetailsPanel> builder(getControl().openedDetailsPanelProperty())
                .withCase(ContainerInformation.class, this::createContainerInformationDetailsPanel)
                .withCase(None.class, action -> null)
                .build();
    }

    private DetailsPanel createContainerInformationDetailsPanel(ContainerInformation action) {
        final ContainerDTO container = action.getContainer();

        final ContainerInformationPanel containerInformationPanel = new ContainerInformationPanel();

        containerInformationPanel.setContainer(container);

        containerInformationPanel.enginesManagerProperty().bind(getControl().enginesManagerProperty());
        containerInformationPanel.verbsManagerProperty().bind(getControl().verbsManagerProperty());
        containerInformationPanel.engineToolsManagerProperty().bind(getControl().engineToolsManagerProperty());

        containerInformationPanel.setOnDeleteContainer(getControl()::deleteContainer);
        containerInformationPanel.setOnChangeEngineVersion(getControl()::changeEngineVersion);
        containerInformationPanel.setOnOpenFileBrowser(getControl()::openFileBrowser);

        getControl().getEngineSettings()
                .addListener((Observable invalidation) -> updateEngineSettings(containerInformationPanel));
        getControl().getVerbs().addListener((Observable invalidation) -> updateVerbs(containerInformationPanel));
        getControl().getEngineTools()
                .addListener((Observable invalidation) -> updateEngineTools(containerInformationPanel));

        updateEngineSettings(containerInformationPanel);
        updateVerbs(containerInformationPanel);
        updateEngineTools(containerInformationPanel);

        final DetailsPanel containerDetailsPanel = new DetailsPanel();

        containerDetailsPanel.setTitle(container.getName());
        containerDetailsPanel.setContent(containerInformationPanel);
        containerDetailsPanel.setOnClose(getControl()::closeDetailsPanel);

        containerDetailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return containerDetailsPanel;
    }

    /**
     * Applies the engine settings belonging to the currently selected container to the given
     * {@link ContainerInformationPanel}
     *
     * @param containerInformationPanel The information panel showing the details for the currently selected container
     */
    private void updateEngineSettings(final ContainerInformationPanel containerInformationPanel) {
        final ObservableMap<String, List<EngineSetting>> engineSettings = getControl().getEngineSettings();
        final ContainerDTO container = containerInformationPanel.getContainer();

        if (container != null && engineSettings.containsKey(container.getEngine().toLowerCase())) {
            containerInformationPanel.getEngineSettings()
                    .setAll(engineSettings.get(container.getEngine().toLowerCase()));
        } else {
            containerInformationPanel.getEngineSettings().clear();
        }
    }

    /**
     * Applies the verbs belonging to the currently selected container to the given {@link ContainerInformationPanel}
     *
     * @param containerInformationPanel The information panel showing the details for the currently selected container
     */
    private void updateVerbs(final ContainerInformationPanel containerInformationPanel) {
        final ObservableMap<String, ApplicationDTO> verbs = getControl().getVerbs();
        final ContainerDTO container = containerInformationPanel.getContainer();

        if (container != null && verbs.containsKey(container.getEngine().toLowerCase())) {
            containerInformationPanel.setVerbs(verbs.get(container.getEngine().toLowerCase()));
        } else {
            containerInformationPanel.setVerbs(null);
        }
    }

    /**
     * Applies the engine tools belonging to the currently selected container to the given
     * {@link ContainerInformationPanel}
     *
     * @param containerInformationPanel The information panel showing the details for the currently selected container
     */
    private void updateEngineTools(final ContainerInformationPanel containerInformationPanel) {
        final ObservableMap<String, ApplicationDTO> engineTools = getControl().getEngineTools();
        final ContainerDTO container = containerInformationPanel.getContainer();

        if (container != null && engineTools.containsKey(container.getEngine().toLowerCase())) {
            containerInformationPanel.setEngineTools(engineTools.get(container.getEngine().toLowerCase()));
        } else {
            containerInformationPanel.setEngineTools(null);
        }
    }
}
