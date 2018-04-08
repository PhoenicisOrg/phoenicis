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

package org.phoenicis.tests;

import org.phoenicis.repository.dto.*;
import org.phoenicis.repository.types.Repository;
import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.repository.types.TeeRepository;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class MockedRepository extends TeeRepository {
    MockedRepository(Repository realRepository) {
        super(new MockRepository(), realRepository);
    }

    private static class MockRepository implements Repository {
        @Override
        public RepositoryDTO fetchInstallableApplications() {
            return new RepositoryDTO.Builder()
                    .withTypes(Collections.singletonList(
                            new TypeDTO.Builder()
                                    .withId("type_1")
                                    .withCategories(Collections.singletonList(
                                            new CategoryDTO.Builder()
                                                    .withApplications(Arrays.asList(
                                                            new ApplicationDTO.Builder()
                                                                    .withId("engines")
                                                                    .withScripts(Arrays.asList(
                                                                            new ScriptDTO.Builder()
                                                                                    .withScriptName("Wine")
                                                                                    .withCompatibleOperatingSystems(
                                                                                            Arrays.asList(
                                                                                                    OperatingSystem.LINUX,
                                                                                                    OperatingSystem.MACOSX))
                                                                                    .withScript(wineScript())
                                                                                    .build()))
                                                                    .build()))
                                                    .withName("Functions")
                                                    .build()))
                                    .build()))
                    .build();
        }

        private String wineScript() {
            try {
                return IOUtils.toString(getClass().getResourceAsStream("wine.js"), "UTF-8");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
