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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.tools.files.FileUtilities;
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

    private final FileUtilities fileUtilities;

    ConfigurableRepository(String sourceUrl, String cacheDirectoryPath, FileUtilities fileUtilities, LocalRepository.Factory localRepositoryFactory, ClasspathRepository.Factory classPathRepositoryFactory) {
        this.cacheDirectoryPath = cacheDirectoryPath;
        this.localRepositoryFactory = localRepositoryFactory;
        this.classPathRepositoryFactory = classPathRepositoryFactory;
        this.fileUtilities = fileUtilities;
        final String[] urls = sourceUrl.split(";");
        repository = new MultipleRepository(Arrays.stream(urls).map(this::toRepository).collect(Collectors.toList()));
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        return repository.fetchInstallableApplications();
    }

    @Override
    public void onDelete() {
        this.repository.onDelete();
    }

    private Repository toRepository(String repositoryUrl) {
        LOGGER.info("Registering: " + repositoryUrl);
        try {
            final URI url = new URI(repositoryUrl);
            final String scheme = url.getScheme().split("\\+")[0];

            switch (scheme) {
                case "git":
                    return new GitRepository(repositoryUrl.replace("git+",""), cacheDirectoryPath,
                            localRepositoryFactory, fileUtilities);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfigurableRepository that = (ConfigurableRepository) o;

        EqualsBuilder builder = new EqualsBuilder();

        builder.append(repository, that.repository);
        builder.append(cacheDirectoryPath, that.cacheDirectoryPath);

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(repository);
        builder.append(cacheDirectoryPath);

        return builder.toHashCode();
    }
}
