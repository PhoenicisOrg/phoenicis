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

package com.playonlinux.core.services.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class PlayOnLinuxServicesManager implements ServiceManager {
    private static final Logger LOGGER = Logger.getLogger(ServiceManager.class);

    private final Map<String, Service> backgroundServices;

    public PlayOnLinuxServicesManager() {
        backgroundServices = new HashMap<>();
    }

    @Override
    public String register(Service service) throws ServiceInitializationException {
        final String backgroundName = String.valueOf(service.hashCode());
        register(backgroundName, service);
        return backgroundName;
    }

    @Override
    public void register(String backgroundServiceName, Service service) throws ServiceInitializationException {
        backgroundServices.put(backgroundServiceName, service);
        service.init();
    }

    @Override
    public synchronized void shutdown() {
        backgroundServices.values().forEach(Service::shutdown);
    }

    @Override
    public synchronized void unregister(Service service) {
        final List<String> keysToRemove = new ArrayList<>();
        backgroundServices.keySet().stream().filter(backgroundServiceKey -> service.equals(backgroundServices.get(backgroundServiceKey))).forEach(backgroundServiceKey -> {
            keysToRemove.add(backgroundServiceKey);
            service.shutdown();
        });
        keysToRemove.forEach(backgroundServices::remove);
    }

    @Override
    public <T extends Service> T getService(String backgroundServiceName, Class<T> backgroundServiceType) {
        final Service service = backgroundServices.get(backgroundServiceName);

        if(backgroundServiceType.isAssignableFrom(service.getClass())) {
            return (T) service;
        } else {
            throw new IllegalArgumentException("The selected type is not valid");
        }
    }

    @Override
    public <T extends Service> T getService(Class<T> backgroundServiceType) {
        for(Service service : backgroundServices.values()) {
            if(backgroundServiceType.isAssignableFrom(service.getClass())) {
                return (T) service;
            }
        }

        throw new IllegalArgumentException("The selected type ("+backgroundServiceType.getName()+") is not valid. Existing services: "+backgroundServices.values().toString());
    }

    @Override
    public boolean containsService(String serviceName) {
        return backgroundServices.containsKey(serviceName);
    }

    @Override
    public void init(ServiceManagerConfiguration serviceManagerConfiguration) throws ServiceInitializationException {
        for(ServiceImplementationDefinition serviceImplementationDefinition: serviceManagerConfiguration) {
            try {
                LOGGER.info(String.format("Registering component service: %s -> %s",
                        serviceImplementationDefinition.getInterfaces(),
                        serviceImplementationDefinition.getImplementation()));
                this.register(serviceImplementationDefinition.getInterfaces().getName(),
                        serviceImplementationDefinition.getImplementation().newInstance());
            } catch (ReflectiveOperationException e) {
                throw new ServiceInitializationException(e);
            }
        }
    }


}
