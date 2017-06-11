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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.configuration.localisation.Localisation;
import org.phoenicis.configuration.localisation.PropertiesResourceBundle;
import org.phoenicis.repository.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class LocalRepository implements Repository {
    private final static Logger LOGGER = LoggerFactory.getLogger(LocalRepository.class);

    private static final String CATEGORY_ICON_NAME = "icon.png";

    private final File repositoryDirectory;
    private final ObjectMapper objectMapper;

    private final URI repositorySource;

    private LocalRepository(File repositoryDirectory, URI repositorySource, ObjectMapper objectMapper) {
        this.repositoryDirectory = repositoryDirectory;
        this.objectMapper = objectMapper;
        this.repositorySource = repositorySource;
    }

    private LocalRepository(File repositoryDirectory, ObjectMapper objectMapper) {
        this(repositoryDirectory, repositoryDirectory.toURI(), objectMapper);
    }

    @Override
    public RepositoryDTO fetchInstallableApplications() {

        final File[] categoryDirectories = repositoryDirectory.listFiles();

        if (categoryDirectories == null) {
            return new RepositoryDTO.Builder().build();
        }

        LOGGER.info("Reading directory : " + repositoryDirectory);
        final RepositoryDTO.Builder repositoryDTOBuilder = new RepositoryDTO.Builder()
                .withName(repositoryDirectory.getName()).withCategories(fetchCategories(categoryDirectories));

        final File i18nDirectory = new File(repositoryDirectory, "i18n");
        if (i18nDirectory.exists()) {
            final File[] translationFiles = i18nDirectory
                    .listFiles((dir, name) -> name.endsWith(Locale.getDefault().getLanguage() + ".properties"));
            Properties mergedProperties = new Properties();
            for (File translationFile : translationFiles) {
                try {
                    Properties langProperties = new Properties();
                    langProperties.load(new FileInputStream(translationFile));
                    mergedProperties.putAll(langProperties);
                } catch (IOException e) {
                    LOGGER.error("Could not read translation properties", e);
                }
            }
            repositoryDTOBuilder.withTranslations(new TranslationDTO.Builder()
                    .withLanguage(Locale.getDefault().getLanguage()).withProperties(mergedProperties).build());
            Localisation.setAdditionalTranslations(new PropertiesResourceBundle(mergedProperties));
        }

        return repositoryDTOBuilder.build();
    }

    private List<CategoryDTO> fetchCategories(File[] categoryDirectories) {
        final List<CategoryDTO> results = new ArrayList<>();

        for (File categoryDirectory : categoryDirectories) {
            if (categoryDirectory.isDirectory() && !categoryDirectory.getName().startsWith(".")) {
                final File categoryJson = new File(categoryDirectory, "category.json");

                if (categoryJson.exists()) {
                    final CategoryDTO.Builder categoryDTOBuilder = new CategoryDTO.Builder(
                            unSerializeCategory(categoryJson)).withId(categoryDirectory.getName())
                                    .withApplications(fetchApplications(categoryDirectory));

                    final File categoryIconFile = new File(categoryDirectory, CATEGORY_ICON_NAME);
                    if (categoryIconFile.exists()) {
                        categoryDTOBuilder.withIcon(categoryIconFile.toURI());
                    }

                    CategoryDTO category = categoryDTOBuilder.build();
                    results.add(category);
                }
            }
        }

        Collections.sort(results, Comparator.comparing(CategoryDTO::getName));
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
                File applicationJson = new File(applicationDirectory, "application.json");
                final ApplicationDTO.Builder applicationDTOBuilder = new ApplicationDTO.Builder(
                        unSerializeApplication(applicationJson));

                if (StringUtils.isBlank(applicationDTOBuilder.getName())) {
                    applicationDTOBuilder.withId(applicationDirectory.getName());
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

                ApplicationDTO app = applicationDTOBuilder.build();
                results.add(app);
            }
        }

        Collections.sort(results, Comparator.comparing(ApplicationDTO::getName));
        return results;
    }

    private List<URI> fetchMiniatures(File miniaturesDirectory) throws IOException {

        final File[] miniatureFiles = miniaturesDirectory.listFiles();

        return Arrays.stream(miniatureFiles)
                .filter(miniatureFile -> !miniatureFile.isDirectory() && !miniatureFile.getName().startsWith("."))
                .map(File::toURI).collect(Collectors.toList());
    }

    private List<ResourceDTO> fetchResources(File applicationDirectory) {

        final File[] resources = new File(applicationDirectory, "resources").listFiles();
        if (resources == null) {
            return Collections.emptyList();
        }

        final List<ResourceDTO> results = new ArrayList<>();

        for (File resourceFile : resources) {
            if (!resourceFile.isDirectory() && !resourceFile.getName().startsWith(".")) {
                try {
                    results.add(new ResourceDTO(resourceFile.getName(),
                            IOUtils.toByteArray(new FileInputStream(resourceFile))));
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
            if (scriptDirectory.isDirectory() && !"miniatures".equals(scriptDirectory.getName())
                    && !"resources".equals(scriptDirectory.getName())) {
                final ScriptDTO.Builder scriptDTOBuilder = new ScriptDTO.Builder(
                        unSerializeScript(new File(scriptDirectory, "script.json")));

                scriptDTOBuilder.withScriptSource(repositorySource);

                if (StringUtils.isBlank(scriptDTOBuilder.getScriptName())) {
                    scriptDTOBuilder.withScriptName(scriptDirectory.getName());
                }

                final File scriptFile = new File(scriptDirectory, "script.js");

                if (scriptFile.exists()) {
                    try {
                        scriptDTOBuilder.withScript(new String(IOUtils.toByteArray(new FileInputStream(scriptFile))));
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("repositorySource", repositorySource)
                .append("repositoryDirectory", repositoryDirectory).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocalRepository that = (LocalRepository) o;

        EqualsBuilder builder = new EqualsBuilder();

        builder.append(repositoryDirectory, that.repositoryDirectory);

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(repositoryDirectory);

        return builder.toHashCode();
    }

    public static class Factory {
        private final ObjectMapper objectMapper;

        public Factory(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        public LocalRepository createInstance(File path) {
            return new LocalRepository(path, objectMapper);
        }

        public LocalRepository createInstance(File path, URI source) {
            return new LocalRepository(path, source, objectMapper);
        }
    }
}
