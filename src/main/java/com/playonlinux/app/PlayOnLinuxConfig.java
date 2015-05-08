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

import com.playonlinux.injection.AbstractConfigFile;
import com.playonlinux.injection.Bean;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.impl.javafx.ControllerJavaFXImplementation;
import com.playonlinux.domain.PlayOnLinuxError;

import java.io.IOException;

@SuppressWarnings("unused")
public class PlayOnLinuxConfig extends AbstractConfigFile  {

    @Bean
    public Controller controller() {
        return new ControllerJavaFXImplementation();
    }

    @Bean
    public EventHandler eventHandler() {
            return new EventHandlerPlayOnLinuxImplementation();
    }

    @Bean
    public PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxError, IOException {
        return new PlayOnLinuxContext();
    }

    @Bean
    public PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager() {
        return new PlayOnLinuxBackgroundServicesManager();
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }
}
