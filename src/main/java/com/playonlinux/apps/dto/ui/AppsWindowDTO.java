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

package com.playonlinux.apps.dto.ui;

import com.playonlinux.dto.AbstractDTO;

import java.util.List;

public class AppsWindowDTO implements AbstractDTO {
    public List<AppsItemDTO> getAppsItemDTOs() {
        return appsItemDTOs;
    }

    public List<AppsCategoryDTO> getCategoryDTOs() {
        return categoryDTOs;
    }

    public boolean isDownloadFailed() {
        return downloadFailed;
    }

    public boolean isDownloading() {
        return downloading;
    }

    final List<AppsItemDTO> appsItemDTOs;
    final List<AppsCategoryDTO> categoryDTOs;
    final boolean downloading;
    final boolean downloadFailed;

    private AppsWindowDTO(Builder builder) {
        appsItemDTOs = builder.appsItemDTOs;
        categoryDTOs = builder.appsCategoryDTOs;
        downloadFailed = builder.downloadFailed;
        downloading = builder.downloading;
    }

    public static class Builder {
        List<AppsItemDTO> appsItemDTOs;
        List<AppsCategoryDTO> appsCategoryDTOs;
        boolean downloading;
        boolean downloadFailed;

        public Builder withAppsItem(List<AppsItemDTO> appsItemDTOs) {
            this.appsItemDTOs = appsItemDTOs;
            return this;
        }

        public Builder withAppsCategory(List<AppsCategoryDTO> appsCategoryDTOs) {
            this.appsCategoryDTOs = appsCategoryDTOs;
            return this;
        }

        public Builder withDownloading(boolean downloading) {
            this.downloading = downloading;
            return this;
        }

        public Builder withDownloadFailed(boolean downloadFailed) {
            this.downloadFailed = downloadFailed;
            return this;
        }

        public AppsWindowDTO build() {
            return new AppsWindowDTO(this);
        }
    }
}
