package org.phoenicis.javafx.components.application.control;

import javafx.beans.property.*;
import javafx.scene.web.WebView;
import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.javafx.components.application.skin.ApplicationInformationPanelSkin;
import org.phoenicis.javafx.components.application.utils.ScriptFilter;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

/**
 * A details panel for the applications tab used to show the details for a selected application
 */
public class ApplicationInformationPanel
        extends ControlBase<ApplicationInformationPanel, ApplicationInformationPanelSkin> implements ScriptFilter {
    /**
     * The script interpreter to execute installation scripts
     */
    private final ObjectProperty<ScriptInterpreter> scriptInterpreter;

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
     * The shown application
     */
    private final ObjectProperty<ApplicationDTO> application;

    /**
     * Boolean flag to decide whether the script sources should be shown
     */
    private final BooleanProperty showScriptSource;

    /**
     * The stylesheet for the {@link WebView}
     */
    private final StringProperty webEngineStylesheet;

    /**
     * Constructor
     */
    public ApplicationInformationPanel() {
        super();

        this.scriptInterpreter = new SimpleObjectProperty<>();
        this.operatingSystem = new SimpleObjectProperty<>();
        this.containCommercialApplications = new SimpleBooleanProperty();
        this.containRequiresPatchApplications = new SimpleBooleanProperty();
        this.containTestingApplications = new SimpleBooleanProperty();
        this.containAllOSCompatibleApplications = new SimpleBooleanProperty();
        this.application = new SimpleObjectProperty<>();
        this.showScriptSource = new SimpleBooleanProperty();
        this.webEngineStylesheet = new SimpleStringProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInformationPanelSkin createSkin() {
        return new ApplicationInformationPanelSkin(this);
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

    public ApplicationDTO getApplication() {
        return this.application.get();
    }

    public ObjectProperty<ApplicationDTO> applicationProperty() {
        return this.application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application.set(application);
    }

    public boolean isShowScriptSource() {
        return this.showScriptSource.get();
    }

    public BooleanProperty showScriptSourceProperty() {
        return this.showScriptSource;
    }

    public void setShowScriptSource(boolean showScriptSource) {
        this.showScriptSource.set(showScriptSource);
    }

    public String getWebEngineStylesheet() {
        return this.webEngineStylesheet.get();
    }

    public StringProperty webEngineStylesheetProperty() {
        return this.webEngineStylesheet;
    }

    public void setWebEngineStylesheet(String webEngineStylesheet) {
        this.webEngineStylesheet.set(webEngineStylesheet);
    }
}
