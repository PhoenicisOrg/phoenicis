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

/**
 * Represents a Service
 * Services are central PlayOnLinux components.
 * They are all registered on startup, or one by one during integration tests
 */
public interface Service {
    /**
     * Shutdown the service
     */
    void shutdown();

    /**
     * Initialize the service
     * @throws ServiceInitializationException If any error occurs during the initialization of the service
     */
    void init() throws ServiceInitializationException;
}
