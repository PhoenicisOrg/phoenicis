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

package com.playonlinux.contexts;

import java.util.Arrays;
import java.util.Iterator;

import com.playonlinux.core.services.manager.ServiceImplementationDefinition;
import com.playonlinux.core.services.manager.ServiceManagerConfiguration;
import com.playonlinux.engines.wine.DefaultWineVersionsManager;
import com.playonlinux.engines.wine.WineVersionEntitiesProvider;
import com.playonlinux.engines.wine.WineVersionManager;

/**
 * Loads all the component required test wine version manager
 */
public class WineVersionServicesContext implements ServiceManagerConfiguration {

    @Override
    public synchronized Iterator<ServiceImplementationDefinition> iterator() {
        return Arrays.asList(
                new ServiceImplementationDefinition(WineVersionManager.class, DefaultWineVersionsManager.class),
                new ServiceImplementationDefinition(WineVersionEntitiesProvider.class, WineVersionEntitiesProvider.class)
        ).iterator();
    }
}
