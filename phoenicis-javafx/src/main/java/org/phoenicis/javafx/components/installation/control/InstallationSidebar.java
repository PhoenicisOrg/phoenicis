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
     * The installations filterCategory
     */
    private final ObjectProperty<InstallationsFilter> filter;

    /**
     * The JavaFX settings manager
     */
    private final ObjectProperty<JavaFxSettingsManager> javaFxSettingsManager;

    /**
     * Constructor
     *
     * @param filter The installations filterCategory
     * @param items The items shown inside a toggle button group in the sidebar
     * @param selectedListWidget The currently selected {@link ListWidgetType} by the user
     * @param javaFxSettingsManager The JavaFX settings manager
     */
    private InstallationSidebar(InstallationsFilter filter, ObservableList<InstallationCategoryDTO> items,
            ObjectProperty<ListWidgetType> selectedListWidget,
            ObjectProperty<JavaFxSettingsManager> javaFxSettingsManager) {
        super(items, filter.searchTermProperty(), selectedListWidget);

        this.javaFxSettingsManager = javaFxSettingsManager;
        this.filter = new SimpleObjectProperty<>(filter);

        this.selectedInstallationCategory = filter.selectedInstallationCategoryProperty();
    }

    /**
     * Constructor
     *
     * @param filter The installations filterCategory
     * @param items The items shown inside a toggle button group in the sidebar
     * @param selectedListWidget The currently selected {@link ListWidgetType} by the user
     */
    public InstallationSidebar(InstallationsFilter filter, ObservableList<InstallationCategoryDTO> items,
            ObjectProperty<ListWidgetType> selectedListWidget) {
        this(filter, items, selectedListWidget, new SimpleObjectProperty<>());
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

    public JavaFxSettingsManager getJavaFxSettingsManager() {
        return this.javaFxSettingsManager.get();
    }

    public ObjectProperty<JavaFxSettingsManager> javaFxSettingsManagerProperty() {
        return this.javaFxSettingsManager;
    }

    public void setJavaFxSettingsManager(JavaFxSettingsManager javaFxSettingsManager) {
        this.javaFxSettingsManager.set(javaFxSettingsManager);
    }
}
