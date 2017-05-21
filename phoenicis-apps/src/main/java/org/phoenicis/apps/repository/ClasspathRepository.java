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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.apps.dto.ScriptDTO;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class ClasspathRepository implements Repository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathRepository.class);
    private final URI packagePath;
    private final ResourcePatternResolver resourceResolver;
    private final ObjectMapper objectMapper;

    public ClasspathRepository(URI packagePath, ResourcePatternResolver resourceResolver, ObjectMapper objectMapper) {
        this.packagePath = packagePath;
        this.resourceResolver = resourceResolver;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        try {
            final List<CategoryDTO> categoryDTOs = new ArrayList<>();
            Resource[] resources = resourceResolver.getResources(packagePath.toString() + "/*");
            for (Resource resource : resources) {
                final CategoryDTO category = buildCategory(resource.getFilename());
                if (!category.getApplications().isEmpty()) {
                    categoryDTOs.add(category);
                }
            }
            Collections.sort(categoryDTOs, Comparator.comparing(CategoryDTO::getName));
            return categoryDTOs;
        } catch (IOException e) {
            LOGGER.warn("Error while reading resource directory", e);
            return Collections.emptyList();
        }
    }

    private CategoryDTO buildCategory(String categoryFileName) throws IOException {
        final String jsonCategoryFile = packagePath.toString() + "/" + categoryFileName + "/category.json";
        final CategoryDTO categoryDTO = objectMapper.readValue(getClass().getResourceAsStream(jsonCategoryFile),
                CategoryDTO.class);

        try {
            return new CategoryDTO.Builder(categoryDTO)
                    .withIcon(new URI(packagePath.toString() + "/" + categoryFileName + "/icon.png"))
                    .withApplications(buildApplications(categoryFileName)).build();
        } catch (URISyntaxException e) {
            LOGGER.warn("Invalid icon path", e);
            return new CategoryDTO.Builder(categoryDTO).withApplications(buildApplications(categoryFileName)).build();
        }
    }

    private List<ApplicationDTO> buildApplications(String categoryFileName) throws IOException {
        final String categoryScanClassPath = packagePath.toString() + "/" + categoryFileName;
        Resource[] resources = resourceResolver.getResources(categoryScanClassPath + "/*");
        final List<ApplicationDTO> applicationDTOS = new ArrayList<>();

        for (Resource resource : resources) {
            final String fileName = resource.getFilename();
            if (!"icon.png".equals(fileName) && !"category.json".equals(fileName)) {
                final ApplicationDTO application = buildApplication(categoryFileName, fileName);
                if (!application.getScripts().isEmpty()) {
                    applicationDTOS.add(application);
                }
            }
        }

        Collections.sort(applicationDTOS, Comparator.comparing(ApplicationDTO::getName));
        return applicationDTOS;
    }

    private ApplicationDTO buildApplication(String categoryFileName, String applicationFileName) throws IOException {
        final String applicationJsonFile = packagePath.toString() + "/" + categoryFileName + "/" + applicationFileName
                + "/application.json";
        final ApplicationDTO applicationDTO = objectMapper
                .readValue(getClass().getResourceAsStream(applicationJsonFile), ApplicationDTO.class);

        return new ApplicationDTO.Builder(applicationDTO)
                .withScripts(buildScripts(categoryFileName, applicationFileName))
                .withMiniatures(buildMiniatures(categoryFileName, applicationFileName)).build();
    }

    private List<URI> buildMiniatures(String categoryFileName, String applicationFileName) throws IOException {
        final String applicationScanClassPath = packagePath.toString() + "/" + categoryFileName + "/"
                + applicationFileName + "/miniatures/";
        Resource[] resources = resourceResolver.getResources(applicationScanClassPath + "/*");

        return Arrays.stream(resources).map(resource -> {
            final String resourceFile = packagePath.toString() + "/" + categoryFileName + "/" + applicationFileName
                    + "/miniatures/" + resource.getFilename();

            try {
                return getClass().getResource(resourceFile).toURI();
            } catch (URISyntaxException e) {
                return null;
            }
        }).collect(Collectors.toList());
    }

    private List<ScriptDTO> buildScripts(String categoryFileName, String applicationFileName) throws IOException {
        final String applicationScanClassPath = packagePath.toString() + "/" + categoryFileName + "/"
                + applicationFileName;
        Resource[] resources = resourceResolver.getResources(applicationScanClassPath + "/*");
        final List<ScriptDTO> scriptDTOs = new ArrayList<>();

        for (Resource resource : resources) {
            final String fileName = resource.getFilename();
            if (!"resources".equals(fileName) && !"miniatures".equals(fileName)
                    && !"application.json".equals(fileName)) {
                final ScriptDTO script = buildScript(categoryFileName, applicationFileName, fileName);
                scriptDTOs.add(script);
            }
        }

        Collections.sort(scriptDTOs, Comparator.comparing(ScriptDTO::getScriptName));

        return scriptDTOs;
    }

    private ScriptDTO buildScript(String categoryFileName, String applicationFileName, String scriptFileName)
            throws IOException {
        final String scriptJsonFile = packagePath.toString() + "/" + categoryFileName + "/" + applicationFileName + "/"
                + scriptFileName + "/script.json";
        final InputStream scriptJsonInputStream = getClass().getResourceAsStream(scriptJsonFile);
        final InputStream scriptFile = getClass().getResourceAsStream(packagePath.toString() + "/" + categoryFileName
                + "/" + applicationFileName + "/" + scriptFileName + "/script.js");

        if (scriptJsonInputStream == null) {
            return null;
        }

        return new ScriptDTO.Builder(objectMapper.readValue(scriptJsonInputStream, ScriptDTO.class))
                .withScript(new String(IOUtils.toByteArray(scriptFile))).build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("packagePath", packagePath).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClasspathRepository that = (ClasspathRepository) o;

        EqualsBuilder builder = new EqualsBuilder();

        builder.append(packagePath, that.packagePath);

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(packagePath);

        return builder.toHashCode();
    }

    public static class Factory {
        private final ObjectMapper objectMapper;
        private final ResourcePatternResolver resourceResolver;

        public Factory(ObjectMapper objectMapper, ResourcePatternResolver resourceResolver) {
            this.objectMapper = objectMapper;
            this.resourceResolver = resourceResolver;
        }

        public ClasspathRepository createInstance(URI packagePath) {
            return new ClasspathRepository(packagePath, resourceResolver, objectMapper);
        }
    }

}
