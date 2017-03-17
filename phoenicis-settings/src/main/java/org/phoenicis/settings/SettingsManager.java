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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SettingsManager {
	@Value("${application.theme}")
	private String theme;

	@Value("${application.viewsource}")
	private boolean viewScriptSource;

	@Value("${application.repository.configuration}")
	private String repository;

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

	public boolean isViewScriptSource() {
		return viewScriptSource;
	}

	public void setViewScriptSource(boolean viewScriptSource) {
		this.viewScriptSource = viewScriptSource;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public void save() {
		Settings settings = load();
		try {
			File file = new File(settingsFileName);
			OutputStream outputStream = new FileOutputStream(file);
			DefaultPropertiesPersister persister = new DefaultPropertiesPersister();
			persister.store(settings.getProperties(), outputStream, "PoL 5 User Settings");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Settings load() {
		Settings settings = new Settings();
		settings.set(Setting.THEME, theme);
		settings.set(Setting.VIEW_SOURCE, String.valueOf(viewScriptSource));
		settings.set(Setting.REPOSITORY, repository);
		return settings;
	}
}
