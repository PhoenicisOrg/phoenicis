package com.playonlinux.cli;

import com.github.jankroken.commandline.annotations.AllAvailableArguments;
import com.github.jankroken.commandline.annotations.LongSwitch;
import com.github.jankroken.commandline.annotations.Option;
import com.github.jankroken.commandline.annotations.ShortSwitch;
import com.phoenicis.library.ShortcutRunner;
import com.phoenicis.library.dto.ShortcutDTO;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class CLIController implements AutoCloseable {
    private final ConfigurableApplicationContext applicationContext;

    public CLIController() {
        applicationContext = new AnnotationConfigApplicationContext(CLIConfiguration.class);
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

    @Override
    public void close() throws InterruptedException {
        applicationContext.close();
    }
}
