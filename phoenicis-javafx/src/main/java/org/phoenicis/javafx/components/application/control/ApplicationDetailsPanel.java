package org.phoenicis.javafx.components.application.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import org.phoenicis.javafx.components.application.skin.ApplicationDetailsPanelSkin;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationFilter;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

public class ApplicationDetailsPanel extends ControlBase<ApplicationDetailsPanel, ApplicationDetailsPanelSkin> {
    private final ScriptInterpreter scriptInterpreter;

    private final ApplicationFilter filter;

    private final ObjectProperty<ApplicationDTO> application;

    private final BooleanProperty showScriptSource;

    private final StringProperty webEngineStylesheet;

    private final ObjectProperty<Runnable> onClose;

    public ApplicationDetailsPanel(ScriptInterpreter scriptInterpreter, ApplicationFilter filter, ObjectProperty<ApplicationDTO> application, BooleanProperty showScriptSource, StringProperty webEngineStylesheet, ObjectProperty<Runnable> onClose) {
        super();

        this.scriptInterpreter = scriptInterpreter;
        this.filter = filter;
        this.application = application;
        this.showScriptSource = showScriptSource;
        this.webEngineStylesheet = webEngineStylesheet;
        this.onClose = onClose;
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

    public boolean isShowScriptSource() {
        return showScriptSource.get();
    }

    public BooleanProperty showScriptSourceProperty() {
        return showScriptSource;
    }

    public String getWebEngineStylesheet() {
        return webEngineStylesheet.get();
    }

    public StringProperty webEngineStylesheetProperty() {
        return webEngineStylesheet;
    }

    public Runnable getOnClose() {
        return onClose.get();
    }

    public ObjectProperty<Runnable> onCloseProperty() {
        return onClose;
    }
}
