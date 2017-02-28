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

import org.phoenicis.repository.dto.ApplicationCategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ConfigurableApplicationSource implements RepositorySource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableApplicationSource.class);

    private final LocalRepositorySource.Factory localApplicationsSourceFactory;
    private final ClasspathRepositorySource.Factory classPathApplicationsSourceFactory;
    private final RepositorySource repositorySource;

    ConfigurableApplicationSource(String sourceUrl, LocalRepositorySource.Factory localApplicationsSourceFactory, ClasspathRepositorySource.Factory classPathApplicationsSourceFactory) {
        this.localApplicationsSourceFactory = localApplicationsSourceFactory;
        this.classPathApplicationsSourceFactory = classPathApplicationsSourceFactory;
        final String[] urls = sourceUrl.split(";");
        repositorySource = new MultipleRepositorySource(Arrays.stream(urls).map(this::toApplicationSource).collect(Collectors.toList()));
    }

    @Override
    public List<ApplicationCategoryDTO> fetchInstallableApplications() {
        return repositorySource.fetchInstallableApplications();
    }

    private RepositorySource toApplicationSource(String applicationSourceUrl) {
        LOGGER.info("Registering: " + applicationSourceUrl);
        try {
            final URI url = new URI(applicationSourceUrl);
            final String scheme = url.getScheme().split("\\+")[0];

            switch (scheme) {
                case "git":
                    return new GitRepositorySource(applicationSourceUrl.replace("git+",""), localApplicationsSourceFactory);
                case "file":
                    return localApplicationsSourceFactory.createInstance(url.getRawPath());
                case "classpath":
                    return classPathApplicationsSourceFactory.createInstance(url.getPath());
                default:
                    LOGGER.warn("Unsupported URL: " + applicationSourceUrl);
                    return new NullRepositorySource();
            }
        } catch (URISyntaxException e) {
            LOGGER.warn("Cannot parse URL: " + applicationSourceUrl, e);
            return new NullRepositorySource();
        }
    }
}
