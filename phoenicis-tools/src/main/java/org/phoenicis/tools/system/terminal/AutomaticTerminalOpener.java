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

import org.phoenicis.configuration.security.Safe;
import org.phoenicis.tools.system.OperatingSystemFetcher;

import java.util.Map;
import java.util.Optional;

@Safe
public class AutomaticTerminalOpener implements TerminalOpener {
    private final TerminalOpener terminalOpener;

    public AutomaticTerminalOpener(
            TerminalOpenerFactory terminalOpenerFactory,
            OperatingSystemFetcher operatingSystemFetcher,
            Optional<String> terminalCommand) {
        switch (operatingSystemFetcher.fetchCurrentOperationSystem()) {
            case LINUX:
                terminalOpener = terminalOpenerFactory.createInstance(LinuxTerminalOpener.class);
                break;
            case MACOSX:
                terminalOpener = terminalOpenerFactory.createInstance(MacOSTerminalOpener.class);
                break;
            case FREEBSD:
            default:
                terminalOpener = new TerminalOpener() {
                    @Override
                    public void openTerminal(String workingDirectory, Map<String, String> environmentVariables) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void setTerminalCommand(Optional<String> terminalCommand) {
                        throw new UnsupportedOperationException();
                    }
                };
        }
        if (terminalCommand.isPresent()) {
            terminalOpener.setTerminalCommand(terminalCommand);
        }
    }

    @Override
    public void openTerminal(String workingDirectory, Map<String, String> environmentVariables) {
        terminalOpener.openTerminal(workingDirectory, environmentVariables);
    }

    @Override
    public void setTerminalCommand(Optional<String> terminalCommand) {
        // currently unused
    }
}
