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

package com.phoenicis.javafx.views.common;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class HtmlTemplateTest {

    private File testTemplate;

    @Before
    public void setUp() throws IOException {
        testTemplate = File.createTempFile("test", "html");
        testTemplate.deleteOnExit();

        try (OutputStream outputStream = new FileOutputStream(testTemplate)) {
            outputStream.write(("<html>" + "<head>" + "<title>{{title}}</title>" + "</head>" + "<body>"
                    + "Content: {{content}}" + "</bod>" + "</html>").getBytes());

            outputStream.flush();
        }
    }

    @Test
    public void testRender_replaceTwoValues_valuesAreReplaced() throws Exception {
        HtmlTemplate htmlTemplate = new HtmlTemplate(new FileInputStream(testTemplate));

        String expected = "<html>" + "<head>" + "<title>Title</title>" + "</head>" + "<body>" + "Content: Content"
                + "</bod>" + "</html>";
        assertEquals(expected, htmlTemplate.render(new Object() {
            String title = "Title";
            String content = "Content";
        }));
    }

}