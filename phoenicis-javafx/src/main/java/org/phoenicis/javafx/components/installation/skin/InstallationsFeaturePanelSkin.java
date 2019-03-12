package org.phoenicis.javafx.components.installation.skin;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.control.SidebarBase;
import org.phoenicis.javafx.components.common.skin.FeaturePanelSkin;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.installation.control.InstallationSidebar;
import org.phoenicis.javafx.components.installation.control.InstallationsFeaturePanel;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.utils.ObjectBindings;
import org.phoenicis.javafx.utils.StringBindings;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;

import java.util.Comparator;
import java.util.Optional;

/**
 * A skin implementation for the {@link InstallationsFeaturePanel} component
 */
public class InstallationsFeaturePanelSkin
        extends FeaturePanelSkin<InstallationsFeaturePanel, InstallationsFeaturePanelSkin> {
    /**
     * The currently selected list widget
     */
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    /**
     * The currently selected element inside the list widget
     */
    private final ObjectProperty<ListWidgetSelection<InstallationDTO>> selectedListWidgetElement;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public InstallationsFeaturePanelSkin(InstallationsFeaturePanel control) {
        super(control);

        this.selectedListWidget = new SimpleObjectProperty<>();
        this.selectedListWidgetElement = createSelectedListWidgetElement();
    }

    private ObjectProperty<ListWidgetSelection<InstallationDTO>> createSelectedListWidgetElement() {
        final ObjectProperty<ListWidgetSelection<InstallationDTO>> selectedListWidgetElement = new SimpleObjectProperty<>();

        // this can't be replaced by a binding because the installation can be changed otherwise
        selectedListWidgetElement.addListener((Observable invalidation) -> {
            final ListWidgetSelection<InstallationDTO> selection = selectedListWidgetElement.getValue();

            if (selection != null) {
                getControl().setSelectedInstallation(selection.getItem());
            } else {
                getControl().setSelectedInstallation(null);
            }
        });

        return selectedListWidgetElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<SidebarBase<?, ?, ?>> createSidebar() {
        final SortedList<InstallationCategoryDTO> sortedCategories = getControl().getInstallationCategories()
                .sorted(Comparator.comparing(InstallationCategoryDTO::getName));

        final InstallationSidebar sidebar = new InstallationSidebar(getControl().getFilter(), sortedCategories,
                this.selectedListWidget);

        sidebar.javaFxSettingsManagerProperty().bind(getControl().javaFxSettingsManagerProperty());

        // set the default selection
        sidebar.setSelectedListWidget(Optional
                .ofNullable(getControl().getJavaFxSettingsManager())
                .map(JavaFxSettingsManager::getInstallationsListType)
                .orElse(ListWidgetType.ICONS_LIST));

        // save changes to the list widget selection to the hard drive
        sidebar.selectedListWidgetProperty().addListener((observable, oldValue, newValue) -> {
            final JavaFxSettingsManager javaFxSettingsManager = getControl().getJavaFxSettingsManager();

            if (newValue != null) {
                javaFxSettingsManager.setInstallationsListType(newValue);
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
        final FilteredList<InstallationDTO> filteredInstallations = ConcatenatedList
                .create(new MappedList<>(getControl().getInstallationCategories()
                        .sorted(Comparator.comparing(InstallationCategoryDTO::getName)),
                        InstallationCategoryDTO::getInstallations))
                .filtered(getControl().getFilter()::filter);

        filteredInstallations.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl().getFilter()::filter,
                        getControl().getFilter().searchTermProperty(),
                        getControl().getFilter().selectedInstallationCategoryProperty()));

        final SortedList<InstallationDTO> sortedInstallations = filteredInstallations
                .sorted(Comparator.comparing(InstallationDTO::getName));

        final ObservableList<ListWidgetElement<InstallationDTO>> listWidgetEntries = new MappedList<>(
                sortedInstallations,
                ListWidgetElement::create);

        final CombinedListWidget<InstallationDTO> combinedListWidget = new CombinedListWidget<>(listWidgetEntries);

        combinedListWidget.selectedElementProperty().bindBidirectional(this.selectedListWidgetElement);
        combinedListWidget.selectedListWidgetProperty().bind(this.selectedListWidget);

        /*
         * whenever a selected installation is set inside the control,
         * automatically select in in the combined list widget
         */
        getControl().selectedInstallationProperty().addListener((Observable invalidation) -> {
            final InstallationDTO installation = getControl().getSelectedInstallation();

            if (installation != null) {
                combinedListWidget.select(getControl().getSelectedInstallation());
            } else {
                combinedListWidget.setSelectedElement(null);
            }
        });

        return new SimpleObjectProperty<>(combinedListWidget);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<DetailsPanel> createDetailsPanel() {
        final ObjectBinding<InstallationDTO> installation = ObjectBindings
                .map(this.selectedListWidgetElement, ListWidgetSelection::getItem);

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty().bind(StringBindings.map(installation, InstallationDTO::getName));
        detailsPanel.contentProperty().bind(ObjectBindings.map(installation, InstallationDTO::getNode));

        detailsPanel.setOnClose(() -> this.selectedListWidgetElement.setValue(null));

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return Bindings.when(Bindings.isNotNull(installation))
                .then(new SimpleObjectProperty<>(detailsPanel))
                .otherwise(new SimpleObjectProperty<>());
    }
}
