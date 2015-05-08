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

import com.playonlinux.api.Controller;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.InjectionException;
import com.playonlinux.domain.CancelException;
import com.playonlinux.domain.PlayOnLinuxError;

import java.io.IOException;

@Component
public class PlayOnLinuxApp {

    @Inject
    static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    @Inject
    static Controller controller;

    public void start() throws InjectionException {
        PlayOnLinuxConfig playOnLinuxConfig = new PlayOnLinuxConfig();
        playOnLinuxConfig.load();

        controller.startApplication();
    }

    public static void main(String [] args) throws CancelException, InterruptedException,
            PlayOnLinuxError, IOException, InjectionException {
        PlayOnLinuxApp application =  new PlayOnLinuxApp();
        application.start();

        playOnLinuxBackgroundServicesManager.shutdown();
    }

}
