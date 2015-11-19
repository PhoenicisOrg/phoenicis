/*
 * Copyright (C) 2015 Markus Ebner
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

package com.playonlinux.qt.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPixmap;

/**
 * Small helper class for easing the use of images and icons.
 */
public final class ResourceHelper {

    private static final Logger LOGGER = Logger.getLogger(ResourceHelper.class);

    /**
     * Load the resource of the given class with the given path as an image.
     *
     * @param c            Class who's resources should be searched for the given resourcePath.
     * @param resourcePath Relative path to the resource starting from the classes package.
     * @return QIcon, created from the loaded resource.
     */
    public static QIcon getIcon(Class<?> c, String resourcePath) {
        //get classPath to class's resources
        String classPath = c.getPackage().getName().replace('.', '/');

        return new QIcon(new QPixmap("classpath:" + classPath + "/" + resourcePath));
    }

    /**
     * Load a shared resource as image.
     *
     * @param resourcePath Relative path to the shared resource folder.
     * @return QIcon, created from the loaded resource.
     */
    public static QIcon getIcon(String resourcePath) {
        return getIcon(ResourceHelper.class, resourcePath);
    }

    public static String getStyleSheet(Class<?> c, String resourcePath) {
        StringWriter styleWriter = new StringWriter();
        InputStream styleStream = c.getResourceAsStream(resourcePath);
        try {
            IOUtils.copy(styleStream, styleWriter, "UTF-8");
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return styleWriter.toString();
    }

}
