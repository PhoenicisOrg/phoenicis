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

/**
 * Manages the background service
 */
public interface BackgroundServiceManager {
    /**
     * Register a background service. The class name will be use has a custom name
     * @param backgroundService The background service to register
     * @return the name of the background service
     */
    String register(BackgroundService backgroundService) throws BackgroundServiceInitializationException;

    /**
     * Register a background service with a custom name
     * @param backgroundServiceName The name of the Background service
     * @param backgroundService The background service to register
     */
    void register(String backgroundServiceName, BackgroundService backgroundService) throws BackgroundServiceInitializationException;

    /**
     * Shutdown all the background services
     */
    void shutdown();

    /**
     * Unregister a background service
     * @param backgroundService The background service to register
     */
    void unregister(BackgroundService backgroundService);

    /**
     * Get a background service
     * @param backgroundServiceName The name of the background service
     * @param backgroundServiceType The type of the background service
     * @param <T> The type of the background service
     * @return The background service
     */
    <T extends BackgroundService> T getBackgroundService(String backgroundServiceName, Class<T> backgroundServiceType);

    /**
     * Checks if the background manager currently contains the given service
     * @param serviceName The service to test
     * @return true if the background manager currently contains the service. False if it does not
     */
    boolean containsService(String serviceName);

    /**
     * Initialize the background service manager
     */
    void init() throws BackgroundServiceInitializationException;

    <T extends BackgroundService> T getBackgroundService(Class<T> installedApplicationsClass);
}
