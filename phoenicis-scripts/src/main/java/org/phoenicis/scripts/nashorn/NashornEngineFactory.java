/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.scripts.nashorn;

import org.phoenicis.scripts.interpreter.ScriptException;
import org.phoenicis.scripts.interpreter.ScriptFetcher;
import org.phoenicis.scripts.wizard.UiSetupWizardImplementation;
import org.phoenicis.scripts.wizard.UiSetupWizardFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.phoenicis.scripts.wizard.WizardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class NashornEngineFactory {
    private final UiSetupWizardFactory uiSetupWizardFactory;
    private final ScriptFetcher scriptFetcher;

    @Autowired
    private ApplicationContext applicationContext;

    public NashornEngineFactory(UiSetupWizardFactory uiSetupWizardFactory, ScriptFetcher scriptFetcher) {
        this.uiSetupWizardFactory = uiSetupWizardFactory;
        this.scriptFetcher = scriptFetcher;
    }

    NashornEngine createEngine() {
        final Set<List<String>> includedScripts = new HashSet<>();

        final NashornEngine nashornEngine = new NashornEngine(
                new ScriptEngineManager().getEngineByName("nashorn")
        );

        nashornEngine.eval(
                new InputStreamReader(getClass().getResourceAsStream("utils.js")),
                this::throwException
        );

        nashornEngine.put("Bean", (Function<String, Object>) title -> applicationContext.getBean(title), this::throwException);
        nashornEngine.put("UiSetupWizardImplementation", (Function<String, UiSetupWizardImplementation>) (name) -> {
            final UiSetupWizardImplementation uiSetupWizardImplementation = uiSetupWizardFactory.create(name, WizardType.APP);
            nashornEngine.addErrorHandler(e -> uiSetupWizardImplementation.close());
            return uiSetupWizardImplementation;
        }, this::throwException);

        nashornEngine.put("EngineSetupWizard", (Function<String, UiSetupWizardImplementation>) (name) -> {
            final UiSetupWizardImplementation uiSetupWizardImplementation = uiSetupWizardFactory.create(name, WizardType.ENGINE);
            nashornEngine.addErrorHandler(e -> uiSetupWizardImplementation.close());
            return uiSetupWizardImplementation;
        }, this::throwException);

        nashornEngine.put("include", (Consumer<ScriptObjectMirror>) args -> {
            final String[] arguments = args.to(String[].class);
            final String script = scriptFetcher.getScript(arguments);
            if(script == null) {
                throwException(new ScriptException(Arrays.asList(arguments).toString() + " is not found"));
            }

            if(includedScripts.add(Arrays.asList(arguments))) {
                nashornEngine.eval("//# sourceURL=" + Arrays.asList(arguments).toString() + "\n" + script, this::throwException);
            }
        }, this::throwException);

        return nashornEngine;
    }

    private void throwException(Exception e) {
        throw new org.phoenicis.scripts.interpreter.ScriptException(e);
    }
}
