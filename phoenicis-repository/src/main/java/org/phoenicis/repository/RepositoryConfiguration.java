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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.phoenicis.multithreading.MultithreadingConfiguration;
import org.phoenicis.repository.dto.ClasspathRepositoryLocation;
import org.phoenicis.repository.dto.GitRepositoryLocation;
import org.phoenicis.repository.dto.LocalRepositoryLocation;
import org.phoenicis.repository.dto.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.BackgroundRepository;
import org.phoenicis.repository.repositoryTypes.ClasspathRepository;
import org.phoenicis.repository.repositoryTypes.LocalRepository;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.phoenicis.tools.ToolsConfiguration;
import org.phoenicis.tools.files.FileUtilities;
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
                multithreadingConfiguration.appsExecutorService(), enforceUncompatibleOperatingSystems,
                toolsConfiguration, cacheDirectoryPath, fileUtilities, localRepositoryFactory(),
                classPathRepositoryFactory(), backgroundRepositoryFactory());

        // set initial repositories
        repositoryManager
                .addRepositories(this.loadRepositoryLocations(objectMapper()).toArray(new RepositoryLocation[0]));

        return repositoryManager;
    }

    public void saveRepositories(ObjectMapper objectMapper,
            List<RepositoryLocation<? extends Repository>> repositoryLocations) {
        try {
            objectMapper.writeValue(new File(repositoryListPath), repositoryLocations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<RepositoryLocation<? extends Repository>> loadRepositoryLocations(ObjectMapper objectMapper) {
        List<RepositoryLocation<? extends Repository>> result = new ArrayList<>();

        File repositoryListFile = new File(repositoryListPath);

        if (repositoryListFile.exists()) {
            try {
                result = objectMapper.readValue(new File(repositoryListPath),
                        TypeFactory.defaultInstance().constructParametricType(List.class, RepositoryLocation.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                result.add(new GitRepositoryLocation.Builder()
                        .withGitRepositoryUri(new URL("https://github.com/PlayOnLinux/Scripts").toURI()).build());
                result.add(new GitRepositoryLocation.Builder()
                        .withGitRepositoryUri(new URL("https://github.com/PlayOnLinux/Oldwares").toURI()).build());
                result.add(
                        new ClasspathRepositoryLocation.Builder().withPackagePath("/org/phoenicis/repository").build());
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
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
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
