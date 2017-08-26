package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;
import org.phoenicis.scripts.wizard.UiSetupWizardFactory;
import org.phoenicis.scripts.wizard.UiSetupWizardImplementation;

import java.net.URI;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Injects SetupWizard() function into a script engine
 */
public class SetupWizardInjector implements EngineInjector {
    private final UiSetupWizardFactory uiSetupWizardFactory;

    public SetupWizardInjector(UiSetupWizardFactory uiSetupWizardFactory) {
        this.uiSetupWizardFactory = uiSetupWizardFactory;
    }

    @Override
    public void injectInto(NashornEngine nashornEngine) {
        nashornEngine.put("SetupWizard", (Function<String, UiSetupWizardImplementation>) (name) -> {
            final UiSetupWizardImplementation uiSetupWizardImplementation = uiSetupWizardFactory.create(name,
                    Optional.empty());
            nashornEngine.addErrorHandler(e -> uiSetupWizardImplementation.close());
            return uiSetupWizardImplementation;
        }, this::throwException);
        nashornEngine.put("SetupWizardWithMiniature",
                (BiFunction<String, Optional<URI>, UiSetupWizardImplementation>) (name, miniature) -> {
                    final UiSetupWizardImplementation uiSetupWizardImplementation = uiSetupWizardFactory.create(name,
                            miniature);
                    nashornEngine.addErrorHandler(e -> uiSetupWizardImplementation.close());
                    return uiSetupWizardImplementation;
                }, this::throwException);
    }
}
