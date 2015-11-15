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

package com.playonlinux.app;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.playonlinux.core.utils.ReplacableProperties;

public class ReplacablePropertiesTest {

    private ReplacableProperties properties;
    private final String PROPERTIES_FILENAME = "com/playonlinux/app/playonlinuxtest.properties";

    @Before
    public void setUp() throws IOException {
        this.properties = new ReplacableProperties();
        this.properties.load(ReplacablePropertiesTest.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME));
    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }
    @Test
    public void testGetProperty_TestSimpleValue_CorrectValueIsReturned() {
        assertEquals("test", getProperty("property1"));
        assertEquals("test2", getProperty("property2"));
        assertEquals("test3", getProperty("property3"));
    }

    @Test
    public void testGetProperty_TestVariableComposedValue_CorrectValueIsReturned() {
        assertEquals("test3 test2", getProperty("property4"));
        assertEquals("test3 test2 test3", getProperty("property5"));
    }

    @Test
    public void testGetProperty_TestVMPropertiesComposedValue_CorrectValueIsReturned() {
        assertEquals('/', getProperty("home_directory").getBytes()[0]);
    }
}