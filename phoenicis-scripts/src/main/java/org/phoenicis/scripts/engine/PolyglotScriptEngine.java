package org.phoenicis.scripts.engine;

import com.google.common.util.concurrent.Runnables;
import org.apache.commons.io.IOUtils;
import org.graalvm.polyglot.Context;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PolyglotScriptEngine implements PhoenicisScriptEngine {
    private final List<Consumer<Exception>> errorHandlers = new ArrayList<>();

    private final String language;

    private final Context context;

    public PolyglotScriptEngine(String language, Map<String, String> options) {
        super();

        this.language = language;
        this.context = Context.newBuilder(language)
                .options(options).allowHostAccess(true).build();
    }

    @Override
    public void eval(InputStreamReader inputStreamReader, Consumer<Exception> errorCallback) {
        try {
            String script = IOUtils.toString(inputStreamReader);

            eval(script, Runnables.doNothing(), errorCallback);
        } catch (IOException ioe) {
            handleError(errorCallback, ioe);
        }
    }

    @Override
    public void eval(String script, Runnable doneCallback, Consumer<Exception> errorCallback) {
        try {
            this.context.eval(this.language, script);
        } catch (Exception e) {
            handleError(errorCallback, e);
        }
    }

    @Override
    public Object evalAndReturn(String script, Consumer<Exception> errorCallback) {
        try {
            return this.context.eval(this.language, script);
        } catch (Exception e) {
            handleError(errorCallback, e);

            return "";
        }
    }

    @Override
    public void put(String name, Object object, Consumer<Exception> errorCallback) {
        this.context.getBindings(this.language).putMember(name, object);
    }

    @Override
    public void addErrorHandler(Consumer<Exception> errorHandler) {
        this.errorHandlers.add(errorHandler);
    }

    private void handleError(Consumer<Exception> errorCallback, Exception e) {
        for (Consumer<Exception> errorHandler : this.errorHandlers) {
            errorHandler.accept(e);
        }

        errorCallback.accept(e);
    }
}
