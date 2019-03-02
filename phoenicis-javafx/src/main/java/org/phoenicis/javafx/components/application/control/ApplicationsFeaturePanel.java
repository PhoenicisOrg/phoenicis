package org.phoenicis.javafx.components.application.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.application.skin.ApplicationsFeaturePanelSkin;
import org.phoenicis.javafx.components.common.control.FeaturePanel;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationFilter;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

/**
 * The component shown inside the Phoenicis "Applications" tab
 */
public class ApplicationsFeaturePanel extends FeaturePanel<ApplicationsFeaturePanel, ApplicationsFeaturePanelSkin> {
    /**
     * The theme manager
     */
    private final ObjectProperty<ThemeManager> themeManager;

    /**
     * The applications filter
     */
    private final ObjectProperty<ApplicationFilter> filter;

    /**
     * The JavaFX settings manager
     */
    private final ObjectProperty<JavaFxSettingsManager> javaFxSettingsManager;

    /**
     * The currently selected list widget
     */
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    /**
     * The shown application categories
     */
    private final ObservableList<CategoryDTO> categories;

    /**
     * The script interpreter
     */
    private final ObjectProperty<ScriptInterpreter> scriptInterpreter;

    /**
     * The currently selected application
     */
    private final ObjectProperty<ApplicationDTO> selectedApplication;

    /**
     * Constructor
     */
    public ApplicationsFeaturePanel() {
        super();

        this.themeManager = new SimpleObjectProperty<>();
        this.filter = new SimpleObjectProperty<>();
        this.javaFxSettingsManager = new SimpleObjectProperty<>();
        this.selectedListWidget = new SimpleObjectProperty<>();
        this.categories = FXCollections.observableArrayList();
        this.scriptInterpreter = new SimpleObjectProperty<>();
        this.selectedApplication = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationsFeaturePanelSkin createSkin() {
        return new ApplicationsFeaturePanelSkin(this);
    }

    public ThemeManager getThemeManager() {
        return this.themeManager.get();
    }

    public ObjectProperty<ThemeManager> themeManagerProperty() {
        return this.themeManager;
    }

    public void setThemeManager(ThemeManager themeManager) {
        this.themeManager.set(themeManager);
    }

    public ApplicationFilter getFilter() {
        return this.filter.get();
    }

    public ObjectProperty<ApplicationFilter> filterProperty() {
        return this.filter;
    }

    public void setFilter(ApplicationFilter filter) {
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

    public ListWidgetType getSelectedListWidget() {
        return this.selectedListWidget.get();
    }

    public ObjectProperty<ListWidgetType> selectedListWidgetProperty() {
        return this.selectedListWidget;
    }

    public void setSelectedListWidget(ListWidgetType selectedListWidget) {
        this.selectedListWidget.set(selectedListWidget);
    }

    public ObservableList<CategoryDTO> getCategories() {
        return this.categories;
    }

    public ScriptInterpreter getScriptInterpreter() {
        return this.scriptInterpreter.get();
    }

    public ObjectProperty<ScriptInterpreter> scriptInterpreterProperty() {
        return this.scriptInterpreter;
    }

    public void setScriptInterpreter(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter.set(scriptInterpreter);
    }

    public ApplicationDTO getSelectedApplication() {
        return this.selectedApplication.get();
    }

    public ObjectProperty<ApplicationDTO> selectedApplicationProperty() {
        return this.selectedApplication;
    }

    public void setSelectedApplication(ApplicationDTO selectedApplication) {
        this.selectedApplication.set(selectedApplication);
    }
}
