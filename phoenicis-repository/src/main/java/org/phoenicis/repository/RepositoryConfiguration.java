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

package org.phoenicis.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.phoenicis.multithreading.MultithreadingConfiguration;
import org.phoenicis.repository.location.ClasspathRepositoryLocation;
import org.phoenicis.repository.location.GitRepositoryLocation;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.BackgroundRepository;
import org.phoenicis.repository.repositoryTypes.ClasspathRepository;
import org.phoenicis.repository.repositoryTypes.LocalRepository;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.phoenicis.tools.ToolsConfiguration;
import org.phoenicis.tools.files.FileUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RepositoryConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryConfiguration.class);

    @Value("${application.repository.forceIncompatibleOperatingSystems:false}")
    private boolean enforceUncompatibleOperatingSystems;

    @Value("${application.user.cache}")
    private String cacheDirectoryPath;

    @Value("${application.repository.list}")
    private String repositoryListPath;

    @Autowired
    private MultithreadingConfiguration multithreadingConfiguration;

    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Autowired
    private FileUtilities fileUtilities;

    @Bean
    public RepositoryManager repositoryManager() {
        RepositoryManager repositoryManager = new DefaultRepositoryManager(
                multithreadingConfiguration.appsExecutorService(),
                enforceUncompatibleOperatingSystems,
                toolsConfiguration,
                cacheDirectoryPath,
                fileUtilities,
                localRepositoryFactory(),
                classPathRepositoryFactory(),
                backgroundRepositoryFactory());

        // set initial repositories
        repositoryManager.addRepositories(this.loadRepositoryLocations().toArray(new RepositoryLocation[0]));

        return repositoryManager;
    }

    public void saveRepositories(List<RepositoryLocation<? extends Repository>> repositoryLocations) {
        try {
            this.objectMapper().writeValue(new File(repositoryListPath), repositoryLocations);
        } catch (IOException e) {
            LOGGER.error("Couldn't save repository location list", e);
        }
    }

    public List<RepositoryLocation<? extends Repository>> loadRepositoryLocations() {
        List<RepositoryLocation<? extends Repository>> result = new ArrayList<>();

        File repositoryListFile = new File(repositoryListPath);

        if (repositoryListFile.exists()) {
            try {
                result = this.objectMapper().readValue(new File(repositoryListPath),
                        TypeFactory.defaultInstance().constructParametricType(List.class, RepositoryLocation.class));
            } catch (IOException e) {
                LOGGER.error("Couldn't load repository location list", e);
            }
        } else {
            try {
                result.add(new GitRepositoryLocation.Builder()
                        .withGitRepositoryUri(new URL("https://github.com/PlayOnLinux/Scripts").toURI())
                        .withBranch("master").build());
                result.add(new GitRepositoryLocation.Builder()
                        .withGitRepositoryUri(new URL("https://github.com/PlayOnLinux/Oldwares").toURI())
                        .withBranch("master").build());
                result.add(new ClasspathRepositoryLocation("/org/phoenicis/repository"));
            } catch (URISyntaxException | MalformedURLException e) {
                LOGGER.error("Couldn't create default repository location list", e);
            }
        }

        return result;
    }

    @Bean
    ClasspathRepository.Factory classPathRepositoryFactory() {
        return new ClasspathRepository.Factory(objectMapper(), new PathMatchingResourcePatternResolver());
    }

    @Bean
    LocalRepository.Factory localRepositoryFactory() {
        return new LocalRepository.Factory(objectMapper());
    }

    @Bean
    BackgroundRepository.Factory backgroundRepositoryFactory() {
        return new BackgroundRepository.Factory();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
