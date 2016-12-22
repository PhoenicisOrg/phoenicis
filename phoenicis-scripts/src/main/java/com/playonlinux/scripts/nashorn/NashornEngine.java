package com.playonlinux.scripts.nashorn;

import javax.script.ScriptEngine;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class NashornEngine {
    private final ScriptEngine scriptEngine;
    private final List<Consumer<Exception>> errorHandlers = new ArrayList<>();

    NashornEngine(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    void eval(InputStreamReader inputStreamReader, Consumer<Exception> errorCallback) {
        try {
            this.scriptEngine.eval(inputStreamReader);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    void eval(String script, Consumer<Exception> errorCallback) {
        try {
            this.scriptEngine.eval(script);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    String evalAndReturn(String line, Consumer<Exception> errorCallback) {
        try {
            final Object evaluation = this.scriptEngine.eval(line);
            if(evaluation == null) {
                return null;
            }
            return evaluation.toString();
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


    void put(String name, Object object, Consumer<Exception> errorCallback) {
        try {
            this.scriptEngine.put(name, object);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    void addErrorHandler(Consumer<Exception> errorHandler) {
        errorHandlers.add(errorHandler);
    }


}
