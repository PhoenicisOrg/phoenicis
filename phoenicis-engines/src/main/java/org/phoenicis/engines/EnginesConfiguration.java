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

package org.phoenicis.engines;

import org.phoenicis.configuration.PhoenicisGlobalConfiguration;
import org.phoenicis.scripts.ScriptsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnginesConfiguration {
    @Autowired
    private ScriptsConfiguration scriptsConfiguration;

    @Autowired
    private PhoenicisGlobalConfiguration phoenicisGlobalConfiguration;

    @Bean
    public EnginesSource enginesSource() {
        return new EnginesSource(scriptsConfiguration.scriptInterpreter(), phoenicisGlobalConfiguration.objectMapper());
    }

    @Bean
    public EngineToolsManager engineToolsManager() {
        return new EngineToolsManager(scriptsConfiguration.scriptInterpreter());
    }
}
