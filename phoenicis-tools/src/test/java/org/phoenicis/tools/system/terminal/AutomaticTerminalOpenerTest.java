/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.tools.system.terminal;

import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.tools.system.OperatingSystemFetcher;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AutomaticTerminalOpenerTest {
    private final TerminalOpenerFactory mockTerminalOpenerFactory = mock(TerminalOpenerFactory.class);
    private final OperatingSystemFetcher mockOperatingSystemFetcher = mock(OperatingSystemFetcher.class);
    private final TerminalOpener mockLinuxTerminalOpener = mock(TerminalOpener.class);
    private final TerminalOpener mockMacTerminalOpener = mock(TerminalOpener.class);

    @Before
    public void setUp() {
        when(mockTerminalOpenerFactory.createInstance(LinuxTerminalOpener.class)).thenReturn(mockLinuxTerminalOpener);
        when(mockTerminalOpenerFactory.createInstance(MacOSTerminalOpener.class)).thenReturn(mockMacTerminalOpener);
    }

    @Test
    public void openTerminal_testOSX() {
        when(mockOperatingSystemFetcher.fetchCurrentOperationSystem()).thenReturn(OperatingSystem.MACOSX);
        final AutomaticTerminalOpener automaticTerminalOpener = new AutomaticTerminalOpener(mockTerminalOpenerFactory, mockOperatingSystemFetcher);

        final Map<String, String> map = new HashMap<>();
        automaticTerminalOpener.openTerminal("workingDirectory", map);

        verify(mockMacTerminalOpener).openTerminal("workingDirectory", map);
        verifyNoMoreInteractions(mockLinuxTerminalOpener, mockMacTerminalOpener);
    }


    @Test
    public void openTerminal_testLinux() {
        when(mockOperatingSystemFetcher.fetchCurrentOperationSystem()).thenReturn(OperatingSystem.LINUX);
        final AutomaticTerminalOpener automaticTerminalOpener = new AutomaticTerminalOpener(mockTerminalOpenerFactory, mockOperatingSystemFetcher);

        final Map<String, String> map = new HashMap<>();
        automaticTerminalOpener.openTerminal("workingDirectory", map);

        verify(mockLinuxTerminalOpener).openTerminal("workingDirectory", map);
        verifyNoMoreInteractions(mockLinuxTerminalOpener, mockMacTerminalOpener);
    }

}