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

package com.playonlinux.services;

import java.util.*;

public final class PlayOnLinuxBackgroundServicesManager implements BackgroundServiceManager {
    private final Map<String, BackgroundService> backgroundServices;

    public PlayOnLinuxBackgroundServicesManager() {
        backgroundServices = new HashMap<>();
    }

    public String register(BackgroundService backgroundService) throws BackgroundServiceInitializationException {
        final String backgroundName = String.valueOf(backgroundService.hashCode());
        register(backgroundName, backgroundService);
        return backgroundName;
    }

    @Override
    public void register(String backgroundServiceName, BackgroundService backgroundService) throws BackgroundServiceInitializationException {
        backgroundServices.put(backgroundServiceName, backgroundService);
        backgroundService.start();
    }

    public void shutdown() {
        backgroundServices.values().forEach(BackgroundService::shutdown);
    }

    synchronized public void unregister(BackgroundService backgroundService) {
        final List<String> keysToRemove = new ArrayList<>();
        for(String backgroundServiceKey: backgroundServices.keySet()) {
            if(backgroundService.equals(backgroundServices.get(backgroundServiceKey))) {
                keysToRemove.add(backgroundServiceKey);
                backgroundService.shutdown();
            }
        }
        for(String keyToRemove: keysToRemove) {
            backgroundServices.remove(keyToRemove);
        }
    }

    @Override
    public <T extends BackgroundService> T getBackgroundService(String backgroundServiceName, Class<T> backgroundServiceType) {
        final BackgroundService backgroundService = backgroundServices.get(backgroundServiceName);

        if(backgroundServiceType.isAssignableFrom(backgroundService.getClass())) {
            return (T) backgroundService;
        } else {
            throw new IllegalArgumentException("The selected type is not valid");
        }
    }

    @Override
    public <T extends BackgroundService> T getBackgroundService(Class<T> backgroundServiceType) {
        for(BackgroundService backgroundService: backgroundServices.values()) {
            if(backgroundServiceType.isAssignableFrom(backgroundService.getClass())) {
                return (T) backgroundService;
            }
        }

        throw new IllegalArgumentException("The selected type is not valid");
    }

    @Override
    public boolean containsService(String serviceName) {
        return backgroundServices.containsKey(serviceName);
    }

    @Override
    public void init() throws BackgroundServiceInitializationException {
        final AutoStartedBackgroundServiceFinder autoStartedBackgroundServiceFinder = new AutoStartedBackgroundServiceFinder();
        final Map<String, Class<?>> autoLoadedBackgroundServices
                = autoStartedBackgroundServiceFinder.findClasses();

        for(String backgroundServiceKey: autoLoadedBackgroundServices.keySet()) {
            try {
                this.register(backgroundServiceKey,
                        (BackgroundService) autoLoadedBackgroundServices.get(backgroundServiceKey).newInstance());
            } catch (ReflectiveOperationException e) {
                throw new BackgroundServiceInitializationException(e);
            }
        }
    }


}
