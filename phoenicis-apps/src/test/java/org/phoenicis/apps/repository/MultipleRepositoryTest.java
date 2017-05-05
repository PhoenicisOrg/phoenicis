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

package org.phoenicis.apps.repository;

import org.phoenicis.apps.dto.CategoryDTO;
import org.junit.Test;
import org.phoenicis.apps.repository.MultipleRepository;
import org.phoenicis.apps.repository.Repository;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class MultipleRepositoryTest {
    @Test
    public void testWithEmptyList_emptySetIsReturned() {
        final MultipleRepository multipleRepository = new MultipleRepository();
        assertEquals(0, multipleRepository.fetchInstallableApplications().size());
    }

    @Test
    public void testWithThreeSources_threeResults() {
        final Repository firstSource = () -> Collections.singletonList(
                new CategoryDTO.Builder()
                        .withName("Category 1")
                        .build()
        );

        final Repository secondSource = () -> Collections.singletonList(
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .build()
        );

        final Repository thirdSource = () -> Collections.singletonList(
                new CategoryDTO.Builder()
                        .withName("Category 3")
                        .build()
        );


        final MultipleRepository multipleRepository = new MultipleRepository(firstSource, secondSource, thirdSource);
        assertEquals(3, multipleRepository.fetchInstallableApplications().size());
    }
}