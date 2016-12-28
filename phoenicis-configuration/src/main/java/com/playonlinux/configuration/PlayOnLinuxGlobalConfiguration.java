package com.playonlinux.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:${os.name}.properties")
@PropertySource(value="file:${application.user.settings}", ignoreResourceNotFound = true)
public class PlayOnLinuxGlobalConfiguration {
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public PropertyReader propertyReader() {
        return new PropertyReader();
    }
}
