package org.phoenicis.javafx.components.application.control;

import javafx.beans.property.*;
import javafx.scene.web.WebView;
import org.phoenicis.javafx.components.application.skin.ApplicationInformationPanelSkin;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationFilter;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

/**
 * A details panel for the applications tab used to show the details for a selected application
 */
public class ApplicationInformationPanel
        extends ControlBase<ApplicationInformationPanel, ApplicationInformationPanelSkin> {
    /**
     * The script interpreter to execute installation scripts
     */
    private final ScriptInterpreter scriptInterpreter;

    /**
     * The filter settings to be used to filter the installation scripts
     */
    private final ApplicationFilter filter;

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
     *
     * @param scriptInterpreter The script interpreter to execute installation scripts
     * @param filter The filter settings to be used to filter the installation scripts
     * @param application The shown application
     * @param showScriptSource Boolean flag to decide whether the script sources should be shown
     * @param webEngineStylesheet The stylesheet for the {@link WebView}
     */
    public ApplicationInformationPanel(ScriptInterpreter scriptInterpreter, ApplicationFilter filter,
            ObjectProperty<ApplicationDTO> application, BooleanProperty showScriptSource,
            StringProperty webEngineStylesheet) {
        super();

        this.scriptInterpreter = scriptInterpreter;
        this.filter = filter;
        this.application = application;
        this.showScriptSource = showScriptSource;
        this.webEngineStylesheet = webEngineStylesheet;
    }

    /**
     * Constructor
     *
     * @param scriptInterpreter The script interpreter to execute installation scripts
     * @param filter The filter settings to be used to filter the installation scripts
     * @param application The shown application
     */
    public ApplicationInformationPanel(ScriptInterpreter scriptInterpreter, ApplicationFilter filter,
            ObjectProperty<ApplicationDTO> application) {
        this(scriptInterpreter, filter, application, new SimpleBooleanProperty(), new SimpleStringProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInformationPanelSkin createSkin() {
        return new ApplicationInformationPanelSkin(this);
    }

    public ScriptInterpreter getScriptInterpreter() {
        return scriptInterpreter;
    }

    public ApplicationFilter getFilter() {
        return filter;
    }

    public ApplicationDTO getApplication() {
        return application.get();
    }

    public ObjectProperty<ApplicationDTO> applicationProperty() {
        return application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application.set(application);
    }

    public boolean isShowScriptSource() {
        return showScriptSource.get();
    }

    public BooleanProperty showScriptSourceProperty() {
        return showScriptSource;
    }

    public void setShowScriptSource(boolean showScriptSource) {
        this.showScriptSource.set(showScriptSource);
    }

    public String getWebEngineStylesheet() {
        return webEngineStylesheet.get();
    }

    public StringProperty webEngineStylesheetProperty() {
        return webEngineStylesheet;
    }

    public void setWebEngineStylesheet(String webEngineStylesheet) {
        this.webEngineStylesheet.set(webEngineStylesheet);
    }
}
