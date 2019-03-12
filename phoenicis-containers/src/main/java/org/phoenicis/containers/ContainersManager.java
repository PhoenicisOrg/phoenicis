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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * Manager class for {@link ContainerDTO} objects
 */
public interface ContainersManager {
    Logger LOGGER = LoggerFactory.getLogger(ContainersManager.class);

    /**
     * Fetches a list containing all available containers
     *
     * @param onSuccess Callback for when the fetching was successful
     * @param onError Callback for when an error occurred during the fetching
     */
    void fetchContainers(Consumer<List<ContainerCategoryDTO>> onSuccess, Consumer<Exception> onError);

    /**
     * Deletes a given container
     *
     * @param container The container
     * @param onSuccess Callback for when the container has been successfully deleted
     * @param onError Callback for when an error occured during the deletion
     */
    void deleteContainer(ContainerDTO container, Consumer<ContainerDTO> onSuccess, Consumer<Exception> onError);
}
