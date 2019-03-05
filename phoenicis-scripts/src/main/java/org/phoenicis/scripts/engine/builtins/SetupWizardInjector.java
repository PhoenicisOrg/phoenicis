package org.phoenicis.scripts.engine.builtins;

import org.phoenicis.scripts.TriFunction;
import org.phoenicis.scripts.engine.PhoenicisScriptContext;
import org.phoenicis.scripts.ui.InstallationType;
import org.phoenicis.scripts.wizard.UiSetupWizardFactory;
import org.phoenicis.scripts.wizard.UiSetupWizardImplementation;

import java.net.URI;
import java.util.Optional;

/**
 * Injects SetupWizard() function into a script engine
 */
public class SetupWizardInjector implements ScriptContextInjector {
    private final UiSetupWizardFactory uiSetupWizardFactory;

    public SetupWizardInjector(UiSetupWizardFactory uiSetupWizardFactory) {
        this.uiSetupWizardFactory = uiSetupWizardFactory;
    }

    @Override
    public void injectInto(PhoenicisScriptContext phoenicisScriptContext) {
        phoenicisScriptContext.eval(
                "var InstallationType = Java.type(\"" + InstallationType.class.getCanonicalName() + "\")",
                this::throwException);
        phoenicisScriptContext.put("SetupWizard",
                (TriFunction<InstallationType, String, Optional<URI>, UiSetupWizardImplementation>) (installationType,
                        name, miniature) -> {
                    final UiSetupWizardImplementation uiSetupWizardImplementation = uiSetupWizardFactory.create(name,
                            miniature, installationType);
                    phoenicisScriptContext.addErrorHandler(e -> uiSetupWizardImplementation.close());
                    return uiSetupWizardImplementation;
                },
                this::throwException);
    }
}
