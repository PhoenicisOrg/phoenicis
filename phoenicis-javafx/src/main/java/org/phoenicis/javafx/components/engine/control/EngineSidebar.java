package org.phoenicis.javafx.components.engine.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.components.common.control.ExtendedSidebarBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.engine.skin.EngineSidebarSkin;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesFilter;

/**
 * A sidebar implementation for the engines tab
 */
public class EngineSidebar extends ExtendedSidebarBase<EngineCategoryDTO, EngineSidebar, EngineSidebarSkin> {
    /**
     * The JavaFX settings manager
     */
    private final ObjectProperty<JavaFxSettingsManager> javaFxSettingsManager;

    /**
     * The selected engine category
     */
    private final ObjectProperty<EngineCategoryDTO> selectedEngineCategory;

    /**
     * Are installed engines searched
     */
    private final BooleanProperty showInstalled;

    /**
     * Are not installed engines searched
     */
    private final BooleanProperty showNotInstalled;

    /**
     * The engines filter
     */
    private final ObjectProperty<EnginesFilter> filter;

    /**
     * Constructor
     *
     * @param filter The engines filter
     * @param items The items shown inside a toggle button group in the sidebar
     * @param selectedListWidget The currently selected {@link ListWidgetType} by the user
     * @param javaFxSettingsManager The JavaFX settings manager
     */
    private EngineSidebar(EnginesFilter filter, ObservableList<EngineCategoryDTO> items,
            ObjectProperty<ListWidgetType> selectedListWidget,
            ObjectProperty<JavaFxSettingsManager> javaFxSettingsManager) {
        super(items, filter.searchTermProperty(), selectedListWidget);

        this.javaFxSettingsManager = javaFxSettingsManager;
        this.filter = new SimpleObjectProperty<>(filter);

        this.selectedEngineCategory = filter.selectedEngineCategoryProperty();
        this.showInstalled = filter.showInstalledProperty();
        this.showNotInstalled = filter.showNotInstalledProperty();
    }

    /**
     * Constructor
     *
     * @param filter The engines filter
     * @param items The items shown inside a toggle button group in the sidebar
     * @param selectedListWidget The currently selected {@link ListWidgetType} by the user
     */
    public EngineSidebar(EnginesFilter filter, ObservableList<EngineCategoryDTO> items,
            ObjectProperty<ListWidgetType> selectedListWidget) {
        this(filter, items, selectedListWidget, new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineSidebarSkin createSkin() {
        return new EngineSidebarSkin(this);
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

    public EngineCategoryDTO getSelectedEngineCategory() {
        return this.selectedEngineCategory.get();
    }

    public ObjectProperty<EngineCategoryDTO> selectedEngineCategoryProperty() {
        return this.selectedEngineCategory;
    }

    public void setSelectedEngineCategory(EngineCategoryDTO selectedEngineCategory) {
        this.selectedEngineCategory.set(selectedEngineCategory);
    }

    public boolean isShowInstalled() {
        return this.showInstalled.get();
    }

    public BooleanProperty showInstalledProperty() {
        return this.showInstalled;
    }

    public void setShowInstalled(boolean showInstalled) {
        this.showInstalled.set(showInstalled);
    }

    public boolean isShowNotInstalled() {
        return this.showNotInstalled.get();
    }

    public BooleanProperty showNotInstalledProperty() {
        return this.showNotInstalled;
    }

    public void setShowNotInstalled(boolean showNotInstalled) {
        this.showNotInstalled.set(showNotInstalled);
    }

    public EnginesFilter getFilter() {
        return this.filter.get();
    }

    public ObjectProperty<EnginesFilter> filterProperty() {
        return this.filter;
    }

    public void setFilter(EnginesFilter filter) {
        this.filter.set(filter);
    }
}
