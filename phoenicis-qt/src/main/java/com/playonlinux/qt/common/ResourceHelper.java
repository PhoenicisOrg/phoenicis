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
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trolltech.qt.core.QBuffer;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPixmap;

/**
 * Small helper class for easing the use of images and icons.
 */
public final class ResourceHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceHelper.class);

    /**
     * Load the resource of the given class with the given path as a pixmap.
     *
     * @param c            Class who's resources should be searched for the given resourcePath.
     * @param resourcePath Relative path to the resource starting from the classes package.
     * @return QIcon, created from the loaded resource.
     */
    public static QPixmap getPixmap(Class<?> c, String resourcePath) {
        //get classPath to class's resources
        String classPath = c.getPackage().getName().replace('.', '/');
        return new QPixmap("classpath:" + classPath + "/" + resourcePath);
    }

    /**
     * Load a shared resource as image.
     *
     * @param resourcePath Relative path to the shared resource folder.
     * @return QIcon, created from the loaded resource.
     */
    public static QPixmap getPixmap(String resourcePath) {
        return getPixmap(ResourceHelper.class, resourcePath);
    }

    /**
     * Load a pixmap from the given URL.
     * @param source URL to load the pixmap from
     * @return loaded Pixmap
     */
    public static QPixmap getPixmap(URL source){
        try {
            QBuffer sourceDev = getDeviceFromStream(source.openStream());
            QPixmap pixmap = new QPixmap();
            pixmap.loadFromData(sourceDev.data());
            return pixmap;
        } catch (IOException e) {
            LOGGER.error("Failed to load pixmap", e);
            return new QPixmap();
        }
    }


    /**
     * Load the resource of the given class with the given path as an icon.
     *
     * @param c            Class who's resources should be searched for the given resourcePath.
     * @param resourcePath Relative path to the resource starting from the classes package.
     * @return QIcon, created from the loaded resource.
     */
    public static QIcon getIcon(Class<?> c, String resourcePath) {
        return new QIcon(getPixmap(c, resourcePath));
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
            LOGGER.error("Failed to get stylesheet", e);
        }
        return styleWriter.toString();
    }

    /**
     * Generate a QIODevice from the given InputStream
     * @param stream Stream to turn into a QIODevice
     * @return A QBuffer filled with the date from the given InputStream
     */
    public static QBuffer getDeviceFromStream(InputStream stream){
        byte[] resourceData;
        try {
            resourceData = IOUtils.toByteArray(stream);
        } catch (IOException e) {
            LOGGER.error("Failed to get device", e);
            return null;
        }
        return new QBuffer(new QByteArray(resourceData));
    }

    /**
     * Generate a QIODevice for the resource with the given name of the given class
     * @param c Class that should be used for locating the resource
     * @param resourcePath relative path of the resource
     * @return A QBuffer filled with the data from the resource
     */
    public static QBuffer getDeviceFromResource(Class<?> c, String resourcePath){
        return getDeviceFromStream(c.getResourceAsStream(resourcePath));
    }

}
