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

package org.phoenicis.repository.repositoryTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.repository.RepositoryException;
import org.phoenicis.repository.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClasspathRepository implements Repository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathRepository.class);
    private final String packagePath;
    private final ResourcePatternResolver resourceResolver;
    private final ObjectMapper objectMapper;

    public ClasspathRepository(String packagePath, ResourcePatternResolver resourceResolver,
            ObjectMapper objectMapper) {
        this.packagePath = packagePath;
        this.resourceResolver = resourceResolver;
        this.objectMapper = objectMapper;
    }

    @Override
    public RepositoryDTO fetchInstallableApplications() {
        try {
            final List<TypeDTO> typeDTOS = new ArrayList<>();
            Resource[] resources = resourceResolver.getResources(packagePath + "/*");
            for (Resource resource : resources) {
                final TypeDTO type = buildType(resource.getFilename());
                if (!type.getCategories().isEmpty()) {
                    typeDTOS.add(type);
                }
            }
            typeDTOS.sort(Comparator.comparing(TypeDTO::getName));

            final RepositoryDTO.Builder repositoryDTOBuilder = new RepositoryDTO.Builder()
                    .withName("classpath repository").withTypes(typeDTOS);
            return repositoryDTOBuilder.build();
        } catch (IOException e) {
            LOGGER.warn("Error while reading resource directory", e);
            return new RepositoryDTO.Builder().build();
        }
    }

    @Override
    public boolean isSafe() {
        return true;
    }

    private TypeDTO buildType(String typeFileName) throws RepositoryException {
        try {
            final String jsonTypePath = packagePath + "/" + typeFileName + "/type.json";
            final URL jsonTypeFile = getClass().getResource(jsonTypePath);
            if (jsonTypeFile != null) {
                final TypeDTO typeDTO = objectMapper.readValue(jsonTypeFile, TypeDTO.class);

                try {
                    return new TypeDTO.Builder(typeDTO)
                            .withIcon(new URI(packagePath + "/" + typeFileName + "/icon.png")).withId(typeFileName)
                            .withCategories(buildCategories(typeFileName)).build();
                } catch (URISyntaxException e) {
                    LOGGER.warn("Invalid icon path", e);
                    return new TypeDTO.Builder(typeDTO).withId(typeFileName)
                            .withCategories(buildCategories(typeFileName)).build();
                }
            } else {
                LOGGER.debug(String.format("type.json %s for classpath repository does not exist", jsonTypePath));
                return new TypeDTO.Builder().build();
            }
        } catch (IOException e) {
            throw new RepositoryException("Could not build type", e);
        }
    }

    private List<CategoryDTO> buildCategories(String typeFileName) throws RepositoryException {
        try {
            final String categoryScanClassPath = packagePath + "/" + typeFileName;
            Resource[] resources = resourceResolver.getResources(categoryScanClassPath + "/*");
            final List<CategoryDTO> categoryDTOS = new ArrayList<>();

            for (Resource resource : resources) {
                final String fileName = resource.getFilename();
                if (!"icon.png".equals(fileName) && !"category.json".equals(fileName)) {
                    final CategoryDTO category = buildCategory(typeFileName, fileName);
                    if (!category.getApplications().isEmpty()) {
                        categoryDTOS.add(category);
                    }
                }
            }

            categoryDTOS.sort(Comparator.comparing(CategoryDTO::getName));
            return categoryDTOS;
        } catch (IOException e) {
            throw new RepositoryException("Could not build categories", e);
        }
    }

    private CategoryDTO buildCategory(String typeFileName, String categoryFileName) throws RepositoryException {
        try {
            final String jsonCategoryPath = packagePath + "/" + typeFileName + "/" + categoryFileName
                    + "/category.json";
            final URL jsonCategoryFile = getClass().getResource(jsonCategoryPath);
            if (jsonCategoryFile != null) {
                final CategoryDTO categoryDTO = objectMapper.readValue(jsonCategoryFile, CategoryDTO.class);

                try {
                    return new CategoryDTO.Builder(categoryDTO)
                            .withIcon(new URI(packagePath + "/" + typeFileName + "/" + categoryFileName + "/icon.png"))
                            .withId(categoryFileName)
                            .withApplications(buildApplications(typeFileName, categoryFileName))
                            .build();
                } catch (URISyntaxException e) {
                    LOGGER.warn("Invalid icon path", e);
                    return new CategoryDTO.Builder(categoryDTO).withId(categoryFileName)
                            .withApplications(buildApplications(typeFileName, categoryFileName)).build();
                }
            } else {
                LOGGER.debug(String.format("category.json %s for classpath repository does not exist",
                        jsonCategoryPath));
                return new CategoryDTO.Builder().build();
            }
        } catch (IOException e) {
            throw new RepositoryException("Could not build category", e);
        }
    }

    private List<ApplicationDTO> buildApplications(String typeFileName, String categoryFileName)
            throws RepositoryException {
        try {
            final String categoryScanClassPath = packagePath + "/" + typeFileName + "/" + categoryFileName;
            Resource[] resources = resourceResolver.getResources(categoryScanClassPath + "/*");
            final List<ApplicationDTO> applicationDTOS = new ArrayList<>();

            for (Resource resource : resources) {
                final String fileName = resource.getFilename();
                if (!"icon.png".equals(fileName) && !"category.json".equals(fileName)) {
                    final ApplicationDTO application = buildApplication(typeFileName, categoryFileName, fileName);
                    if (!application.getScripts().isEmpty()) {
                        applicationDTOS.add(application);
                    }
                }
            }

            applicationDTOS.sort(Comparator.comparing(ApplicationDTO::getName));
            return applicationDTOS;
        } catch (IOException e) {
            throw new RepositoryException("Could not build applications", e);
        }
    }

    private ApplicationDTO buildApplication(String typeFileName, String categoryFileName, String applicationFileName)
            throws RepositoryException {
        try {
            final String applicationDirectory = packagePath + "/" + typeFileName + "/" + categoryFileName + "/"
                    + applicationFileName;
            File applicationJson = new File(applicationDirectory, "application.json");

            final ApplicationDTO applicationDTO = objectMapper
                    .readValue(getClass().getResourceAsStream(applicationJson.getAbsolutePath()), ApplicationDTO.class);

            return new ApplicationDTO.Builder(applicationDTO).withId(applicationFileName)
                    .withScripts(buildScripts(typeFileName, categoryFileName, applicationFileName))
                    .withMiniatures(buildMiniatures(typeFileName, categoryFileName, applicationFileName)).build();
        } catch (IOException e) {
            throw new RepositoryException("Could not build application", e);
        }
    }

    private List<URI> buildMiniatures(String typeFileName, String categoryFileName, String applicationFileName)
            throws RepositoryException {
        try {
            final String applicationScanClassPath = packagePath + "/" + typeFileName + "/" + categoryFileName + "/"
                    + applicationFileName
                    + "/miniatures/";
            Resource[] resources = resourceResolver.getResources(applicationScanClassPath + "/*");

            return Arrays.stream(resources).map(resource -> {
                final String resourceFile = packagePath + "/" + typeFileName + "/" + categoryFileName + "/"
                        + applicationFileName
                        + "/miniatures/" + resource.getFilename();

                try {
                    return getClass().getResource(resourceFile).toURI();
                } catch (URISyntaxException e) {
                    return null;
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RepositoryException("Could not build miniatures", e);
        }
    }

    private List<ScriptDTO> buildScripts(String typeFileName, String categoryFileName, String applicationFileName)
            throws RepositoryException {
        try {
            final String applicationScanClassPath = packagePath + "/" + typeFileName + "/" + categoryFileName + "/"
                    + applicationFileName;
            Resource[] resources = resourceResolver.getResources(applicationScanClassPath + "/*");
            final List<ScriptDTO> scriptDTOs = new ArrayList<>();

            for (Resource resource : resources) {
                final String fileName = resource.getFilename();
                if (!"resources".equals(fileName) && !"miniatures".equals(fileName)
                        && !"application.json".equals(fileName)) {
                    final ScriptDTO script = buildScript(typeFileName, categoryFileName, applicationFileName, fileName);
                    scriptDTOs.add(script);
                }
            }

            scriptDTOs.sort(Comparator.comparing(ScriptDTO::getScriptName));

            return scriptDTOs;
        } catch (IOException e) {
            throw new RepositoryException("Could not build scripts", e);
        }
    }

    private ScriptDTO buildScript(String typeFileName, String categoryFileName, String applicationFileName,
            String scriptFileName)
            throws RepositoryException {
        try {
            final String scriptJsonFile = packagePath + "/" + typeFileName + "/" + categoryFileName + "/"
                    + applicationFileName + "/"
                    + scriptFileName + "/script.json";
            final InputStream scriptJsonInputStream = getClass().getResourceAsStream(scriptJsonFile);
            final InputStream scriptFile = getClass().getResourceAsStream(
                    packagePath + "/" + typeFileName + "/" + categoryFileName + "/" + applicationFileName + "/"
                            + scriptFileName + "/script.js");

            if (scriptJsonInputStream == null) {
                return null;
            }

            return new ScriptDTO.Builder(objectMapper.readValue(scriptJsonInputStream, ScriptDTO.class))
                    .withId(scriptFileName)
                    .withScript(new String(IOUtils.toByteArray(scriptFile))).build();
        } catch (IOException e) {
            throw new RepositoryException("Could not build script", e);
        }
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

        final ClasspathRepository that = (ClasspathRepository) o;
        return new EqualsBuilder()
                .append(packagePath, that.packagePath)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(packagePath)
                .toHashCode();
    }

    public static class Factory {
        private final ObjectMapper objectMapper;
        private final ResourcePatternResolver resourceResolver;

        public Factory(ObjectMapper objectMapper, ResourcePatternResolver resourceResolver) {
            this.objectMapper = objectMapper;
            this.resourceResolver = resourceResolver;
        }

        public ClasspathRepository createInstance(String packagePath) {
            return new ClasspathRepository(packagePath, resourceResolver, objectMapper);
        }
    }

}
