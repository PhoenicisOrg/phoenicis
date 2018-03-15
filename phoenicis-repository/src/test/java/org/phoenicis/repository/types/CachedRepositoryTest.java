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

package org.phoenicis.repository.types;

import org.junit.Test;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.dto.TypeDTO;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CachedRepositoryTest {

    @Test
    public void testFetchInstallableApplications() throws Exception {
        Repository repository = mock(Repository.class);
        when(repository
                .fetchInstallableApplications())
                        .thenReturn(
                                new RepositoryDTO.Builder()
                                        .withTypes(Collections.singletonList(
                                                new TypeDTO.Builder()
                                                        .withId("Type 1")
                                                        .withCategories(
                                                                Arrays.asList(
                                                                        new CategoryDTO.Builder().withId("Category 1")
                                                                                .build(),
                                                                        new CategoryDTO.Builder().withId("Category 2")
                                                                                .build()))
                                                        .build()))
                                        .build());

        final Repository cachedSource = new CachedRepository(repository);
        cachedSource.fetchInstallableApplications();
        assertEquals(2, cachedSource.fetchInstallableApplications().getTypes().get(0).getCategories().size());
        cachedSource.fetchInstallableApplications();
        assertEquals(2, cachedSource.fetchInstallableApplications().getTypes().get(0).getCategories().size());

        verify(repository, times(1)).fetchInstallableApplications();
    }

}