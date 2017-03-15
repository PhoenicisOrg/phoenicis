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

package org.phoenicis.apps;

import org.phoenicis.apps.dto.CategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ConfigurableRepositorySource implements RepositorySource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableRepositorySource.class);

    private final LocalRepositorySource.Factory localRepositorySourceFactory;
    private final ClasspathRepositorySource.Factory classPathRepositorySourceFactory;
    private final RepositorySource repositorySource;
    private final String cacheDirectoryPath;

    ConfigurableRepositorySource(String sourceUrl, String cacheDirectoryPath, LocalRepositorySource.Factory localRepositorySourceFactory, ClasspathRepositorySource.Factory classPathRepositorySourceFactory) {
        this.cacheDirectoryPath = cacheDirectoryPath;
        this.localRepositorySourceFactory = localRepositorySourceFactory;
        this.classPathRepositorySourceFactory = classPathRepositorySourceFactory;
        final String[] urls = sourceUrl.split(";");
        repositorySource = new MultipleRepositorySource(Arrays.stream(urls).map(this::toRepositorySource).collect(Collectors.toList()));
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        return repositorySource.fetchInstallableApplications();
    }

    private RepositorySource toRepositorySource(String repositorySourceUrl) {
        LOGGER.info("Registering: " + repositorySourceUrl);
        try {
            final URI url = new URI(repositorySourceUrl);
            final String scheme = url.getScheme().split("\\+")[0];

            switch (scheme) {
                case "git":
                    return new GitRepositorySource(repositorySourceUrl.replace("git+",""), cacheDirectoryPath,
                            localRepositorySourceFactory);
                case "file":
                    return localRepositorySourceFactory.createInstance(url.getRawPath());
                case "classpath":
                    return classPathRepositorySourceFactory.createInstance(url.getPath());
                default:
                    LOGGER.warn("Unsupported URL: " + repositorySourceUrl);
                    return new NullRepositorySource();
            }
        } catch (URISyntaxException e) {
            LOGGER.warn("Cannot parse URL: " + repositorySourceUrl, e);
            return new NullRepositorySource();
        }
    }
}
