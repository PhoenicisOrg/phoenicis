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

package org.phoenicis.scripts;

import org.phoenicis.apps.AppsConfiguration;
import org.phoenicis.multithreading.MultithreadingConfiguration;
import org.phoenicis.scripts.interpreter.BackgroundScriptInterpreter;
import org.phoenicis.scripts.interpreter.ScriptFetcher;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.phoenicis.scripts.nashorn.NashornEngineFactory;
import org.phoenicis.scripts.nashorn.NashornScriptInterpreter;
import org.phoenicis.scripts.wizard.WizardConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WizardConfiguration.class)
public class ScriptsConfiguration {
    @Autowired
    private WizardConfiguration wizardConfiguration;

    @Autowired
    private AppsConfiguration appsConfiguration;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MultithreadingConfiguration multithreadingConfiguration;

    @Bean
    public NashornEngineFactory scriptEngineFactory() {
        return new NashornEngineFactory(wizardConfiguration.setupWizardFactory(), wizardConfiguration.progressWizardFactory(), scriptFetcher());
    }

    @Bean
    public ScriptFetcher scriptFetcher() {
        return new ScriptFetcher(appsConfiguration.repository());
    }

    @Bean
    public ScriptInterpreter scriptInterpreter() {
        return new BackgroundScriptInterpreter(nashornInterprpeter(), multithreadingConfiguration.scriptExecutorService());
    }

    @Bean
    ScriptInterpreter nashornInterprpeter() {
        return new NashornScriptInterpreter(scriptEngineFactory());
    }
}
