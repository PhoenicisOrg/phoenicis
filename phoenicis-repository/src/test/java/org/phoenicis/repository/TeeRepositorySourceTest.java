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

import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.junit.Test;
import org.phoenicis.repository.dto.RepositoryDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TeeRepositorySourceTest {
    @Test
    public void testFetchInstallableApplications_onlyRepositoriesNoCollapse_numberOfResultIsCorrect() {
        final RepositorySource leftSource = () -> Arrays.asList(
                new RepositoryDTO.Builder()
                        .withName("Repository 1")
                        .build(),
                new RepositoryDTO.Builder()
                        .withName("Repository 2")
                        .build()
        );

        final RepositorySource rightSource = () -> Arrays.asList(
                new RepositoryDTO.Builder()
                        .withName("Repository 3")
                        .build(),
                new RepositoryDTO.Builder()
                        .withName("Repository 4")
                        .build(),
                new RepositoryDTO.Builder()
                        .withName("Repository 5")
                        .build()
        );

        final RepositorySource teeSource = new TeeRepositorySource(leftSource, rightSource);
        assertEquals(5, teeSource.fetchRepositories().size());
    }


    @Test
    public void testFetchInstallableApplications_onlyRepositoriesCollapse_numberOfResultIsCorrect() {
        final RepositorySource leftSource = () -> Arrays.asList(
                new RepositoryDTO.Builder()
                        .withName("Repository 1")
                        .build(),
                new RepositoryDTO.Builder()
                        .withName("Repository 2")
                        .build()
        );

        final RepositorySource rightSource = () -> Arrays.asList(
                new RepositoryDTO.Builder()
                        .withName("Repository 2")
                        .build(),
                new RepositoryDTO.Builder()
                        .withName("Repository 4")
                        .build(),
                new RepositoryDTO.Builder()
                        .withName("Repository 5")
                        .build()
        );

        final RepositorySource teeSource = new TeeRepositorySource(leftSource, rightSource);
        assertEquals(4, teeSource.fetchRepositories().size());
    }

    @Test
    public void testFetchInstallableApplications_repositoriesAndCategoriesAndAppsCollapse_numberOfResultIsCorrect() {
        final RepositorySource leftSource = () -> Arrays.asList(
                new RepositoryDTO.Builder()
                    .withName("Repository 1")
                    .withCategories(Arrays.asList(
                        new CategoryDTO.Builder()
                                .withName("Category 1")
                                .build(),
                        new CategoryDTO.Builder()
                                .withName("Category 2")
                                .withApplications(
                                        Arrays.asList(
                                                new ApplicationDTO.Builder()
                                                        .withName("Application 1")
                                                        .build(),
                                                new ApplicationDTO.Builder()
                                                        .withName("Application 2")
                                                        .build()
                                        ))
                                .build()
                    )).build()
        );

        final RepositorySource rightSource = () -> Arrays.asList(
                new RepositoryDTO.Builder()
                    .withName("Repository 1")
                    .withCategories(Arrays.asList(
                        new CategoryDTO.Builder()
                                .withName("Category 2")
                                .withApplications(
                                        Arrays.asList(
                                                new ApplicationDTO.Builder()
                                                        .withName("Application 1")
                                                        .build(),
                                                new ApplicationDTO.Builder()
                                                        .withName("Application 3")
                                                        .build()
                                        ))
                                .build(),
                        new CategoryDTO.Builder()
                                .withName("Category 4")
                                .build(),
                        new CategoryDTO.Builder()
                                .withName("Category 5")
                                .build()
                    )).build()
        );

        final RepositorySource teeSource = new TeeRepositorySource(leftSource, rightSource);
        List<String> path = new ArrayList<>();
        path.add("Repository 1");
        path.add("Category 2");
        assertEquals(3, teeSource.getCategory(path).getApplications().size());
    }


}