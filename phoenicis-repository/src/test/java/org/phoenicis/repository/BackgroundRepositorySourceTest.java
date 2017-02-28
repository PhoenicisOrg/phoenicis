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

import org.phoenicis.repository.dto.ApplicationCategoryDTO;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BackgroundRepositorySourceTest {
    private final ExecutorService mockExecutor = mock(ExecutorService.class);
    private final RepositorySource mockApplicationSource = mock(RepositorySource.class);
    private final BackgroundRepositorySource backgroundApplicationsSource = new BackgroundRepositorySource(mockApplicationSource, mockExecutor);
    private List<ApplicationCategoryDTO> mockResults = Arrays.asList(mock(ApplicationCategoryDTO.class), mock(ApplicationCategoryDTO.class));

    @Test
    public void testFetchInstallableApplications_taskIsPassed() {
        doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(mockExecutor).submit(any(Runnable.class));

        when(mockApplicationSource.fetchInstallableApplications()).thenReturn(mockResults);
        backgroundApplicationsSource.fetchInstallableApplications(categoryDTOs -> {}, e -> {});

        verify(mockApplicationSource).fetchInstallableApplications(any(), any());
    }
}