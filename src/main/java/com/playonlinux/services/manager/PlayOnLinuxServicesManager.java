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

package com.playonlinux.services.manager;

import java.util.*;

public final class PlayOnLinuxServicesManager implements ServiceManager {
    private final Map<String, Service> backgroundServices;

    public PlayOnLinuxServicesManager() {
        backgroundServices = new HashMap<>();
    }

    public String register(Service service) throws ServiceInitializationException {
        final String backgroundName = String.valueOf(service.hashCode());
        register(backgroundName, service);
        return backgroundName;
    }

    @Override
    public void register(String backgroundServiceName, Service service) throws ServiceInitializationException {
        backgroundServices.put(backgroundServiceName, service);
        service.start();
    }

    public void shutdown() {
        backgroundServices.values().forEach(Service::shutdown);
    }

    synchronized public void unregister(Service service) {
        final List<String> keysToRemove = new ArrayList<>();
        for(String backgroundServiceKey: backgroundServices.keySet()) {
            if(service.equals(backgroundServices.get(backgroundServiceKey))) {
                keysToRemove.add(backgroundServiceKey);
                service.shutdown();
            }
        }
        for(String keyToRemove: keysToRemove) {
            backgroundServices.remove(keyToRemove);
        }
    }

    @Override
    public <T extends Service> T getBackgroundService(String backgroundServiceName, Class<T> backgroundServiceType) {
        final Service service = backgroundServices.get(backgroundServiceName);

        if(backgroundServiceType.isAssignableFrom(service.getClass())) {
            return (T) service;
        } else {
            throw new IllegalArgumentException("The selected type is not valid");
        }
    }

    @Override
    public <T extends Service> T getBackgroundService(Class<T> backgroundServiceType) {
        for(Service service : backgroundServices.values()) {
            if(backgroundServiceType.isAssignableFrom(service.getClass())) {
                return (T) service;
            }
        }

        throw new IllegalArgumentException("The selected type is not valid");
    }

    @Override
    public boolean containsService(String serviceName) {
        return backgroundServices.containsKey(serviceName);
    }

    @Override
    public void init() throws ServiceInitializationException {
        final AutoStartedServiceFinder autoStartedBackgroundServiceFinder = new AutoStartedServiceFinder();
        final Map<String, Class<?>> autoLoadedBackgroundServices
                = autoStartedBackgroundServiceFinder.findClasses();

        for(String backgroundServiceKey: autoLoadedBackgroundServices.keySet()) {
            try {
                this.register(backgroundServiceKey,
                        (Service) autoLoadedBackgroundServices.get(backgroundServiceKey).newInstance());
            } catch (ReflectiveOperationException e) {
                throw new ServiceInitializationException(e);
            }
        }
    }


}
