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

package com.playonlinux.injection;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

@Scan
public class InjectorTest extends AbstractConfigFile {

    @Inject
    static String checkInjectedString;

    @Inject
    static File unmappedDependecy;

    @Bean
    String injectedString() {
        return "Injection is working!";
    }

    @Test
    public void testInjector_InjectAString_StringIsInjected() throws InjectionException {
        this.setStrictLoadingPolicy(false);
        this.load();
        assertEquals(this.injectedString(), checkInjectedString);
    }

    @Test(expected=InjectionException.class)
    public void testInjector_InjectAStringWithStrictPolicyAndUnmappedDependency_ThrowsException()
            throws InjectionException {
        this.setStrictLoadingPolicy(true);
        this.load();
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }
}