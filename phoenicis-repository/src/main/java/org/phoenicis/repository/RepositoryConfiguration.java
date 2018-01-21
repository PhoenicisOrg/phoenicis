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

import org.phoenicis.configuration.PhoenicisGlobalConfiguration;
import org.phoenicis.multithreading.MultithreadingConfiguration;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
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
    private PhoenicisGlobalConfiguration phoenicisGlobalConfiguration;

    @Autowired
    private FileUtilities fileUtilities;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${application.repository.location.loader.name:filesystemLocationLoader}")
    private String repositoryLocationLoaderName;

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
        repositoryManager.addRepositories(this.repositoryLocationLoader().loadRepositoryLocations().toArray(new RepositoryLocation[0]));

        return repositoryManager;
    }

    @Bean
    public RepositoryLocationLoader repositoryLocationLoader() {
        return new DynamicRepositoryLocationLoader(applicationContext, repositoryLocationLoaderName);
    }

    @Bean
    public RepositoryLocationLoader filesystemLocationLoader() {
        return new FilesystemJsonRepositoryLocationLoader(repositoryListPath,
                phoenicisGlobalConfiguration.objectMapper());
    }

    /**
     * restores the default settings
     */
    public void restoreDefault() {
        new File(repositoryListPath).deleteOnExit();
    }

    @Bean
    ClasspathRepository.Factory classPathRepositoryFactory() {
        return new ClasspathRepository.Factory(phoenicisGlobalConfiguration.objectMapper(),
                new PathMatchingResourcePatternResolver());
    }

    @Bean
    LocalRepository.Factory localRepositoryFactory() {
        return new LocalRepository.Factory(phoenicisGlobalConfiguration.objectMapper());
    }

    @Bean
    BackgroundRepository.Factory backgroundRepositoryFactory() {
        return new BackgroundRepository.Factory();
    }

}
