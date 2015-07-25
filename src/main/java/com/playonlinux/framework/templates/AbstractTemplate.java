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

package com.playonlinux.framework.templates;

import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.log.LogStreamFactory;
import com.playonlinux.core.scripts.ScriptTemplate;
import com.playonlinux.core.python.PythonAttribute;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

@Scan
public abstract class AbstractTemplate implements ScriptTemplate {
    @PythonAttribute
    private String title;

    @Inject
    static LogStreamFactory logFactory;

    public void echo(String message) {
        OutputStream outputstream;
        if(title != null) {
            try {
                outputstream = logFactory.getLogger(title);
            } catch (IOException e) {
                outputstream = System.out;
            }
        } else {
            outputstream = System.out;
        }

        PrintWriter printWriter = new PrintWriter(outputstream);
        printWriter.println(message);
        printWriter.flush();
    }
}
