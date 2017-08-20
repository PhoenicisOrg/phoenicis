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

import java.util.Properties;

public class JavaFxSettings {
    private Properties properties = new Properties();

    public String get(JavaFxSetting javaFxSetting) {
        return properties.getProperty(javaFxSetting.toString());
    }

    public void set(JavaFxSetting javaFxSetting, String value) {
        properties.setProperty(javaFxSetting.toString(), value);
    }

    public void set(JavaFxSetting javaFxSetting, double value) {
        properties.setProperty(javaFxSetting.toString(), String.valueOf(value));
    }

    public Properties getProperties() {
        return properties;
    }
}
