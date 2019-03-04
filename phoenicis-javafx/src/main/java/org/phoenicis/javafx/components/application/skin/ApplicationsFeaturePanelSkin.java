package org.phoenicis.javafx.components.application.skin;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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
import org.phoenicis.javafx.components.application.control.ApplicationInformationPanel;
import org.phoenicis.javafx.components.application.control.ApplicationSidebar;
import org.phoenicis.javafx.components.application.control.ApplicationsFeaturePanel;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.control.SidebarBase;
import org.phoenicis.javafx.components.common.skin.FeaturePanelSkin;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.utils.ObjectBindings;
import org.phoenicis.javafx.utils.StringBindings;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;

import java.util.Comparator;

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

        this.selectedListWidget = createSelectedListWidget();
    }

    private ObjectProperty<ListWidgetType> createSelectedListWidget() {
        final ObjectProperty<ListWidgetType> selectedListWidget = new SimpleObjectProperty<>();

        final InvalidationListener listener = (Observable invalidation) -> {
            final JavaFxSettingsManager javaFxSettingsManager = getControl().getJavaFxSettingsManager();

            if (javaFxSettingsManager != null) {
                selectedListWidget.setValue(javaFxSettingsManager.getAppsListType());
            } else {
                selectedListWidget.set(ListWidgetType.ICONS_LIST);
            }
        };

        // set the default selection
        getControl().javaFxSettingsManagerProperty().addListener(listener);

        listener.invalidated(getControl().javaFxSettingsManagerProperty());

        // save changes to the list widget selection to the hard drive
        selectedListWidget.addListener((observable, oldValue, newValue) -> {
            final JavaFxSettingsManager javaFxSettingsManager = getControl().getJavaFxSettingsManager();

            if (newValue != null) {
                javaFxSettingsManager.setAppsListType(newValue);
                javaFxSettingsManager.save();
            }
        });

        return selectedListWidget;
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

        final ApplicationSidebar sidebar = new ApplicationSidebar(sortedCategories);

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

        sidebar.selectedListWidgetProperty().bindBidirectional(this.selectedListWidget);

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

        final CombinedListWidget<ApplicationDTO> listWidget = new CombinedListWidget<>(listWidgetEntries,
                this.selectedListWidget);

        getControl().selectedApplicationProperty().bind(
                ObjectBindings.map(listWidget.selectedElementProperty(), ListWidgetSelection::getItem));

        return new SimpleObjectProperty<>(listWidget);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<DetailsPanel> createDetailsPanel() {
        final ApplicationInformationPanel applicationPanel = new ApplicationInformationPanel();

        applicationPanel.scriptInterpreterProperty().bind(getControl().scriptInterpreterProperty());
        applicationPanel.applicationProperty().bind(getControl().selectedApplicationProperty());

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

        detailsPanel.titleProperty()
                .bind(StringBindings.map(getControl().selectedApplicationProperty(), ApplicationDTO::getName));
        detailsPanel.setContent(applicationPanel);
        detailsPanel.setOnClose(() -> getControl().setSelectedApplication(null));

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return Bindings.when(Bindings.isNotNull(getControl().selectedApplicationProperty()))
                .then(detailsPanel).otherwise(new SimpleObjectProperty<>());
    }
}
