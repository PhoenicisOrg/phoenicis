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

import com.playonlinux.ui.api.CommandLineInterpreterFactory;
import com.playonlinux.ui.events.EventHandler;
import com.playonlinux.ui.events.EventHandlerPlayOnLinuxImplementation;
import com.playonlinux.core.injection.AbstractConfiguration;
import com.playonlinux.core.injection.Bean;
import com.playonlinux.core.scripts.InstallerSource;
import com.playonlinux.core.scripts.InstallerSourceWebserviceImplementation;
import com.playonlinux.core.scripts.ScriptFactory;
import com.playonlinux.core.scripts.ScriptFactoryDefaultImplementation;
import com.playonlinux.core.lang.LanguageBundle;
import com.playonlinux.core.lang.LanguageBundleSelector;
import com.playonlinux.core.log.LogStreamFactory;
import com.playonlinux.core.python.InterpreterFactory;
import com.playonlinux.core.python.JythonCommandLineInterpreterFactory;
import com.playonlinux.core.python.JythonInterpreterFactory;
import com.playonlinux.core.services.manager.PlayOnLinuxServicesManager;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.ui.api.Controller;
import com.playonlinux.ui.impl.cli.ControllerCLIImplementation;
import com.playonlinux.ui.impl.gtk.ControllerGTKImplementation;
import com.playonlinux.ui.impl.javafx.ControllerJavaFXImplementation;
import com.playonlinux.engines.wine.WineVersionSource;
import com.playonlinux.engines.wine.WineversionsSourceWebserviceImplementation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PlayOnLinux beans configuration file
 */
@SuppressWarnings("unused")
public class PlayOnLinuxConfig extends AbstractConfiguration {

    private boolean useCliInterface = false;
    private PlayOnLinuxContext playOnLinuxContext = new PlayOnLinuxContext();
    private ServiceManager playOnLinuxServiceManager = new PlayOnLinuxServicesManager();
    private boolean useGTKInterface;
    private ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * This bean represents the UI controller
     * @return the bean
     */
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

    /**
     * Source data for scripts
     * @return the bean
     * @throws MalformedURLException if the URL in the config file is malformed
     */
    @Bean
    public InstallerSource installerSource() throws MalformedURLException {
        return new InstallerSourceWebserviceImplementation(new URL(playOnLinuxContext.getProperty("webservice.url")));
    }

    /**
     * Source data for wine versions
     * @return the bean
     * @throws MalformedURLException if the URL in the config file is malformed
     */
    @Bean
    public WineVersionSource wineVersionSource() throws MalformedURLException {
        return new WineversionsSourceWebserviceImplementation(new URL(playOnLinuxContext.getProperty("webservice.wine.url")));
    }

    /**
     * Represents the UI event handler
     * @return the bean
     */
    @Bean
    public EventHandler eventHandler() {
            return new EventHandlerPlayOnLinuxImplementation();
    }

    /**
     * PlayOnLinux context
     * @return the bean
     */
    @Bean
    public PlayOnLinuxContext playOnLinuxContext() {
        return playOnLinuxContext;
    }

    /**
     * PlayOnLinux service manager
     * @return the bean
     */
    @Bean
    public ServiceManager playOnLinuxServiceManager() {
        return playOnLinuxServiceManager;
    }

    /**
     * PlayOnLinux language bundle
     * @return the bean
     */
    @Bean
    public LanguageBundle languageBundle() {
        return LanguageBundleSelector.forLocale(Locale.getDefault());
    }

    /**
     * Command interpreter factory. Used for the console window
     * @return the bean
     */
    @Bean
    public CommandLineInterpreterFactory commandLineInterpreterFactory() {
        return new JythonCommandLineInterpreterFactory(defaultExecutor());
    }

    /**
     * Script interpreter factory
     * @return the bean
     */
    @Bean
    public InterpreterFactory interpreterFactory() {
        return new JythonInterpreterFactory();
    }

    /**
     * LogStream factory
     * @return the bean
     */
    @Bean
    public LogStreamFactory logStreamFactory() {
        return new LogStreamFactory();
    }

    /**
     * Script factory
     * @return the script
     */
    @Bean
    public ScriptFactory scriptFactory() {
        return new ScriptFactoryDefaultImplementation().withExecutor(defaultExecutor());
    }

    /**
     * PlayOnLinux default executor service
     * @return
     */
    private ExecutorService defaultExecutor() {
        return executor;
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }

    /**
     * Set the CLI interface
     * @param enabled Determine if this interface should be used or not
     */
    public void setUseCLIInterface(boolean enabled) {
        this.useCliInterface = enabled;
    }

    /**
     * Use the GTK interface
     * @param useGTKInterface Determine is the interface should be used or not
     */
    public void setUseGTKInterface(boolean useGTKInterface) {
        this.useGTKInterface = useGTKInterface;
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
