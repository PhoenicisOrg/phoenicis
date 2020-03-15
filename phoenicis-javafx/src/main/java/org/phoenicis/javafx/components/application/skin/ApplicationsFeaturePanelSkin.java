package org.phoenicis.javafx.components.application.skin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.application.actions.ApplicationInformation;
import org.phoenicis.javafx.components.application.control.ApplicationInformationPanel;
import org.phoenicis.javafx.components.application.control.ApplicationSidebar;
import org.phoenicis.javafx.components.application.control.ApplicationsFeaturePanel;
import org.phoenicis.javafx.components.common.actions.None;
import org.phoenicis.javafx.components.common.actions.OpenDetailsPanel;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.control.SidebarBase;
import org.phoenicis.javafx.components.common.skin.FeaturePanelSkin;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.utils.StringBindings;
import org.phoenicis.javafx.utils.SwitchBinding;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;

import java.util.Comparator;
import java.util.Optional;

/**
 * A skin implementation for the {@link ApplicationsFeaturePanel} component
 */
public class ApplicationsFeaturePanelSkin
        extends FeaturePanelSkin<ApplicationsFeaturePanel, ApplicationsFeaturePanelSkin> {
    /**
     * The currently selected list widget
     */
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ApplicationsFeaturePanelSkin(ApplicationsFeaturePanel control) {
        super(control);

        this.selectedListWidget = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<SidebarBase<?, ?, ?>> createSidebar() {
        /*
         * initialize the category lists by:
         * 1. filtering by installer categories
         * 2. sorting the remaining categories by their name
         */
        final SortedList<CategoryDTO> sortedCategories = getControl().getCategories()
                .filtered(category -> category.getType() == CategoryDTO.CategoryType.INSTALLERS)
                .sorted(Comparator.comparing(CategoryDTO::getName));

        final ApplicationSidebar sidebar = new ApplicationSidebar(sortedCategories, this.selectedListWidget);

        sidebar.operatingSystemProperty().bind(getControl().operatingSystemProperty());
        sidebar.fuzzySearchRatioProperty().bind(getControl().fuzzySearchRatioProperty());

        getControl().searchTermProperty().bind(sidebar.searchTermProperty());
        getControl().filterCategoryProperty().bind(sidebar.selectedItemProperty());
        getControl().containCommercialApplicationsProperty().bind(sidebar.containCommercialApplicationsProperty());
        getControl().containRequiresPatchApplicationsProperty()
                .bind(sidebar.containRequiresPatchApplicationsProperty());
        getControl().containTestingApplicationsProperty().bind(sidebar.containTestingApplicationsProperty());
        getControl().containAllOSCompatibleApplicationsProperty()
                .bind(sidebar.containAllOSCompatibleApplicationsProperty());

        // set the default selection
        sidebar.setSelectedListWidget(Optional
                .ofNullable(getControl().getJavaFxSettingsManager())
                .map(JavaFxSettingsManager::getAppsListType)
                .orElse(ListWidgetType.ICONS_LIST));

        // save changes to the list widget selection to the hard drive
        sidebar.selectedListWidgetProperty().addListener((observable, oldValue, newValue) -> {
            final JavaFxSettingsManager javaFxSettingsManager = getControl().getJavaFxSettingsManager();

            if (newValue != null) {
                javaFxSettingsManager.setAppsListType(newValue);
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
         * initialising the application lists by:
         * 1. sorting the applications by their name
         * 2. filtering them
         */
        final FilteredList<ApplicationDTO> filteredApplications = ConcatenatedList
                .create(new MappedList<>(getControl().getCategories()
                        .filtered(category -> category.getType() == CategoryDTO.CategoryType.INSTALLERS),
                        CategoryDTO::getApplications))
                .sorted(Comparator.comparing(ApplicationDTO::getName))
                .filtered(getControl()::filterApplication);

        filteredApplications.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl()::filterApplication,
                        getControl().searchTermProperty(),
                        getControl().fuzzySearchRatioProperty(),
                        getControl().operatingSystemProperty(),
                        getControl().filterCategoryProperty(),
                        getControl().containAllOSCompatibleApplicationsProperty(),
                        getControl().containCommercialApplicationsProperty(),
                        getControl().containRequiresPatchApplicationsProperty(),
                        getControl().containTestingApplicationsProperty()));

        final ObservableList<ListWidgetElement<ApplicationDTO>> listWidgetEntries = new MappedList<>(
                filteredApplications, ListWidgetElement::create);

        final CombinedListWidget<ApplicationDTO> combinedListWidget = new CombinedListWidget<>(listWidgetEntries,
                this.selectedListWidget);

        // bind direction: controller property -> skin property
        getControl().selectedApplicationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                combinedListWidget.select(newValue);
            } else {
                combinedListWidget.deselect();
            }
        });

        // bind direction: skin property -> controller properties
        combinedListWidget.selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final ApplicationDTO selectedItem = newValue.getItem();
                final MouseEvent event = newValue.getEvent();

                getControl().setSelectedApplication(selectedItem);
                getControl().setOpenedDetailsPanel(new ApplicationInformation(selectedItem));
            } else {
                getControl().setSelectedApplication(null);
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
                .withCase(ApplicationInformation.class, this::createApplicationInformationDetailsPanel)
                .withCase(None.class, action -> null)
                .build();
    }

    private DetailsPanel createApplicationInformationDetailsPanel(ApplicationInformation action) {
        final ApplicationInformationPanel applicationPanel = new ApplicationInformationPanel();

        applicationPanel.scriptInterpreterProperty().bind(getControl().scriptInterpreterProperty());
        applicationPanel.setApplication(action.getApplication());

        applicationPanel.operatingSystemProperty().bind(getControl().operatingSystemProperty());
        applicationPanel.containCommercialApplicationsProperty()
                .bind(getControl().containCommercialApplicationsProperty());
        applicationPanel.containRequiresPatchApplicationsProperty()
                .bind(getControl().containRequiresPatchApplicationsProperty());
        applicationPanel.containAllOSCompatibleApplicationsProperty()
                .bind(getControl().containAllOSCompatibleApplicationsProperty());
        applicationPanel.containTestingApplicationsProperty().bind(getControl().containTestingApplicationsProperty());

        // TODO: change to a binding
        applicationPanel.setShowScriptSource(getControl().getJavaFxSettingsManager().isViewScriptSource());

        // TODO: change to an `ObjectBindings.flatMap` call
        applicationPanel.webEngineStylesheetProperty()
                .bind(getControl().getThemeManager().webEngineStylesheetProperty());

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty().bind(
                StringBindings.map(getControl().selectedApplicationProperty(), ApplicationDTO::getName));
        detailsPanel.setContent(applicationPanel);
        detailsPanel.setOnClose(getControl()::closeDetailsPanel);

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return detailsPanel;
    }
}
