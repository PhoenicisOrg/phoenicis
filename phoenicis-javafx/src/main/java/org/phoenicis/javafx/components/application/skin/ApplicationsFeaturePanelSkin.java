package org.phoenicis.javafx.components.application.skin;

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
import java.util.Optional;

/**
 * A skin implementation for the {@link ApplicationsFeaturePanel} component
 */
public class ApplicationsFeaturePanelSkin
        extends FeaturePanelSkin<ApplicationsFeaturePanel, ApplicationsFeaturePanelSkin> {
    /**
     * The current list widget selection containing the selected {@link ApplicationDTO} object
     */
    private final ObjectProperty<ListWidgetSelection<ApplicationDTO>> listWidgetSelection;

    /**
     * The currently selected application
     */
    private final ObjectProperty<ApplicationDTO> selectedApplication;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ApplicationsFeaturePanelSkin(ApplicationsFeaturePanel control) {
        super(control);

        this.listWidgetSelection = new SimpleObjectProperty<>();
        // TODO: replace with ObjectBinding<ApplicationDTO>
        this.selectedApplication = new SimpleObjectProperty<>();
        this.selectedApplication.bind(ObjectBindings.map(this.listWidgetSelection, ListWidgetSelection::getItem));
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

        final ApplicationSidebar sidebar = new ApplicationSidebar(getControl().getFilter(), sortedCategories,
                getControl().selectedListWidgetProperty());

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
                .filtered(getControl().getFilter()::filter);

        filteredApplications.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl().getFilter()::filter,
                        getControl().getFilter().filterTextProperty(),
                        getControl().getFilter().filterCategoryProperty(),
                        getControl().getFilter().containAllOSCompatibleApplicationsProperty(),
                        getControl().getFilter().containCommercialApplicationsProperty(),
                        getControl().getFilter().containRequiresPatchApplicationsProperty(),
                        getControl().getFilter().containTestingApplicationsProperty()));

        final ObservableList<ListWidgetElement<ApplicationDTO>> listWidgetEntries = new MappedList<>(
                filteredApplications, ListWidgetElement::create);

        final CombinedListWidget<ApplicationDTO> listWidget = new CombinedListWidget<>(listWidgetEntries,
                getControl().selectedListWidgetProperty());

        listWidget.selectedElementProperty().bindBidirectional(this.listWidgetSelection);

        return new SimpleObjectProperty<>(listWidget);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<DetailsPanel> createDetailsPanel() {
        final ApplicationInformationPanel applicationPanel = new ApplicationInformationPanel(
                getControl().getScriptInterpreter(), getControl().getFilter(), this.selectedApplication);

        // TODO: change to a binding
        applicationPanel.setShowScriptSource(getControl().getJavaFxSettingsManager().isViewScriptSource());

        // TODO: change to an `ObjectBindings.flatMap` call
        applicationPanel.webEngineStylesheetProperty()
                .bind(getControl().getThemeManager().webEngineStylesheetProperty());

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty().bind(StringBindings.map(this.selectedApplication, ApplicationDTO::getName));
        detailsPanel.setContent(applicationPanel);
        detailsPanel.setOnClose(() -> this.listWidgetSelection.setValue(null));

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return Bindings.when(Bindings.isNotNull(this.selectedApplication))
                .then(detailsPanel).otherwise(new SimpleObjectProperty<>());
    }
}
