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

package org.phoenicis.apps;

import org.phoenicis.apps.dto.CategoryDTO;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class MultipleRepositorySourceTest {
    @Test
    public void testWithEmptyList_emptySetIsReturned() {
        final MultipleRepositorySource multipleRepositorySource = new MultipleRepositorySource();
        assertEquals(0, multipleRepositorySource.fetchInstallableApplications().size());
    }

    @Test
    public void testWithThreeSources_threeResults() {
        final RepositorySource firstSource = () -> Collections.singletonList(
                new CategoryDTO.Builder()
                        .withName("Category 1")
                        .build()
        );

        final RepositorySource secondSource = () -> Collections.singletonList(
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .build()
        );

        final RepositorySource thirdSource = () -> Collections.singletonList(
                new CategoryDTO.Builder()
                        .withName("Category 3")
                        .build()
        );


        final MultipleRepositorySource multipleRepositorySource = new MultipleRepositorySource(firstSource, secondSource, thirdSource);
        assertEquals(3, multipleRepositorySource.fetchInstallableApplications().size());
    }
}