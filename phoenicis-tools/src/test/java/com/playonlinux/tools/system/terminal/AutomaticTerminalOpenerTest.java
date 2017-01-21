package com.playonlinux.tools.system.terminal;

import com.phoenicis.entities.OperatingSystem;
import com.playonlinux.tools.system.OperatingSystemFetcher;
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