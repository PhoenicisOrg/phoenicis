package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;
import org.phoenicis.scripts.wizard.UiSetupWizardFactory;
import org.phoenicis.scripts.wizard.UiSetupWizardImplementation;

import java.util.function.Function;

public class SetupWizardInjector implements EngineInjector {
    private final UiSetupWizardFactory uiSetupWizardFactory;

    public SetupWizardInjector(UiSetupWizardFactory uiSetupWizardFactory) {
        this.uiSetupWizardFactory = uiSetupWizardFactory;
    }

    @Override
    public void inject(NashornEngine nashornEngine) {
        nashornEngine.put("SetupWizard", (Function<String, UiSetupWizardImplementation>) (name) -> {
            final UiSetupWizardImplementation uiSetupWizardImplementation = uiSetupWizardFactory.create(name);
            nashornEngine.addErrorHandler(e -> uiSetupWizardImplementation.close());
            return uiSetupWizardImplementation;
        }, this::throwException);
    }
}
