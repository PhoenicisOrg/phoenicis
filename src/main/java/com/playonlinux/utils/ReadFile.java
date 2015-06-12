/*
 * Copyright (C) 2015 SLAGMOLEN Raphaël
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

package com.playonlinux.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

public class ReadFile {
    
    public ReadFile() {
        // Should never be used as object
    }
    
    public static String readFile(String file) {
        String text;
        try {
            FileInputStream content = new FileInputStream(new File(file));
            StringWriter writer = new StringWriter();
            IOUtils.copy(content, writer, "UTF-8");
            text = writer.toString();
            content.close();
        } catch (FileNotFoundException e) {
            text = "No file found\n" + e;
        } catch (IOException e) {
            text = "Error during reading the file\n" +e;
        }
        return text;
    }
}
