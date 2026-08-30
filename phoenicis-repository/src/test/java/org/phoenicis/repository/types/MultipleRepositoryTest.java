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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MultipleRepositoryTest {
    private static class ExposedMergeableRepository extends MergeableRepository {
        @Override
        public RepositoryDTO fetchInstallableApplications() {
            return null;
        }

        List<URI> mergeMiniaturesForTest(List<URI> leftMiniatures, List<URI> rightMiniatures) {
            return mergeMiniatures(leftMiniatures, rightMiniatures);
        }
    }

    @Test
    public void testWithEmptyListEmptySetIsReturned() {
        final MultipleRepository multipleRepository = new MultipleRepository();
        assertEquals(null, multipleRepository.fetchInstallableApplications());
    }

    @Test
    public void testWithThreeSourcesThreeResults() {
        final Repository firstSource = () -> new RepositoryDTO.Builder().withTypes(Collections.singletonList(
                new TypeDTO.Builder()
                        .withId("Type 1")
                        .withCategories(Collections.singletonList(
                                new CategoryDTO.Builder().withId("Category 1").build()))
                        .build()))
                .build();

        final Repository secondSource = () -> new RepositoryDTO.Builder().withTypes(Collections.singletonList(
                new TypeDTO.Builder()
                        .withId("Type 1")
                        .withCategories(Collections.singletonList(
                                new CategoryDTO.Builder().withId("Category 2").build()))
                        .build()))
                .build();

        final Repository thirdSource = () -> new RepositoryDTO.Builder().withTypes(Collections.singletonList(
                new TypeDTO.Builder().withId("Type 1")
                        .withCategories(Collections.singletonList(
                                new CategoryDTO.Builder().withId("Category 3").build()))
                        .build()))
                .build();

        final MultipleRepository multipleRepository = new MultipleRepository(firstSource, secondSource, thirdSource);
        assertEquals(3, multipleRepository.fetchInstallableApplications().getTypes().get(0).getCategories().size());
    }

    @Test
    public void testConstructorMakesDefensiveCopyOfRepositoriesList() {
        final Repository source = () -> new RepositoryDTO.Builder().withTypes(Collections.singletonList(
                new TypeDTO.Builder()
                        .withId("Type 1")
                        .withCategories(Collections.singletonList(
                                new CategoryDTO.Builder().withId("Category 1").build()))
                        .build()))
                .build();

        final List<Repository> repositories = new ArrayList<>();
        repositories.add(source);

        final MultipleRepository multipleRepository = new MultipleRepository(repositories);
        repositories.clear();

        assertEquals(1, multipleRepository.size());
        assertEquals(1, multipleRepository.fetchInstallableApplications().getTypes().get(0).getCategories().size());
    }

    @Test
    public void testMergeMiniaturesRemovesDuplicateUrisAndPreservesPriority() {
        final ExposedMergeableRepository mergeableRepository = new ExposedMergeableRepository();

        final URI leftUri = URI.create("https://example.invalid/same-image.png");
        final URI rightUniqueUri = URI.create("https://example.invalid/other-image.png");

        final List<URI> result = mergeableRepository.mergeMiniaturesForTest(
                Collections.singletonList(leftUri),
                java.util.Arrays.asList(leftUri, rightUniqueUri));

        assertEquals(2, result.size());
        assertEquals(leftUri, result.get(0));
        assertEquals(rightUniqueUri, result.get(1));
    }
}
