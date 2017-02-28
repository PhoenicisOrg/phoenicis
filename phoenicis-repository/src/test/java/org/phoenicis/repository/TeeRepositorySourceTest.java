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

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;


public class TeeRepositorySourceTest {
    @Test
    public void testFetchInstallableApplications_onlyCategoriesNoCollapse_numberOfResultIsCorrect() {
        final RepositorySource leftSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 1")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .build()
        );

        final RepositorySource rightSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 3")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 4")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 5")
                        .build()
        );

        final RepositorySource teeSource = new TeeRepositorySource(leftSource, rightSource);
        assertEquals(5, teeSource.fetchInstallableApplications().size());
    }


    @Test
    public void testFetchInstallableApplications_onlyCategoriesCollapse_numberOfResultIsCorrect() {
        final RepositorySource leftSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 1")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .build()
        );

        final RepositorySource rightSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 4")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 5")
                        .build()
        );

        final RepositorySource teeSource = new TeeRepositorySource(leftSource, rightSource);
        assertEquals(4, teeSource.fetchInstallableApplications().size());
    }


    @Test
    public void testFetchInstallableApplications_categoriesAndAppsCollapse_numberOfResultIsCorrect() {
        final RepositorySource leftSource = () -> Arrays.asList(
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
        );

        final RepositorySource rightSource = () -> Arrays.asList(
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
        );

        final RepositorySource teeSource = new TeeRepositorySource(leftSource, rightSource);
        assertEquals(3, teeSource.getCategory(Collections.singletonList("Category 2")).getApplications().size());
    }


}