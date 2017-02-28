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

import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.repository.RepositorySource;
import org.phoenicis.repository.TeeRepositorySource;
import org.phoenicis.repository.dto.ApplicationCategoryDTO;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockedRepositorySource extends TeeRepositorySource {
    MockedRepositorySource(RepositorySource realApplicationSource) {
        super(new MockApplicationSource(), realApplicationSource);
    }

    private static class MockApplicationSource implements RepositorySource {
        @Override
        public List<ApplicationCategoryDTO> fetchInstallableApplications() {
            return Collections.singletonList(
                    new ApplicationCategoryDTO.Builder()
                            .withApplications(
                                    Arrays.asList(
                                            new ApplicationDTO.Builder()
                                                    .withName("Engines")
                                                    .withScripts(Arrays.asList(
                                                            new ScriptDTO.Builder()
                                                                    .withScriptName("Wine")
                                                                    .withCompatibleOperatingSystems(Arrays.asList(OperatingSystem.LINUX, OperatingSystem.MACOSX))
                                                                    .withScript(wineScript())
                                                                    .build()
                                                    ))
                                                    .build()
                                    )
                            )
                            .withName("Functions")
                            .build()
            );
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
