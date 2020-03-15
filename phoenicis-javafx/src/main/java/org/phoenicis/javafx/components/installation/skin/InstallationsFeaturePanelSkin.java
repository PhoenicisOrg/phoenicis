package org.phoenicis.javafx.components.installation.skin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
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
import org.phoenicis.javafx.components.installation.actions.Installation;
import org.phoenicis.javafx.components.installation.control.InstallationSidebar;
import org.phoenicis.javafx.components.installation.control.InstallationsFeaturePanel;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.utils.SwitchBinding;
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
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public InstallationsFeaturePanelSkin(InstallationsFeaturePanel control) {
        super(control);

        this.selectedListWidget = new SimpleObjectProperty<>();
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

        final CombinedListWidget<InstallationDTO> combinedListWidget = new CombinedListWidget<>(listWidgetEntries,
                this.selectedListWidget);

        // bind direction: controller property -> skin property
        getControl().selectedInstallationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                combinedListWidget.select(newValue);
            } else {
                combinedListWidget.deselect();
            }
        });

        // bind direction: skin property -> controller properties
        combinedListWidget.selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final InstallationDTO selectedItem = newValue.getItem();

                getControl().setSelectedInstallation(selectedItem);
                getControl().setOpenedDetailsPanel(new Installation(selectedItem));
            } else {
                getControl().setSelectedInstallation(null);
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
                .withCase(Installation.class, this::createInstallationDetailsPanel)
                .withCase(None.class, action -> null)
                .build();
    }

    private DetailsPanel createInstallationDetailsPanel(Installation action) {
        final InstallationDTO installation = action.getInstallation();

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.setTitle(installation.getName());
        detailsPanel.setContent(installation.getNode());

        detailsPanel.setOnClose(getControl()::closeDetailsPanel);

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return detailsPanel;
    }
}
