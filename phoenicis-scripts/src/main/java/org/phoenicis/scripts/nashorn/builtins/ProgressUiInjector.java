package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;
import org.phoenicis.scripts.wizard.UiProgressWizardFactory;
import org.phoenicis.scripts.wizard.UiProgressWizardImplementation;

import java.util.function.Function;

/**
 * Injects EngineProgressUi() function into a Script Engine
 */

public class ProgressUiInjector implements EngineInjector {
    private final UiProgressWizardFactory uiProgressWizardFactory;

    public ProgressUiInjector(UiProgressWizardFactory uiProgressWizardFactory) {
        this.uiProgressWizardFactory = uiProgressWizardFactory;
    }

    @Override
    public void injectInto(NashornEngine nashornEngine) {
        nashornEngine.put("EngineProgressUi", (Function<String, UiProgressWizardImplementation>) (name) -> {
            final UiProgressWizardImplementation uiProgressWizardImplementation = uiProgressWizardFactory.create(name);
            nashornEngine.addErrorHandler(e -> uiProgressWizardImplementation.close());
            return uiProgressWizardImplementation;
        }, this::throwException);
    }
}
