package org.phoenicis.scripts.engine;

import javax.script.ScriptEngine;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JSAScriptEngine implements PhoenicisScriptEngine {
    private final ScriptEngine scriptEngine;
    private final List<Consumer<Exception>> errorHandlers = new ArrayList<>();

    public JSAScriptEngine(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    public void eval(InputStreamReader inputStreamReader, Consumer<Exception> errorCallback) {
        try {
            this.scriptEngine.eval(inputStreamReader);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    public void eval(String script, Runnable doneCallback, Consumer<Exception> errorCallback) {
        try {
            this.scriptEngine.eval(script);
            doneCallback.run();
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    public Object evalAndReturn(String line, Consumer<Exception> errorCallback) {
        try {
            final Object evaluation = this.scriptEngine.eval(line);
            if (evaluation == null) {
                return null;
            }
            return evaluation;
        } catch (Exception e) {
            handleError(errorCallback, e);
            return "";
        }
    }

    private void handleError(Consumer<Exception> errorCallback, Exception e) {
        for (Consumer<Exception> errorHandler : errorHandlers) {
            errorHandler.accept(e);
        }
        errorCallback.accept(e);
    }

    public void put(String name, Object object, Consumer<Exception> errorCallback) {
        try {
            this.scriptEngine.put(name, object);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    public void addErrorHandler(Consumer<Exception> errorHandler) {
        errorHandlers.add(errorHandler);
    }

}
