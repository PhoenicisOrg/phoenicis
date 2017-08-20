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

package org.phoenicis.javafx.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * manages JavaFX settings
 */
public class JavaFxSettingsManager {
    @Value("${application.theme}")
    private String theme;

    @Value("${application.scale}")
    private double scale;

    @Value("${application.viewsource}")
    private boolean viewScriptSource;

    @Value("${application.windowWidth}")
    private double windowWidth;

    @Value("${application.windowHeight}")
    private double windowHeight;

    @Value("${application.windowMaximized}")
    private boolean windowMaximized;

    private String settingsFileName = "javafx.properties";

    /**
     * constructor
     * @param settingsFileName settings file
     */
    public JavaFxSettingsManager(String settingsFileName) {
        this.settingsFileName = settingsFileName;
    }

    /**
     * saves settings to settings file
     */
    public void save() {
        JavaFxSettings javaFxSettings = load();
        try (OutputStream outputStream = new FileOutputStream(new File(settingsFileName))) {
            DefaultPropertiesPersister persister = new DefaultPropertiesPersister();
            persister.store(javaFxSettings.getProperties(), outputStream, "Phoenicis JavaFX User JavaFxSettings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads settings from settings file
     * @return
     */
    private JavaFxSettings load() {
        JavaFxSettings javaFxSettings = new JavaFxSettings();

        javaFxSettings.set(JavaFxSetting.THEME, theme);
        javaFxSettings.set(JavaFxSetting.SCALE, scale);
        javaFxSettings.set(JavaFxSetting.VIEW_SOURCE, String.valueOf(viewScriptSource));
        javaFxSettings.set(JavaFxSetting.WINDOW_HEIGHT, this.windowHeight);
        javaFxSettings.set(JavaFxSetting.WINDOW_WIDTH, this.windowWidth);
        javaFxSettings.set(JavaFxSetting.WINDOW_MAXIMIZED, String.valueOf(this.windowMaximized));

        return javaFxSettings;
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

    public void setWindowWidth(double windowWidth) {
        this.windowWidth = windowWidth;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public void setWindowHeight(double windowHeight) {
        this.windowHeight = windowHeight;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public void setWindowMaximized(boolean windowMaximized) {
        this.windowMaximized = windowMaximized;
    }

    public boolean isWindowMaximized() {
        return windowMaximized;
    }
}
