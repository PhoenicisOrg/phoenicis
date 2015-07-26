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

package com.playonlinux.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public final class ExeAnalyser {
    private ExeAnalyser() {
        // Utility class
    }

    /**
     * Checks if the file is a 64bits executable
     * @param file the file to analyse
     * @return true if the file is a 64bit executable. False otherwise
     */
    public static boolean is64Bits(File file) throws IOException {
        try(InputStream inputStream = new FileInputStream(file)) {
            int state = 1;
            for(int c = inputStream.read(); c != -1; c = inputStream.read()) {
                if(state == 1 && c == 0x50) {
                    state = 2;
                    continue;
                }
                if(state == 2 && c == 0x45) {
                    state = 3;
                    continue;
                } else if(state == 2) {
                    state = 1;
                }

                if(state == 3) {
                    if(c == 0x4C) {
                        return false;
                    } else if(c == 0x64){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
