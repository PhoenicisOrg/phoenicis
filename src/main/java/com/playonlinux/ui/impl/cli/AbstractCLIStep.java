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

package com.playonlinux.ui.impl.cli;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractCLIStep {
    abstract void printMessage();

    abstract String defineDefaultValue();

    private String defineInputMessage() {
        return "Your choice";
    }

    public final void display() {
        printMessage();
        System.out.println("\n");
        String input;

        if (defineDefaultValue() == null) {
            input = System.console()
                    .readLine(String.format("< %s [%s] > ", defineInputMessage(), defineDefaultValue()));
            if (StringUtils.isBlank(input)) {
                input = defineDefaultValue();
            }
        } else {
            input = System.console().readLine(String.format("< %s >", defineInputMessage()));
        }
    }

}
