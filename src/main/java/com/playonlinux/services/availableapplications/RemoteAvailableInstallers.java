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

package com.playonlinux.services.availableapplications;

import com.playonlinux.dto.ui.AppsItemDTO;
import com.playonlinux.dto.ui.CenterCategoryDTO;
import com.playonlinux.services.RemoteService;
import com.playonlinux.services.manager.Service;
import com.playonlinux.utils.filter.Filterable;
import com.playonlinux.utils.observer.Observable;
import com.playonlinux.utils.observer.Observer;
import com.playonlinux.webservice.DownloadEnvelope;

import java.util.List;

/**
 * Represents a service allowing to access to available installers
 */
public interface RemoteAvailableInstallers extends Filterable<AppsItemDTO>, RemoteService,
        Observer<Observable, DownloadEnvelope>, Service {
    List<CenterCategoryDTO> getCategories();
}
