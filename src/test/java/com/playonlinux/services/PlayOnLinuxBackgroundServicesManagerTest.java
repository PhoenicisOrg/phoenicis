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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class PlayOnLinuxBackgroundServicesManagerTest {
    private PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManagerUnderTest;

    @Before
    public void setUp() {
        this.playOnLinuxBackgroundServicesManagerUnderTest = new PlayOnLinuxBackgroundServicesManager();
    }

    @Test
    public void testRegister() throws BackgroundServiceInitializationException {
        BackgroundService mockBackgroundService = mock(BackgroundService.class);
        playOnLinuxBackgroundServicesManagerUnderTest.register(mockBackgroundService);
        verify(mockBackgroundService).start();
    }

    @Test
    public void testUnregister() throws BackgroundServiceInitializationException {
        BackgroundService mockBackgroundService = mock(BackgroundService.class);
        playOnLinuxBackgroundServicesManagerUnderTest.register(mockBackgroundService);
        playOnLinuxBackgroundServicesManagerUnderTest.unregister(mockBackgroundService);
        verify(mockBackgroundService).shutdown();
    }

    @Test
    public void testShutdown() throws BackgroundServiceInitializationException {
        BackgroundService mockBackgroundService = mock(BackgroundService.class);
        BackgroundService mockBackgroundService2 = mock(BackgroundService.class);

        playOnLinuxBackgroundServicesManagerUnderTest.register(mockBackgroundService);
        playOnLinuxBackgroundServicesManagerUnderTest.register(mockBackgroundService2);

        playOnLinuxBackgroundServicesManagerUnderTest.shutdown();

        verify(mockBackgroundService).shutdown();
        verify(mockBackgroundService2).shutdown();
    }

    @Test
    public void testGet() throws BackgroundServiceInitializationException {
        BackgroundServiceTestImplementation mockBackgroundService = mock(BackgroundServiceTestImplementation.class);
        BackgroundServiceTestImplementation mockBackgroundService2 = mock(BackgroundServiceTestImplementation.class);

        playOnLinuxBackgroundServicesManagerUnderTest.register("backgroundId", mockBackgroundService);
        playOnLinuxBackgroundServicesManagerUnderTest.register("backgroundId2", mockBackgroundService2);

        assertEquals(mockBackgroundService,
                playOnLinuxBackgroundServicesManagerUnderTest.getBackgroundService("backgroundId",
                        BackgroundServiceTestImplementation.class));

        assertEquals(mockBackgroundService2,
                playOnLinuxBackgroundServicesManagerUnderTest.getBackgroundService("backgroundId2",
                        BackgroundServiceTestImplementation.class));


    }



    class BackgroundServiceTestImplementation implements BackgroundService {
        @Override
        public void shutdown() {
            // Not needed for the test
        }

        @Override
        public void start() {
            // Not needed for the test
        }
    }
}