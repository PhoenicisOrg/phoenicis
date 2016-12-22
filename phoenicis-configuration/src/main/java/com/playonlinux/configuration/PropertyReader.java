package com.playonlinux.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class PropertyReader {
    @Autowired
    private ApplicationContext applicationContext;

    public String getProperty(String property) {
        return applicationContext.getEnvironment().getProperty(property);
    }
}
