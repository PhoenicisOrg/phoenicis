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

package com.playonlinux.core.webservice;

import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;

public class DownloadExceptionTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testDownloadException_stringConstructor() throws DownloadException {
        expectedEx.expect(DownloadException.class);
        expectedEx.expectMessage("Exception message");

        throw new DownloadException("Exception message");
    }

    @Test
    public void testDownloadException_stringThrowableConstructor() throws DownloadException {
        final Throwable mockException = mock(Throwable.class);

        expectedEx.expect(DownloadException.class);
        expectedEx.expectMessage("Exception message");
        expectedEx.expectCause(new ArgumentMatcher<Throwable>() {
            @Override
            public boolean matches(Object o) {
                return o == mockException;
            }
        });
        throw new DownloadException("Exception message", mockException);
    }


}