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

package org.phoenicis.cli;

import com.github.jankroken.commandline.CommandLineParser;
import com.github.jankroken.commandline.OptionStyle;

import java.lang.reflect.InvocationTargetException;

public class PhoenicisCLI {
    public static void main(String[] args) {
        final PhoenicisCLI phoenicisCLI = new PhoenicisCLI();
        phoenicisCLI.run(args);
    }

    private void run(String[] args) {
        try {
            final CLIController arguments = CommandLineParser.parse(CLIController.class, args, OptionStyle.SIMPLE);

            arguments.close();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
