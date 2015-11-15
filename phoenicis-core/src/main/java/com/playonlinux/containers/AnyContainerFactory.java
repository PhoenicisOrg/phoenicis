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

package com.playonlinux.containers;

import com.playonlinux.containers.entities.ContainerEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class AnyContainerFactory implements ContainerFactory<ContainerEntity> {
    private Collection<ContainerFactory<? extends ContainerEntity>> containerFactories = new ArrayList<>();

    @Override
    public Container<? extends ContainerEntity> createInstance(String containerType, File file) {
        for(ContainerFactory<? extends ContainerEntity> containerFactory: containerFactories) {
            if(containerFactory.validate(containerType)) {
                return containerFactory.createInstance(containerType, file);
            }
        }

        throw new IllegalStateException(String.format("Unknown container type: %s", containerType));
    }

    @Override
    public boolean validate(String containerType) {
        for(ContainerFactory<?> containerFactory: containerFactories) {
            if(containerFactory.validate(containerType)) {
                return true;
            }
        }

        return false;
    }

    public AnyContainerFactory withContainerFactory(ContainerFactory<? extends ContainerEntity> containerFactory) {
        this.containerFactories.add(containerFactory);
        return this;
    }
}
