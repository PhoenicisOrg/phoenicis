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

class ConfigurableApplicationsSource implements ApplicationsSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableApplicationsSource.class);

    private final LocalApplicationsSource.Factory localApplicationsSourceFactory;
    private final ClasspathApplicationsSource.Factory classPathApplicationsSourceFactory;
    private final ApplicationsSource applicationsSource;
    private final String cacheDirectoryPath;

    ConfigurableApplicationsSource(String sourceUrl, String cacheDirectoryPath, LocalApplicationsSource.Factory localApplicationsSourceFactory, ClasspathApplicationsSource.Factory classPathApplicationsSourceFactory) {
        this.cacheDirectoryPath = cacheDirectoryPath;
        this.localApplicationsSourceFactory = localApplicationsSourceFactory;
        this.classPathApplicationsSourceFactory = classPathApplicationsSourceFactory;
        final String[] urls = sourceUrl.split(";");
        applicationsSource = new MultipleApplicationsSource(Arrays.stream(urls).map(this::toApplicationsSource).collect(Collectors.toList()));
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        return applicationsSource.fetchInstallableApplications();
    }

    private ApplicationsSource toApplicationsSource(String applicationsSourceUrl) {
        LOGGER.info("Registering: " + applicationsSourceUrl);
        try {
            final URI url = new URI(applicationsSourceUrl);
            final String scheme = url.getScheme().split("\\+")[0];

            switch (scheme) {
                case "git":
                    return new GitApplicationsSource(applicationsSourceUrl.replace("git+",""), cacheDirectoryPath, localApplicationsSourceFactory);
                case "file":
                    return localApplicationsSourceFactory.createInstance(url.getRawPath());
                case "classpath":
                    return classPathApplicationsSourceFactory.createInstance(url.getPath());
                default:
                    LOGGER.warn("Unsupported URL: " + applicationsSourceUrl);
                    return new NullApplicationsSource();
            }
        } catch (URISyntaxException e) {
            LOGGER.warn("Cannot parse URL: " + applicationsSourceUrl, e);
            return new NullApplicationsSource();
        }
    }
}
