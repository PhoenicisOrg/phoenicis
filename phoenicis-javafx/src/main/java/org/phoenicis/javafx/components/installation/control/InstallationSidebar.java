package org.phoenicis.javafx.components.installation.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.control.ExtendedSidebarBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.installation.skin.InstallationSidebarSkin;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.mainwindow.installations.InstallationsFilter;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;

/**
 * A sidebar implementation for the installations tab
 */
public class InstallationSidebar
        extends ExtendedSidebarBase<InstallationCategoryDTO, InstallationSidebar, InstallationSidebarSkin> {
    /**
     * The selected installation category
     */
    private ObjectProperty<InstallationCategoryDTO> selectedInstallationCategory;

    /**
     * The installations filter
     */
    private final ObjectProperty<InstallationsFilter> filter;

    /**
     * Constructor
     *
     * @param filter The installations filter
     * @param items The items shown inside a toggle button group in the sidebar
     * @param selectedListWidget The currently selected {@link ListWidgetType} by the user
     */
    public InstallationSidebar(InstallationsFilter filter, ObservableList<InstallationCategoryDTO> items,
            ObjectProperty<ListWidgetType> selectedListWidget) {
        super(items, filter.searchTermProperty(), selectedListWidget);

        this.filter = new SimpleObjectProperty<>(filter);

        this.selectedInstallationCategory = filter.selectedInstallationCategoryProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstallationSidebarSkin createSkin() {
        return new InstallationSidebarSkin(this);
    }

    public InstallationCategoryDTO getSelectedInstallationCategory() {
        return this.selectedInstallationCategory.get();
    }

    public ObjectProperty<InstallationCategoryDTO> selectedInstallationCategoryProperty() {
        return this.selectedInstallationCategory;
    }

    public void setSelectedInstallationCategory(InstallationCategoryDTO selectedInstallationCategory) {
        this.selectedInstallationCategory.set(selectedInstallationCategory);
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
}
