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

package org.phoenicis.settings;

import org.phoenicis.repository.RepositoryConfiguration;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class SettingsManager {
    @Value("${application.theme}")
    private String theme;

    @Value("${application.scale}")
    private double scale;

    @Value("${application.viewsource}")
    private boolean viewScriptSource;

    @Autowired
    private RepositoryConfiguration repositoryConfiguration;

    private String settingsFileName = "config.properties";

    public SettingsManager(String settingsFileName) {
        this.settingsFileName = settingsFileName;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public boolean isViewScriptSource() {
        return viewScriptSource;
    }

    public void setViewScriptSource(boolean viewScriptSource) {
        this.viewScriptSource = viewScriptSource;
    }

    public void save() {
        Settings settings = load();
        try (OutputStream outputStream = new FileOutputStream(new File(settingsFileName))) {
            DefaultPropertiesPersister persister = new DefaultPropertiesPersister();
            persister.store(settings.getProperties(), outputStream, "Phoenicis User Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Settings load() {
        Settings settings = new Settings();

        settings.set(Setting.THEME, theme);
        settings.set(Setting.SCALE, scale);
        settings.set(Setting.VIEW_SOURCE, String.valueOf(viewScriptSource));

        return settings;
    }

    public void saveRepositories(List<RepositoryLocation<? extends Repository>> repositoryLocations) {
        repositoryConfiguration.saveRepositories(repositoryLocations);
    }

    public List<RepositoryLocation<? extends Repository>> loadRepositoryLocations() {
        return repositoryConfiguration.loadRepositoryLocations();
    }
}
