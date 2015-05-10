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

import static org.junit.Assert.*;


public class RegistryKeyTest {

    @Test
    public void testRegistryKey_testGetChildWithMultipleArguments() throws Exception {
        RegistryKey a1 = new RegistryKey("A1");
        RegistryKey b1 = new RegistryKey("B1");
        RegistryKey b2 = new RegistryKey("B2");
        RegistryKey c1 = new RegistryKey("C1");
        RegistryKey c2 = new RegistryKey("C2");

        RegistryValue<StringValueType> v1 = new RegistryValue<>("V1", new StringValueType("Content 1"));
        RegistryValue<StringValueType> v2 = new RegistryValue<>("V2", new StringValueType("Content 2"));
        RegistryValue<StringValueType> v3 = new RegistryValue<>("V3", new StringValueType("Content 3"));

        a1.addChild(b1);
        a1.addChild(b2);

        b2.addChild(c1);
        b2.addChild(c2);
        c1.addChild(v1);
        c2.addChild(v2);
        c2.addChild(v3);

        assertEquals("Content 1", ((RegistryValue) a1.getChild("B2", "C1", "V1")).getText());
        assertEquals("Content 2", ((RegistryValue) a1.getChild("B2", "C2", "V2")).getText() );
        assertEquals("Content 3", ((RegistryValue) a1.getChild("B2", "C2", "V3")).getText() );

        assertEquals("C1", a1.getChild("B2", "C1").getName());
    }
}