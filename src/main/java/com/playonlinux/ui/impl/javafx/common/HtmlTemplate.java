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

package com.playonlinux.ui.impl.javafx.common;

import org.python.core.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlTemplate {
    private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
    private final File templateFile;
    private final Charset encoding;

    public HtmlTemplate(File templateFile) {
        this(templateFile, DEFAULT_ENCODING);
    }

    public HtmlTemplate(File templateFile, Charset encoding) {
        this.templateFile = templateFile;
        this.encoding = encoding;
    }

    public String render(String... replacements) throws IOException {
        String fileContent = new String(Files.readAllBytes(Paths.get(templateFile.getAbsolutePath())), encoding);

        return String.format(fileContent, replacements);
    }
}
