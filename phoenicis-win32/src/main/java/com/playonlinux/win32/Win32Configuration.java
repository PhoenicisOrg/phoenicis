package com.playonlinux.win32;

import com.playonlinux.win32.pe.PEReader;
import com.playonlinux.win32.registry.RegistryParser;
import com.playonlinux.win32.registry.RegistryWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Win32Configuration {
    @Bean
    public RegistryWriter registryWriter() {
        return new RegistryWriter();
    }

    @Bean
    public RegistryParser registryParser() {
        return new RegistryParser();
    }

    @Bean
    public PEReader peReader() {
        return new PEReader();
    }
}
