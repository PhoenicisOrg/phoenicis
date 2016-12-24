/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.apps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ResourceDTO;
import com.playonlinux.apps.dto.ScriptDTO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class LocalApplicationsSource implements ApplicationsSource {
    private final static Logger LOGGER = LoggerFactory.getLogger(LocalApplicationsSource.class);

    private static final String CATEGORY_ICON_NAME = "icon.png";
    private final String repositoryDirectory;
    private final ObjectMapper objectMapper;

    public LocalApplicationsSource(String repositoryDirectory, ObjectMapper objectMapper) {
        this.repositoryDirectory = repositoryDirectory;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        final File repositoryDirectoryFile = new File(repositoryDirectory);
        final File[] categoryDirectories = repositoryDirectoryFile.listFiles();

        if (categoryDirectories == null) {
            return Collections.emptyList();
        }

        return fetchCategories(categoryDirectories);
    }

    private List<CategoryDTO> fetchCategories(File[] categoryDirectories) {
        final List<CategoryDTO> results = new ArrayList<>();

        for (File categoryDirectory : categoryDirectories) {
            if (categoryDirectory.isDirectory() && !categoryDirectory.getName().startsWith(".")) {
                final File categoryFile = new File(categoryDirectory, "category.json");

                final CategoryDTO.Builder categoryDTOBuilder = new CategoryDTO.Builder(unSerializeCategory(categoryFile))
                        .withName(categoryDirectory.getName())
                        .withApplications(fetchApplications(categoryDirectory));

                final File categoryIconFile = new File(categoryDirectory, CATEGORY_ICON_NAME);
                if (categoryIconFile.exists()) {
                    try {
                        categoryDTOBuilder.withIcon(IOUtils.toByteArray(new FileInputStream(categoryIconFile)));
                    } catch (IOException e) {
                        LOGGER.warn("No icon found for the category " + categoryDirectory.getName(), e);
                    }
                }

                results.add(categoryDTOBuilder.build());
            }
        }

        return results;
    }

    private List<ApplicationDTO> fetchApplications(File categoryDirectory) {
        final File[] applicationDirectories = categoryDirectory.listFiles();
        if (applicationDirectories == null) {
            return Collections.emptyList();
        }

        final List<ApplicationDTO> results = new ArrayList<>();

        for (File applicationDirectory : applicationDirectories) {
            if (applicationDirectory.isDirectory()) {
                final ApplicationDTO.Builder applicationDTOBuilder = new ApplicationDTO.Builder(
                        unSerializeApplication(new File(applicationDirectory, "application.json")));

                if (StringUtils.isBlank(applicationDTOBuilder.getName())) {
                    applicationDTOBuilder.withName(applicationDirectory.getName());
                }

                final File miniaturesDirectory = new File(applicationDirectory, "miniatures");

                if (miniaturesDirectory.exists() && miniaturesDirectory.isDirectory()) {
                    try {
                        applicationDTOBuilder.withMiniatures(fetchMiniatures(miniaturesDirectory));
                    } catch (IOException e) {
                        LOGGER.warn("Unable to read miniatures", e);
                    }
                }

                applicationDTOBuilder.withScripts(fetchScripts(applicationDirectory));
                applicationDTOBuilder.withResources(fetchResources(applicationDirectory));
                results.add(applicationDTOBuilder.build());
            }
        }

        return results;
    }

    private List<byte[]> fetchMiniatures(File miniaturesDirectory) throws IOException {
        final List<byte[]> miniatures = new ArrayList<>();
        final File[] miniatureFiles = miniaturesDirectory.listFiles();

        if (miniatureFiles != null) {
            for (File miniatureFile : miniatureFiles) {
                if (!miniatureFile.isDirectory() && !miniatureFile.getName().startsWith(".")) {
                    if ("main.png".equals(miniatureFile.getName())) {
                        miniatures.add(0, IOUtils.toByteArray(new FileInputStream(miniatureFile)));
                    } else {
                        miniatures.add(IOUtils.toByteArray(new FileInputStream(miniatureFile)));
                    }
                }
            }
        }
        return miniatures;
    }

    private List<ResourceDTO> fetchResources(File applicationDirectory) {

        final File[] resources = new File(applicationDirectory, "resources").listFiles();
        if (resources == null) {
            return Collections.emptyList();
        }

        final List<ResourceDTO> results = new ArrayList<>();

        for (File resourceFile : resources) {
            if(!resourceFile.isDirectory() && !resourceFile.getName().startsWith(".")) {
                try {
                    results.add(new ResourceDTO(resourceFile.getName(), IOUtils.toByteArray(new FileInputStream(resourceFile))));
                } catch (IOException ignored) {

                }
            }
        }

        return results;
    }

    private List<ScriptDTO> fetchScripts(File applicationDirectory) {
        final File[] scriptDirectories = applicationDirectory.listFiles();
        if (scriptDirectories == null) {
            return Collections.emptyList();
        }

        final List<ScriptDTO> results = new ArrayList<>();

        for (File scriptDirectory : scriptDirectories) {
            if (scriptDirectory.isDirectory()
                    && !"miniatures".equals(scriptDirectory.getName())
                    && !"resources".equals(scriptDirectory.getName())) {
                final ScriptDTO.Builder scriptDTOBuilder = new ScriptDTO.Builder(
                        unSerializeScript(new File(scriptDirectory, "script.json")));

                if (StringUtils.isBlank(scriptDTOBuilder.getScriptName())) {
                    scriptDTOBuilder.withScriptName(scriptDirectory.getName());
                }

                final File scriptFile = new File(scriptDirectory, "script.js");

                if (scriptFile.exists()) {
                    try {
                        scriptDTOBuilder.withScript(
                                new String(IOUtils.toByteArray(new FileInputStream(scriptFile)))
                        );
                    } catch (IOException e) {
                        LOGGER.warn("Script not found", e);
                    }
                }

                results.add(scriptDTOBuilder.build());
            }
        }

        return results;
    }

    private CategoryDTO unSerializeCategory(File jsonFile) {
        try {
            return objectMapper.readValue(jsonFile, CategoryDTO.class);
        } catch (IOException e) {
            LOGGER.debug("JSON file not found", e);
            return new CategoryDTO.Builder().build();
        }
    }

    private ScriptDTO unSerializeScript(File jsonFile) {
        try {
            return objectMapper.readValue(jsonFile, ScriptDTO.class);
        } catch (IOException e) {
            LOGGER.debug("JSON file not found");
            return new ScriptDTO.Builder().build();
        }
    }

    private ApplicationDTO unSerializeApplication(File jsonFile) {
        try {
            return objectMapper.readValue(jsonFile, ApplicationDTO.class);
        } catch (IOException e) {
            LOGGER.debug("JSON file not found", e);
            return new ApplicationDTO.Builder().build();
        }
    }

    static class Factory {
        private final ObjectMapper objectMapper;

        Factory(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        public LocalApplicationsSource createInstance(String path) {
            return new LocalApplicationsSource(path, objectMapper);
        }
    }
}
