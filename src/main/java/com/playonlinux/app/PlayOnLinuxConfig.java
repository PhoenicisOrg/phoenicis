/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.app;

import com.playonlinux.log.LogStreamFactory;
import com.playonlinux.python.InterpreterFactory;
import com.playonlinux.python.JythonCommandInterpreterFactory;
import com.playonlinux.python.JythonInterpreterFactory;
import com.playonlinux.services.BackgroundServiceManager;
import com.playonlinux.services.EventDispatcher;
import com.playonlinux.ui.Controller;
import com.playonlinux.installer.InstallerSource;
import com.playonlinux.installer.ScriptFactory;
import com.playonlinux.installer.ScriptFactoryDefaultImplementation;
import com.playonlinux.services.EventDispatcherPlayOnLinuxImplementation;
import com.playonlinux.services.PlayOnLinuxBackgroundServicesManager;
import com.playonlinux.lang.LanguageBundle;
import com.playonlinux.lang.LanguageBundleSelector;
import com.playonlinux.injection.AbstractConfigFile;
import com.playonlinux.injection.Bean;
import com.playonlinux.ui.api.CommandInterpreterFactory;
import com.playonlinux.ui.impl.cli.ControllerCLIImplementation;
import com.playonlinux.ui.impl.gtk.ControllerGTKImplementation;
import com.playonlinux.ui.impl.javafx.ControllerJavaFXImplementation;
import com.playonlinux.installer.InstallerSourceWebserviceImplementation;
import com.playonlinux.webservice.DownloadManager;
import com.playonlinux.wine.versions.WineVersionSource;
import com.playonlinux.wine.versions.WineversionsSourceWebserviceImplementation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class PlayOnLinuxConfig extends AbstractConfigFile  {

    private boolean useCliInterface = false;
    private PlayOnLinuxContext playOnLinuxContext = new PlayOnLinuxContext();
    private BackgroundServiceManager playOnLinuxBackgroundServiceManager = new PlayOnLinuxBackgroundServicesManager();
    private boolean useGTKInterface;
    private ExecutorService executor = Executors.newCachedThreadPool();

    @Bean
    public Controller controller() {
        if(useCliInterface) {
            return new ControllerCLIImplementation();
        } else if(useGTKInterface) {
            return new ControllerGTKImplementation();
        } else {
            return new ControllerJavaFXImplementation();
        }
    }

    @Bean
    public InstallerSource installerSource() throws MalformedURLException {
        return new InstallerSourceWebserviceImplementation(new URL(playOnLinuxContext.getProperty("webservice.url")));
    }

    @Bean
    public WineVersionSource wineVersionSource() throws MalformedURLException {
        return new WineversionsSourceWebserviceImplementation(new URL(playOnLinuxContext.getProperty("webservice.wine.url")));
    }

    @Bean
    public EventDispatcher eventHandler() {
            return new EventDispatcherPlayOnLinuxImplementation();
    }

    @Bean
    public PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxException, IOException {
        return playOnLinuxContext;
    }

    @Bean
    public BackgroundServiceManager playOnLinuxBackgroundServicesManager() {
        return playOnLinuxBackgroundServiceManager;
    }

    @Bean
    public LanguageBundle languageBundle() {
        return LanguageBundleSelector.forLocale(Locale.getDefault());
    }

    @Bean
    public DownloadManager downloadManager() {
        DownloadManager downloadManager = new DownloadManager();
        playOnLinuxBackgroundServiceManager.register(downloadManager);
        return downloadManager;
    }

    @Bean
    public CommandInterpreterFactory commandInterpreterFactory() {
        return new JythonCommandInterpreterFactory(defaultExecutor());
    }

    @Bean
    public InterpreterFactory interpreterFactory() {
        return new JythonInterpreterFactory();
    }

    @Bean
    public LogStreamFactory logStreamFactory() {
        return new LogStreamFactory();
    }
    
    @Bean
    public ScriptFactory scriptFactory() {
        return new ScriptFactoryDefaultImplementation().withExecutor(defaultExecutor());
    }

    private ExecutorService defaultExecutor() {
        return executor;
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }

    public void setUseCLIInterface(boolean enabled) {
        this.useCliInterface = enabled;
    }

    public void setUseGTKInterface(boolean useGTKInterface) {
        this.useGTKInterface = useGTKInterface;
    }

    @Override
    public void close() {
        playOnLinuxBackgroundServiceManager.shutdown();
        executor.shutdownNow();
    }
}
