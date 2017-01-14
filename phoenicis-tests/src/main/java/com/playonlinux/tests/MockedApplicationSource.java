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
