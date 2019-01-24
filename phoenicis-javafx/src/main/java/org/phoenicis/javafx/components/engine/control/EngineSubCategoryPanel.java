package org.phoenicis.javafx.components.engine.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.phoenicis.engines.Engine;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.engine.skin.EngineSubCategoryPanelSkin;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesFilter;

import java.util.function.BiConsumer;

/**
 * A panel component showing the {@link EngineVersionDTO} objects contained in a {@link EngineSubCategoryDTO} object
 */
public class EngineSubCategoryPanel extends ControlBase<EngineSubCategoryPanel, EngineSubCategoryPanelSkin> {
    /**
     * The engine category
     */
    private final ObjectProperty<EngineCategoryDTO> engineCategory;

    /**
     * The engine subcategory
     */
    private final ObjectProperty<EngineSubCategoryDTO> engineSubCategory;

    /**
     * The path leading to where the engines are saved
     */
    private final StringProperty enginesPath;

    /**
     * The engine
     */
    private final ObjectProperty<Engine> engine;

    /**
     * The applied filter
     */
    private final ObjectProperty<EnginesFilter> filter;

    /**
     * The selected list widget
     */
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    /**
     * The callback for when an engine has been selected
     */
    private final ObjectProperty<BiConsumer<EngineDTO, Engine>> onEngineSelect;

    /**
     * Constructor
     *
     * @param engineCategory The engine category
     * @param engineSubCategory The engine subcategory
     * @param enginesPath The path leading to where the engines are saved
     * @param engine The engine
     * @param filter The applied filter
     * @param selectedListWidget The selected list widget
     * @param onEngineSelect The callback for when an engine has been selected
     */
    public EngineSubCategoryPanel(ObjectProperty<EngineCategoryDTO> engineCategory,
            ObjectProperty<EngineSubCategoryDTO> engineSubCategory, StringProperty enginesPath,
            ObjectProperty<Engine> engine, ObjectProperty<EnginesFilter> filter,
            ObjectProperty<ListWidgetType> selectedListWidget,
            ObjectProperty<BiConsumer<EngineDTO, Engine>> onEngineSelect) {
        super();

        this.engineCategory = engineCategory;
        this.engineSubCategory = engineSubCategory;
        this.enginesPath = enginesPath;
        this.engine = engine;
        this.filter = filter;
        this.selectedListWidget = selectedListWidget;
        this.onEngineSelect = onEngineSelect;
    }

    /**
     * Constructor
     */
    public EngineSubCategoryPanel() {
        this(new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleStringProperty(),
                new SimpleObjectProperty<>(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>(),
                new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineSubCategoryPanelSkin createSkin() {
        return new EngineSubCategoryPanelSkin(this);
    }

    /**
     * Checks whether this panel will be empty when applying the given {@link EnginesFilter filter}
     *
     * @param filter The applied filter
     * @return True if this panel will be empty when applying the given filter, false otherwise
     */
    public boolean notEmpty(EnginesFilter filter) {
        return getEngineSubCategory().getPackages().stream()
                .anyMatch(filter.createFilter(getEngineCategory(), getEngineSubCategory()));
    }

    public EngineCategoryDTO getEngineCategory() {
        return engineCategory.get();
    }

    public ObjectProperty<EngineCategoryDTO> engineCategoryProperty() {
        return engineCategory;
    }

    public void setEngineCategory(EngineCategoryDTO engineCategory) {
        this.engineCategory.set(engineCategory);
    }

    public EngineSubCategoryDTO getEngineSubCategory() {
        return engineSubCategory.get();
    }

    public ObjectProperty<EngineSubCategoryDTO> engineSubCategoryProperty() {
        return engineSubCategory;
    }

    public void setEngineSubCategory(EngineSubCategoryDTO engineSubCategory) {
        this.engineSubCategory.set(engineSubCategory);
    }

    public String getEnginesPath() {
        return enginesPath.get();
    }

    public StringProperty enginesPathProperty() {
        return enginesPath;
    }

    public void setEnginesPath(String enginesPath) {
        this.enginesPath.set(enginesPath);
    }

    public Engine getEngine() {
        return engine.get();
    }

    public ObjectProperty<Engine> engineProperty() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine.set(engine);
    }

    public EnginesFilter getFilter() {
        return filter.get();
    }

    public ObjectProperty<EnginesFilter> filterProperty() {
        return filter;
    }

    public void setFilter(EnginesFilter filter) {
        this.filter.set(filter);
    }

    public ListWidgetType getSelectedListWidget() {
        return selectedListWidget.get();
    }

    public ObjectProperty<ListWidgetType> selectedListWidgetProperty() {
        return selectedListWidget;
    }

    public void setSelectedListWidget(ListWidgetType selectedListWidget) {
        this.selectedListWidget.set(selectedListWidget);
    }

    public BiConsumer<EngineDTO, Engine> getOnEngineSelect() {
        return onEngineSelect.get();
    }

    public ObjectProperty<BiConsumer<EngineDTO, Engine>> onEngineSelectProperty() {
        return onEngineSelect;
    }

    public void setOnEngineSelect(BiConsumer<EngineDTO, Engine> onEngineSelect) {
        this.onEngineSelect.set(onEngineSelect);
    }
}
