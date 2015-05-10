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

package com.playonlinux.wine.registry;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.*;

public class RegistryParserTest {

    @Test
    public void testRegistryParser_parseSimpleFile_testKeyTree() throws IOException, ParseException {
        File temporaryFile = File.createTempFile("registry", "test");

        FileOutputStream outputStream = new FileOutputStream(temporaryFile);

        byte[] bytes = ("[A\\\\B\\\\C] 1430602912\n" +
                "\"C\"=\"1\"\n" +
                "\"D\"=\"2\"\n" +
                "\"E\"=\"3\"").getBytes();

        outputStream.write(bytes);
        outputStream.flush();

        RegistryParser registryParser = new RegistryParser(temporaryFile, "Temporary");
        RegistryKey node = registryParser.parseFile();

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
    public void testRegistryParser_parseSimpleFile_testStringValues() throws IOException, ParseException {
        File temporaryFile = File.createTempFile("registry", "test");

        FileOutputStream outputStream = new FileOutputStream(temporaryFile);

        byte[] bytes = ("[A] 1430602912\n" +
                "   \n" +
                "\"C=D\"=\"Content1\"\n" +
                "\"D\\\"\"=\"Content\\\"2\"\n" +
                "\"E\"=\"3\\\\\"\n" +
                "\"\\\\G\"=\"3\"\n" +
                "\"test\"=\":\"\n"
        ).getBytes();

        outputStream.write(bytes);
        outputStream.flush();

        RegistryParser registryParser = new RegistryParser(temporaryFile, "Temporary");
        RegistryKey node = registryParser.parseFile();

        RegistryKey levelOneNode = (RegistryKey) node.getChild(0);

        assertEquals(5, levelOneNode.getChildren().size());
        assertEquals("C=D", levelOneNode.getChild(0).getName());
        assertEquals("D\"", levelOneNode.getChild(1).getName());
        assertEquals("E", levelOneNode.getChild(2).getName());
        assertEquals("\\G", levelOneNode.getChild(3).getName());
        assertEquals("test", levelOneNode.getChild(4).getName());

        assertEquals("Content1", ((RegistryValue) levelOneNode.getChild(0)).getText());
        assertEquals("Content\"2", ((RegistryValue) levelOneNode.getChild(1)).getText());
        assertEquals("3\\", ((RegistryValue) levelOneNode.getChild(2)).getText());
        assertEquals(":", ((RegistryValue) levelOneNode.getChild(4)).getText());
    }


    @Test
    public void testParse_realRegFile_testObjectPopulated() throws IOException, ParseException {
        File registryFile = new File(this.getClass().getResource("user.reg").getFile());

        RegistryParser registryParser = new RegistryParser(registryFile, "User");
        RegistryKey parsedFile = registryParser.parseFile();

        assertEquals(1541, parsedFile.toString().split("\n").length);

        System.out.println(parsedFile);
    }

}