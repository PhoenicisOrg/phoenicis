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

import com.playonlinux.services.BackgroundServiceManager;
import com.playonlinux.ui.Controller;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.InjectionException;
import com.playonlinux.injection.Scan;

@Scan
public class PlayOnLinuxApp {
    @Inject
    static Controller controller;

    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static BackgroundServiceManager backgroundServiceManager;

    public void start(String[] args) throws InjectionException {
        try(PlayOnLinuxConfig playOnLinuxConfig = new PlayOnLinuxConfig()) {
            if (args.length > 0 && "--cli".equals(args[0])) {
                playOnLinuxConfig.setUseCLIInterface(true);
            }
            if (args.length > 0 && "--gtk".equals(args[0])) {
                playOnLinuxConfig.setUseGTKInterface(true);
            }

            playOnLinuxConfig.load();
            playOnLinuxContext.initLogger();
            backgroundServiceManager.init();

            controller.startApplication();
        } catch (PlayOnLinuxException e) {
            System.out.println("Unable to load PlayOnLinux");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InjectionException {
        PlayOnLinuxApp application =  new PlayOnLinuxApp();
        application.start(args);
    }

}
