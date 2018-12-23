package org.phoenicis.javafx.components.application.control;

import javafx.beans.property.*;
import org.phoenicis.javafx.components.application.skin.ApplicationDetailsPanelSkin;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.common.control.DetailsPanelBase;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationFilter;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

public class ApplicationDetailsPanel extends DetailsPanelBase<ApplicationDetailsPanel, ApplicationDetailsPanelSkin> {
    private final ScriptInterpreter scriptInterpreter;

    private final ApplicationFilter filter;

    private final ObjectProperty<ApplicationDTO> application;

    private final BooleanProperty showScriptSource;

    private final StringProperty webEngineStylesheet;

    public ApplicationDetailsPanel(ScriptInterpreter scriptInterpreter, ApplicationFilter filter,
            ObjectProperty<ApplicationDTO> application, BooleanProperty showScriptSource,
            StringProperty webEngineStylesheet, ObjectProperty<Runnable> onClose) {
        super(onClose);

        this.scriptInterpreter = scriptInterpreter;
        this.filter = filter;
        this.application = application;
        this.showScriptSource = showScriptSource;
        this.webEngineStylesheet = webEngineStylesheet;
    }

    public ApplicationDetailsPanel(ScriptInterpreter scriptInterpreter, ApplicationFilter filter,
            ObjectProperty<ApplicationDTO> application) {
        this(scriptInterpreter, filter, application, new SimpleBooleanProperty(), new SimpleStringProperty(),
                new SimpleObjectProperty<>());
    }

    @Override
    public ApplicationDetailsPanelSkin createSkin() {
        return new ApplicationDetailsPanelSkin(this);
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
