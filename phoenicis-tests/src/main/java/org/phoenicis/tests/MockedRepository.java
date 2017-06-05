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

import org.apache.commons.io.IOUtils;
import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.phoenicis.repository.repositoryTypes.TeeRepository;

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
            final ScriptDTO scriptDTO = new ScriptDTO.Builder().withScriptName("Wine")
                    .withCompatibleOperatingSystems(Arrays.asList(OperatingSystem.LINUX, OperatingSystem.MACOSX))
                    .withScript(wineScript()).build();
            final ApplicationDTO applicationDTO = new ApplicationDTO.Builder().withName("Engines")
                    .withScripts(Arrays.asList(scriptDTO)).build();
            final CategoryDTO categoryDTO = new CategoryDTO.Builder().withApplications(Arrays.asList(applicationDTO))
                    .withName("Functions").build();
            return new RepositoryDTO.Builder().withCategories(Collections.singletonList(categoryDTO)).build();
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
