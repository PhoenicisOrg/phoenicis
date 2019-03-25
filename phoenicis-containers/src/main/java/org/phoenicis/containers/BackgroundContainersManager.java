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

package org.phoenicis.containers;

import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * ContainersManager which runs in the background
 */
class BackgroundContainersManager implements ContainersManager {
    private final ContainersManager delegatedContainersManager;
    private final ExecutorService executorService;

    /**
     * Constructor
     *
     * @param delegatedContainersManager The delegated containers manager
     * @param executorService The executor service
     */
    BackgroundContainersManager(ContainersManager delegatedContainersManager, ExecutorService executorService) {
        this.delegatedContainersManager = delegatedContainersManager;
        this.executorService = executorService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fetchContainers(Consumer<List<ContainerCategoryDTO>> onSuccess, Consumer<Exception> onError) {
        executorService.submit(() -> delegatedContainersManager.fetchContainers(onSuccess, onError));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteContainer(ContainerDTO container, Consumer<ContainerDTO> onSuccess, Consumer<Exception> onError) {
        executorService.submit(() -> delegatedContainersManager.deleteContainer(container, onSuccess, onError));
    }
}
