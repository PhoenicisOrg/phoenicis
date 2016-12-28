package com.playonlinux.cli;


import com.phoenicis.library.LibraryConfiguration;
import com.playonlinux.apps.AppsConfiguration;
import com.playonlinux.cli.setupwindow.SetupWindowCLIConfiguration;
import com.playonlinux.configuration.PlayOnLinuxGlobalConfiguration;
import com.playonlinux.engines.EnginesConfiguration;
import com.playonlinux.multithreading.MultithreadingConfiguration;
import com.playonlinux.scripts.ScriptsConfiguration;
import com.playonlinux.tools.ToolsConfiguration;
import com.playonlinux.win32.Win32Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PlayOnLinuxGlobalConfiguration.class,
        ScriptsConfiguration.class,
        AppsConfiguration.class,
        EnginesConfiguration.class,
        LibraryConfiguration.class,
        Win32Configuration.class,
        ToolsConfiguration.class,
        MultithreadingConfiguration.class,
        SetupWindowCLIConfiguration.class
})
class CLIConfiguration {

}
