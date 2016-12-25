package com.playonlinux.cli;

import com.github.jankroken.commandline.annotations.AllAvailableArguments;
import com.github.jankroken.commandline.annotations.LongSwitch;
import com.github.jankroken.commandline.annotations.Option;
import com.github.jankroken.commandline.annotations.ShortSwitch;
import com.phoenicis.library.ShortcutRunner;
import com.playonlinux.apps.ApplicationsSource;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.List;

public class CLIController implements AutoCloseable {
    private final ConfigurableApplicationContext applicationContext;
    private final ApplicationsSource applicationsSource;
    private final ScriptInterpreter scriptInterpreter;

    public CLIController() {
        applicationContext = new AnnotationConfigApplicationContext(CLIConfiguration.class);
        applicationsSource = applicationContext.getBean("backgroundAppsManager", ApplicationsSource.class);
        scriptInterpreter = applicationContext.getBean("scriptInterpreter", ScriptInterpreter.class);
    }

    @Option
    @LongSwitch("run")
    @ShortSwitch("r")
    @AllAvailableArguments
    public void runApp(List<String> arguments) {
        final String shortcutName = arguments.get(0);
        arguments.remove(0);

        final ShortcutRunner shortcutRunner = applicationContext.getBean(ShortcutRunner.class);
        shortcutRunner.run(shortcutName, arguments, e -> { throw new IllegalStateException(e); });
    }

    @Option
    @LongSwitch("install")
    @ShortSwitch("i")
    @AllAvailableArguments
    public void installApp(List<String> arguments) {
        final String categoryName = arguments.get(0);
        final String appName = arguments.get(1);
        final String scriptName = arguments.get(2);

        applicationsSource.getScript(Arrays.asList(categoryName, appName, scriptName), scriptDTO -> {
            scriptInterpreter.runScript(scriptDTO.getScript(), Throwable::printStackTrace);
        }, e -> { throw new IllegalStateException(e); });
    }

    @Override
    public void close() throws InterruptedException {
        applicationContext.close();
    }
}
