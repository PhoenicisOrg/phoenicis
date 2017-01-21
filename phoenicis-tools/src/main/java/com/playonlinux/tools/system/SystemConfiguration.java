package com.playonlinux.tools.system;

import com.playonlinux.tools.system.terminal.AutomaticTerminalOpener;
import com.playonlinux.tools.system.terminal.TerminalOpener;
import com.playonlinux.tools.system.terminal.TerminalOpenerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfiguration {
    @Bean
    public TerminalOpener terminalOpener() {
        return new AutomaticTerminalOpener(terminalOpenerFactory(), operatingSystemFetcher());
    }

    @Bean
    public TerminalOpenerFactory terminalOpenerFactory() {
        return new TerminalOpenerFactory();
    }

    @Bean
    public OperatingSystemFetcher operatingSystemFetcher() {
        return new OperatingSystemFetcher();
    }
}
