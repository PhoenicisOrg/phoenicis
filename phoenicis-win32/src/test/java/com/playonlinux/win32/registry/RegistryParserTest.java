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

package com.playonlinux.win32.registry;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class RegistryParserTest {

    @Test
    public void testRegistryParser_parseSimpleFile_testKeyTree() throws IOException {
        File temporaryFile = File.createTempFile("registry", "test");
        temporaryFile.deleteOnExit();

        try (FileOutputStream outputStream = new FileOutputStream(temporaryFile)) {
            byte[] bytes = ("[A\\\\B\\\\C] 1430602912\n" + "\"C\"=\"1\"\n" + "\"D\"=\"2\"\n" + "\"E\"=\"3\"")
                    .getBytes();

            outputStream.write(bytes);
            outputStream.flush();
        }

        RegistryParser registryParser = new RegistryParser();
        RegistryKey node = registryParser.parseFile(temporaryFile, "Temporary");

        RegistryKey levelOneNode = (RegistryKey) node.getChild(0);
        RegistryKey levelTwoNode = (RegistryKey) levelOneNode.getChild(0);
        RegistryKey levelThreeNode = (RegistryKey) levelTwoNode.getChild(0);

        assertEquals("Temporary", node.getName());
        assertEquals("A", levelOneNode.getName());
        assertEquals(1, node.getChildren().size());

        assertEquals("B", levelTwoNode.getName());
        assertEquals(1, levelOneNode.getChildren().size());

        assertEquals("C", levelThreeNode.getName());
        assertEquals(1, levelTwoNode.getChildren().size());
    }

    @Test
    public void testParse_realRegFile_testObjectPopulated() {
        File registryFile = new File(this.getClass().getResource("user.reg").getFile());

        RegistryParser registryParser = new RegistryParser();
        RegistryKey parsedFile = registryParser.parseFile(registryFile, "User");

        assertEquals(1542, parsedFile.toString().split("\n").length);
    }

    @Test
    public void testRegistryParser_wineBug37575_valueIsCorrectlyParsed() throws IOException {
        File temporaryFile = File.createTempFile("registry", "test");
        RegistryParser registryParser = new RegistryParser();
        temporaryFile.deleteOnExit();

        try (FileOutputStream outputStream = new FileOutputStream(temporaryFile)) {
            byte[] bytes = ("[Software\\\\Wine\\\\DllOverrides] 1431283548\n" + "\"*d3dx9_24\"=\"native, builtin\\0\"\n"
                    + "\"*d3dx9_25\"=\"native, builtin\\0\"\n" + "\"*d3dx9_26\"=\"native, builtin\\0\"").getBytes();

            outputStream.write(bytes);
            outputStream.flush();
        }

        AbstractRegistryNode registryNode = registryParser.parseFile(temporaryFile, "Temporary")
                .getChild("Software", "Wine", "DllOverrides", "*d3dx9_24");
        RegistryValue<?> registryValue = null;
        if (registryNode instanceof RegistryValue) {
            registryValue = (RegistryValue<?>) registryNode;
        }

        assertEquals("*d3dx9_24", registryValue.getName());
        assertEquals("native, builtin", registryValue.getText());
    }

}