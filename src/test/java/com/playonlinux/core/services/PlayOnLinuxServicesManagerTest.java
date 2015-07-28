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

package com.playonlinux.core.services;

import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.PlayOnLinuxServicesManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class PlayOnLinuxServicesManagerTest {
    private PlayOnLinuxServicesManager playOnLinuxServicesManagerUnderTest;

    @Before
    public void setUp() {
        this.playOnLinuxServicesManagerUnderTest = new PlayOnLinuxServicesManager();
    }

    @Test
    public void testRegister() throws ServiceInitializationException {
        Service mockService = mock(Service.class);
        playOnLinuxServicesManagerUnderTest.register(mockService);
        verify(mockService).init();
    }

    @Test
    public void testUnregister() throws ServiceInitializationException {
        Service mockService = mock(Service.class);
        playOnLinuxServicesManagerUnderTest.register(mockService);
        playOnLinuxServicesManagerUnderTest.unregister(mockService);
        verify(mockService).shutdown();
    }

    @Test
    public void testShutdown() throws ServiceInitializationException {
        Service mockService = mock(Service.class);
        Service mockService2 = mock(Service.class);

        playOnLinuxServicesManagerUnderTest.register(mockService);
        playOnLinuxServicesManagerUnderTest.register(mockService2);

        playOnLinuxServicesManagerUnderTest.shutdown();

        verify(mockService).shutdown();
        verify(mockService2).shutdown();
    }

    @Test
    public void testGet() throws ServiceInitializationException {
        ServiceTestImplementation mockBackgroundService = mock(ServiceTestImplementation.class);
        ServiceTestImplementation mockBackgroundService2 = mock(ServiceTestImplementation.class);

        playOnLinuxServicesManagerUnderTest.register("backgroundId", mockBackgroundService);
        playOnLinuxServicesManagerUnderTest.register("backgroundId2", mockBackgroundService2);

        assertEquals(mockBackgroundService,
                playOnLinuxServicesManagerUnderTest.getService("backgroundId",
                        ServiceTestImplementation.class));

        assertEquals(mockBackgroundService2,
                playOnLinuxServicesManagerUnderTest.getService("backgroundId2",
                        ServiceTestImplementation.class));


    }



    class ServiceTestImplementation implements Service {
        @Override
        public void shutdown() {
            // Not needed for the test
        }

        @Override
        public void init() {
            // Not needed for the test
        }
    }
}