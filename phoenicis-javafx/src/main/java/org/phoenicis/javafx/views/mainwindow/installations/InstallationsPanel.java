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

package org.phoenicis.javafx.views.mainwindow.installations;

import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;

/**
 * shows details for an installation
 */
final class InstallationsPanel extends DetailsView {

    /**
     * set installation which shall be shown in this details view
     * @param installationDTO
     */
    public void setInstallationDTO(InstallationDTO installationDTO) {
        this.setTitle(installationDTO.getName());
        this.setCenter(installationDTO.getNode());
    }

}
