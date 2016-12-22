package com.playonlinux.scripts;

import com.playonlinux.apps.AppsConfiguration;
import com.playonlinux.scripts.interpreter.BackgroundScriptInterpreter;
import com.playonlinux.scripts.interpreter.ScriptFetcher;
import com.playonlinux.scripts.nashorn.NashornEngineFactory;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;
import com.playonlinux.scripts.nashorn.NashornScriptInterpreter;
import com.playonlinux.scripts.wizard.WizardConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Import(WizardConfiguration.class)
public class ScriptsConfiguration {
    @Autowired
    private WizardConfiguration wizardConfiguration;

    @Autowired
    private AppsConfiguration appsConfiguration;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public NashornEngineFactory scriptEngineFactory() {
        return new NashornEngineFactory(wizardConfiguration.setupWindowFactory(), scriptFetcher());
    }

    @Bean
    public ScriptFetcher scriptFetcher() {
        return new ScriptFetcher(appsConfiguration.appsManager());
    }

    @Bean
    public ScriptInterpreter scriptInterpreter() {
        return new BackgroundScriptInterpreter(nashornInterprpeter(), executorService());
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public ScriptInterpreter nashornInterprpeter() {
        return new NashornScriptInterpreter(scriptEngineFactory());
    }
}
