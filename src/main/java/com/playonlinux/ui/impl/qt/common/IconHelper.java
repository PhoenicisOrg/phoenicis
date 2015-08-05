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

package com.playonlinux.ui.impl.qt.common;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QImageReader;
import com.trolltech.qt.gui.QPixmap;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Small helper class for easing the use of images and icons.
 */
public final class IconHelper {

    /**
     * Load the resource of the given class with the given path to the resource.
     * @param c Class who's resources should be searched for the given resourcePath.
     * @param resourcePath Relative path to the resource starting from the classes package.
     * @return QIcon, created from the loaded resource.
     */
    public static QIcon fromResource(Class<?> c, String resourcePath){
        //get classPath to class's resources
        String classPath = c.getPackage().getName().replace('.', '/');

        return new QIcon(new QPixmap("classpath:" + classPath + "/" + resourcePath));
    }

}
