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

package com.playonlinux.tests;

import com.phoenicis.entities.OperatingSystem;
import com.playonlinux.apps.ApplicationsSource;
import com.playonlinux.apps.TeeApplicationsSource;
import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ScriptDTO;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockedApplicationSource extends TeeApplicationsSource {
    MockedApplicationSource(ApplicationsSource realApplicationSource) {
        super(new MockApplicationSource(), realApplicationSource);
    }

    private static class MockApplicationSource implements ApplicationsSource {
        @Override
        public List<CategoryDTO> fetchInstallableApplications() {
            return Collections.singletonList(
                    new CategoryDTO.Builder()
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
