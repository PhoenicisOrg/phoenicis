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

import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.InjectionException;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.ui.api.Controller;

/**
 * Main PlayOnLinux app
 */
@Scan
public class PlayOnLinuxApp {
    @Inject
    static Controller controller;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ServiceManager serviceManager;

    /**
     * Start the application instance
     * @param args system arguments
     */
    public void start(String[] args) throws PlayOnLinuxException {
        try(PlayOnLinuxConfig playOnLinuxConfig = new PlayOnLinuxConfig()) {
            if (args.length > 0 && "--cli".equals(args[0])) {
                playOnLinuxConfig.setUseCLIInterface(true);
            }
            if (args.length > 0 && "--gtk".equals(args[0])) {
                playOnLinuxConfig.setUseGTKInterface(true);
            }

            playOnLinuxConfig.load();
            playOnLinuxContext.initLogger();
            serviceManager.init();

            controller.startApplication();

            serviceManager.shutdown();
        } catch (InjectionException e) {
            throw new PlayOnLinuxException("Fatal error: Unable to inject dependencies", e);
        }
    }

    /**
     * Main methods
     * @param args system arguments
     * @throws PlayOnLinuxException If any errors occur
     */
    public static void main(String[] args) throws PlayOnLinuxException {
        PlayOnLinuxApp application =  new PlayOnLinuxApp();
        application.start(args);
    }

}
