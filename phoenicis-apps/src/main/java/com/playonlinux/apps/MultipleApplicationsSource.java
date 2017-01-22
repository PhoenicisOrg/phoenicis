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

package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.util.Arrays;
import java.util.List;

class MultipleApplicationsSource implements ApplicationsSource {
    private final ApplicationsSource applicationsSource;

    MultipleApplicationsSource(ApplicationsSource ...applicationsSources) {
        this(Arrays.asList(applicationsSources));
    }

    MultipleApplicationsSource(List<ApplicationsSource> applicationsSources) {
        ApplicationsSource lastApplicationSource = new NullApplicationsSource();

        for (ApplicationsSource applicationSource : applicationsSources) {
            lastApplicationSource = new TeeApplicationsSource(lastApplicationSource, applicationSource);
        }

        this.applicationsSource = lastApplicationSource;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        return applicationsSource.fetchInstallableApplications();
    }
}
