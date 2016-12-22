package com.playonlinux.scripts.nashorn;

import com.playonlinux.scripts.interpreter.ScriptFetcher;
import com.playonlinux.scripts.wizard.SetupWizard;
import com.playonlinux.scripts.wizard.SetupWizardFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.function.Function;

public class NashornEngineFactory {
    private final SetupWizardFactory setupWizardFactory;
    private final ScriptFetcher scriptFetcher;
    @Autowired
    private ApplicationContext applicationContext;

    public NashornEngineFactory(SetupWizardFactory setupWizardFactory, ScriptFetcher scriptFetcher) {
        this.setupWizardFactory = setupWizardFactory;
        this.scriptFetcher = scriptFetcher;
    }

    public NashornEngine createEngine() {
        final NashornEngine nashornEngine = new NashornEngine(
                new ScriptEngineManager().getEngineByName("nashorn")
        );

        nashornEngine.eval(
                new InputStreamReader(getClass().getResourceAsStream("utils.js")),
                this::throwException
        );

        nashornEngine.put("Bean", (Function<String, Object>) title -> applicationContext.getBean(title), this::throwException);
        nashornEngine.put("SetupWizard", (Function<String, SetupWizard>) name -> {
            final SetupWizard setupWizard = setupWizardFactory.create(name);
            nashornEngine.addErrorHandler(e -> setupWizard.close());
            return setupWizard;
        }, this::throwException);

        nashornEngine.put("include", (Consumer<ScriptObjectMirror>) args -> nashornEngine.eval(scriptFetcher.getScript(args.to(String[].class)), this::throwException), this::throwException);

        return nashornEngine;
    }

    private void throwException(Exception e) {
        throw new com.playonlinux.scripts.interpreter.ScriptException(e);
    }
}
