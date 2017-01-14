package com.playonlinux.tests;

import com.playonlinux.apps.ApplicationsSource;
import com.playonlinux.apps.AppsConfiguration;
import com.playonlinux.configuration.PlayOnLinuxGlobalConfiguration;
import com.playonlinux.multithreading.MultithreadingConfiguration;
import com.playonlinux.scripts.ScriptsConfiguration;
import com.playonlinux.scripts.wizard.WizardConfiguration;
import com.playonlinux.tools.ToolsConfiguration;
import com.playonlinux.win32.Win32Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PlayOnLinuxGlobalConfiguration.class,
        MultithreadingConfiguration.class,
        Win32Configuration.class,
        ToolsConfiguration.class,
        AppsConfiguration.class,
        ScriptsConfiguration.class,
        WizardConfiguration.class,
        TestUIConfiguration.class,
})
class PhoenicisTestsConfiguration {
    @Autowired
    private AppsConfiguration appsConfiguration;

    @Bean
    public ApplicationsSource mockedApplicationSource() {
        return new MockedApplicationSource(appsConfiguration.appsSource());
    }

}
