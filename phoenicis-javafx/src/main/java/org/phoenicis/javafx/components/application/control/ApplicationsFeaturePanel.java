package org.phoenicis.javafx.components.application.control;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.javafx.components.application.skin.ApplicationsFeaturePanelSkin;
import org.phoenicis.javafx.components.application.utils.ApplicationFilter;
import org.phoenicis.javafx.components.common.control.FeaturePanel;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

/**
 * The component shown inside the Phoenicis "Applications" tab
 */
public class ApplicationsFeaturePanel extends FeaturePanel<ApplicationsFeaturePanel, ApplicationsFeaturePanelSkin>
        implements ApplicationFilter {
    /**
     * The theme manager
     */
    private final ObjectProperty<ThemeManager> themeManager;

    /**
     * The JavaFX settings manager
     */
    private final ObjectProperty<JavaFxSettingsManager> javaFxSettingsManager;

    /**
     * The entered search term
     */
    private final StringProperty searchTerm;

    /**
     * The selected {@link CategoryDTO} by the user
     */
    private final ObjectProperty<CategoryDTO> filterCategory;

    /**
     * The fuzzy search ratio
     */
    private final DoubleProperty fuzzySearchRatio;

    /**
     * The operating system
     */
    private final ObjectProperty<OperatingSystem> operatingSystem;

    /**
     * Information about whether the user wants to see commercial applications or not
     */
    private final BooleanProperty containCommercialApplications;

    /**
     * Information about whether the user wants to see scripts requiring patches
     */
    private final BooleanProperty containRequiresPatchApplications;

    /**
     * Information about whether the user wants to see scripts that are still in testing
     */
    private final BooleanProperty containTestingApplications;

    /**
     * Information about whether the user wants to see scripts that are not tested on his operating system
     */
    private final BooleanProperty containAllOSCompatibleApplications;

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
        this.javaFxSettingsManager = new SimpleObjectProperty<>();
        this.searchTerm = new SimpleStringProperty();
        this.filterCategory = new SimpleObjectProperty<>();
        this.fuzzySearchRatio = new SimpleDoubleProperty();
        this.operatingSystem = new SimpleObjectProperty<>();
        this.containCommercialApplications = new SimpleBooleanProperty();
        this.containRequiresPatchApplications = new SimpleBooleanProperty();
        this.containTestingApplications = new SimpleBooleanProperty();
        this.containAllOSCompatibleApplications = new SimpleBooleanProperty();
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

    public JavaFxSettingsManager getJavaFxSettingsManager() {
        return this.javaFxSettingsManager.get();
    }

    public ObjectProperty<JavaFxSettingsManager> javaFxSettingsManagerProperty() {
        return this.javaFxSettingsManager;
    }

    public void setJavaFxSettingsManager(JavaFxSettingsManager javaFxSettingsManager) {
        this.javaFxSettingsManager.set(javaFxSettingsManager);
    }

    public String getSearchTerm() {
        return this.searchTerm.get();
    }

    @Override
    public StringProperty searchTermProperty() {
        return this.searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.set(searchTerm);
    }

    public CategoryDTO getFilterCategory() {
        return this.filterCategory.get();
    }

    @Override
    public ObjectProperty<CategoryDTO> filterCategoryProperty() {
        return this.filterCategory;
    }

    public void setFilterCategory(CategoryDTO filterCategory) {
        this.filterCategory.set(filterCategory);
    }

    public double getFuzzySearchRatio() {
        return this.fuzzySearchRatio.get();
    }

    @Override
    public DoubleProperty fuzzySearchRatioProperty() {
        return this.fuzzySearchRatio;
    }

    public void setFuzzySearchRatio(double fuzzySearchRatio) {
        this.fuzzySearchRatio.set(fuzzySearchRatio);
    }

    public OperatingSystem getOperatingSystem() {
        return this.operatingSystem.get();
    }

    @Override
    public ObjectProperty<OperatingSystem> operatingSystemProperty() {
        return this.operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem.set(operatingSystem);
    }

    public boolean isContainCommercialApplications() {
        return this.containCommercialApplications.get();
    }

    @Override
    public BooleanProperty containCommercialApplicationsProperty() {
        return this.containCommercialApplications;
    }

    public void setContainCommercialApplications(boolean containCommercialApplications) {
        this.containCommercialApplications.set(containCommercialApplications);
    }

    public boolean isContainRequiresPatchApplications() {
        return this.containRequiresPatchApplications.get();
    }

    @Override
    public BooleanProperty containRequiresPatchApplicationsProperty() {
        return this.containRequiresPatchApplications;
    }

    public void setContainRequiresPatchApplications(boolean containRequiresPatchApplications) {
        this.containRequiresPatchApplications.set(containRequiresPatchApplications);
    }

    public boolean isContainTestingApplications() {
        return this.containTestingApplications.get();
    }

    @Override
    public BooleanProperty containTestingApplicationsProperty() {
        return this.containTestingApplications;
    }

    public void setContainTestingApplications(boolean containTestingApplications) {
        this.containTestingApplications.set(containTestingApplications);
    }

    public boolean isContainAllOSCompatibleApplications() {
        return this.containAllOSCompatibleApplications.get();
    }

    @Override
    public BooleanProperty containAllOSCompatibleApplicationsProperty() {
        return this.containAllOSCompatibleApplications;
    }

    public void setContainAllOSCompatibleApplications(boolean containAllOSCompatibleApplications) {
        this.containAllOSCompatibleApplications.set(containAllOSCompatibleApplications);
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
