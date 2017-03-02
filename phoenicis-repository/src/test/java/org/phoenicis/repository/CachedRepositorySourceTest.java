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

package org.phoenicis.repository;

import org.junit.Test;
import org.mockito.Mockito;
import org.phoenicis.repository.dto.RepositoryDTO;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CachedRepositorySourceTest {

    @Test
    public void testFetchInstallableApplications() throws Exception {
        RepositorySource repositorySource = Mockito.mock(RepositorySource.class);
        when(repositorySource.fetchRepositories()).thenReturn(Arrays.asList(
                new RepositoryDTO.Builder()
                        .withName("Repository 1")
                        .build(),
                new RepositoryDTO.Builder()
                        .withName("Repository 2")
                        .build()
        ));

        final RepositorySource cachedSource = new CachedRepositorySource(repositorySource);
        cachedSource.fetchRepositories();
        assertEquals(2, cachedSource.fetchRepositories().size());
        cachedSource.fetchRepositories();
        assertEquals(2, cachedSource.fetchRepositories().size());

        verify(repositorySource, times(1)).fetchRepositories();
    }

}