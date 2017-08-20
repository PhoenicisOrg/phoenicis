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

package org.phoenicis.javafx.views.mainwindow.containers;

import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.wine.WinePrefixContainerController;
import org.phoenicis.engines.dto.EngineVersionDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ContainerPanelFactory<T extends AbstractContainerPanel<C>, C extends ContainerDTO> {
    private final Class<T> clazz;
    private final Class<C> entityClazz;

    public ContainerPanelFactory(Class<T> viewClazz, Class<C> entityClazz) {
        this.clazz = viewClazz;
        this.entityClazz = entityClazz;
    }

    public T createContainerPanel(C containerDTO, List<EngineVersionDTO> engineVersions,
            WinePrefixContainerController winePrefixContainerController) {
        try {
            return this.clazz
                    .getConstructor(entityClazz, List.class, WinePrefixContainerController.class)
                    .newInstance(containerDTO, engineVersions, winePrefixContainerController);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}
