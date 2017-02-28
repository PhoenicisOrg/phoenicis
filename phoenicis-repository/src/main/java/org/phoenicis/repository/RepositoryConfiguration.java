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
import org.phoenicis.multithreading.MultithreadingConfiguration;
import org.phoenicis.tools.ToolsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class RepositoryConfiguration {
    @Value("${application.repository.configuration}")
    private String repositoryConfiguration;

    @Value("${application.repository.forceIncompatibleOperatingSystems:false}")
    private boolean enforceUncompatibleOperatingSystems;

    @Autowired
    private MultithreadingConfiguration multithreadingConfiguration;

    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Bean
    public RepositorySource repositorySource() {
        RepositorySource repositorySource = new ConfigurableRepositorySource(
                repositoryConfiguration,
                new LocalRepositorySource.Factory(objectMapper()),
                new ClasspathRepositorySource.Factory(objectMapper(), new PathMatchingResourcePatternResolver())
        );
        return new FilterRepositorySource(
                new CachedRepositorySource(repositorySource),
                toolsConfiguration.operatingSystemFetcher(),
                enforceUncompatibleOperatingSystems
        );
    }

    @Bean
    public RepositorySource backgroundAppsSource() {
        return new BackgroundRepositorySource(repositorySource(), multithreadingConfiguration.appsExecutorService());
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
