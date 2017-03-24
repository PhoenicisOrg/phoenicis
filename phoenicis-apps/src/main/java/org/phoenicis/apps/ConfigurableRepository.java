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

class ConfigurableRepository implements Repository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableRepository.class);

    private final LocalRepository.Factory localRepositoryFactory;
    private final ClasspathRepository.Factory classPathRepositoryFactory;
    private final Repository repository;
    private final String cacheDirectoryPath;

    ConfigurableRepository(String sourceUrl, String cacheDirectoryPath, LocalRepository.Factory localRepositoryFactory, ClasspathRepository.Factory classPathRepositoryFactory) {
        this.cacheDirectoryPath = cacheDirectoryPath;
        this.localRepositoryFactory = localRepositoryFactory;
        this.classPathRepositoryFactory = classPathRepositoryFactory;
        final String[] urls = sourceUrl.split(";");
        repository = new MultipleRepository(Arrays.stream(urls).map(this::toRepository).collect(Collectors.toList()));
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        return repository.fetchInstallableApplications();
    }

    @Override
    public void delete() {
        this.repository.delete();
    }

    private Repository toRepository(String repositoryUrl) {
        LOGGER.info("Registering: " + repositoryUrl);
        try {
            final URI url = new URI(repositoryUrl);
            final String scheme = url.getScheme().split("\\+")[0];

            switch (scheme) {
                case "git":
                    return new GitRepository(repositoryUrl.replace("git+",""), cacheDirectoryPath,
                            localRepositoryFactory);
                case "file":
                    return localRepositoryFactory.createInstance(url.getRawPath());
                case "classpath":
                    return classPathRepositoryFactory.createInstance(url.getPath());
                default:
                    LOGGER.warn("Unsupported URL: " + repositoryUrl);
                    return new NullRepository();
            }
        } catch (URISyntaxException e) {
            LOGGER.warn("Cannot parse URL: " + repositoryUrl, e);
            return new NullRepository();
        }
    }
}
