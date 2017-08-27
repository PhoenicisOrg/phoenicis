package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.scripts.nashorn.NashornEngine;
import org.phoenicis.scripts.ui.InstallationType;
import org.phoenicis.scripts.wizard.UiSetupWizardFactory;
import org.phoenicis.scripts.wizard.UiSetupWizardImplementation;

import java.net.URI;
import java.util.Optional;
import java.util.function.BiFunction;

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
        nashornEngine.put("SetupWizardApps",
                (BiFunction<String, Optional<URI>, UiSetupWizardImplementation>) (name, miniature) -> {
                    final UiSetupWizardImplementation uiSetupWizardImplementation = uiSetupWizardFactory.create(name,
                            miniature, InstallationType.APPS);
                    nashornEngine.addErrorHandler(e -> uiSetupWizardImplementation.close());
                    return uiSetupWizardImplementation;
                },
                this::throwException);

        nashornEngine.put("SetupWizardEngines",
                (BiFunction<String, Optional<URI>, UiSetupWizardImplementation>) (name, miniature) -> {
                    final UiSetupWizardImplementation uiSetupWizardImplementation = uiSetupWizardFactory.create(name,
                            miniature, InstallationType.ENGINES);
                    nashornEngine.addErrorHandler(e -> uiSetupWizardImplementation.close());
                    return uiSetupWizardImplementation;
                },
                this::throwException);
    }
}
