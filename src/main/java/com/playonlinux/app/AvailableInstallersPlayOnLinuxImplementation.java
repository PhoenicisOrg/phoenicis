/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.app;

import com.playonlinux.common.dtos.CategoryDTO;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.AvailableInstallers;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

@Component
public class AvailableInstallersPlayOnLinuxImplementation extends Observable implements AvailableInstallers, Observer {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Override
    public Iterator<CategoryDTO> iterator() {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
