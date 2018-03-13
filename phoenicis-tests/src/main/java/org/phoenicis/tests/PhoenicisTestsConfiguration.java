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

package org.phoenicis.tests;

import org.phoenicis.configuration.PhoenicisGlobalConfiguration;
import org.phoenicis.multithreading.MultithreadingConfiguration;
import org.phoenicis.repository.RepositoryConfiguration;
import org.phoenicis.repository.repositoryTypes.NullRepository;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.phoenicis.scripts.ScriptsConfiguration;
import org.phoenicis.scripts.wizard.WizardConfiguration;
import org.phoenicis.tools.ToolsConfiguration;
import org.phoenicis.win32.Win32Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ PhoenicisGlobalConfiguration.class, MultithreadingConfiguration.class, Win32Configuration.class,
        ToolsConfiguration.class, RepositoryConfiguration.class, ScriptsConfiguration.class, WizardConfiguration.class,
        TestUiConfiguration.class, })
class PhoenicisTestsConfiguration {

    @Bean
    public Repository mockedRepository() {
        return new MockedRepository(new NullRepository());
    }

}
