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
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BackgroundRepositoryTest {
    private final ExecutorService mockExecutor = mock(ExecutorService.class);
    private final Repository mockRepository = mock(Repository.class);
    private final BackgroundRepository backgroundRepository = new BackgroundRepository(mockRepository, mockExecutor);
    private RepositoryDTO mockResults = new RepositoryDTO.Builder().withTypes(Collections.singletonList(
            new TypeDTO.Builder().withId("Type 1")
                    .withCategories(Arrays.asList(mock(CategoryDTO.class), mock(CategoryDTO.class))).build()))
            .build();

    @Test
    public void testFetchInstallableApplicationsTaskIsPassed() {
        doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(mockExecutor).submit(any(Runnable.class));

        when(mockRepository.fetchInstallableApplications()).thenReturn(mockResults);
        backgroundRepository.fetchInstallableApplications(categoryDTOs -> {
        }, e -> {
        });

        verify(mockRepository).fetchInstallableApplications(any(), any());
    }
}