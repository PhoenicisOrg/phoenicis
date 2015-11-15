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

package com.playonlinux.ui.impl.javafx.mainwindow.containers;

import com.playonlinux.containers.entities.ContainerEntity;

import java.util.ArrayList;
import java.util.Collection;

public class AnyContainerConfigurationViewFactory implements ContainerConfigurationViewFactory<ContainerEntity> {
    private final Collection<ContainerConfigurationViewFactory<? extends ContainerEntity>> containerConfigurationViewFactories = new ArrayList<>();

    @Override
    public ContainerConfigurationView<? extends ContainerEntity> createInstance(ContainerEntity containerEntity) {
        for(ContainerConfigurationViewFactory<? extends ContainerEntity> containerConfigurationViewFactory: containerConfigurationViewFactories) {
            if(containerConfigurationViewFactory.validate(containerEntity)) {
                return containerConfigurationViewFactory.createInstance(containerEntity);
            }
        }

        throw new IllegalArgumentException("Unknown container configuration view type");
    }

    @Override
    public boolean validate(ContainerEntity containerEntity) {
        for(ContainerConfigurationViewFactory<? extends ContainerEntity> containerConfigurationViewFactory: containerConfigurationViewFactories) {
            if(containerConfigurationViewFactory.validate(containerEntity)) {
                return true;
            }
        }

        return false;
    }

    public AnyContainerConfigurationViewFactory withContainerConfigurationViewFactory(ContainerConfigurationViewFactory<? extends ContainerEntity> containerConfigurationViewFactory) {
        containerConfigurationViewFactories.add(containerConfigurationViewFactory);
        return this;
    }
}
