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
 * Represents the definition of the implementation of a given service. This
 * class holds the interface name of the services and the chosen implementation
 */
public class ServiceImplementationDefinition {
    private final Class<? extends Service> interfaces;

    private final Class<? extends Service> implementation;

    public ServiceImplementationDefinition(Class<? extends Service> interfaces,
            Class<? extends Service> implementation) {
        if (!interfaces.isAssignableFrom(implementation)) {
            throw new IllegalArgumentException(String.format("%s is not an assignable form of %s", interfaces.getName(),
                    implementation.getName()));
        }
        this.interfaces = interfaces;
        this.implementation = implementation;
    }

    /**
     * Getter
     * 
     * @return The interface that needs to be implemented
     */
    public Class<? extends Service> getImplementation() {
        return implementation;
    }

    /**
     * Getter
     * 
     * @return The actual implementation of the interface
     */
    public Class<? extends Service> getInterfaces() {
        return interfaces;
    }
}
