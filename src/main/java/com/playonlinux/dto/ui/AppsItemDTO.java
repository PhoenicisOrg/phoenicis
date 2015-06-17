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

package com.playonlinux.dto.ui;

import com.playonlinux.dto.AbstractDTO;
import com.playonlinux.utils.comparator.Nameable;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AppsItemDTO implements AbstractDTO, Nameable, ItemWithMiniatureDTO {
    private final String name;
    private final String categoryName;
    private final boolean isCommercial;
    private final boolean isTesting;
    private final boolean requiresNoCd;
    private final List<URL> miniaturesUrls;
    private final String description;

    private AppsItemDTO(Builder builder) {
        this.name = builder.name;
        this.categoryName = builder.categoryName;
        this.isCommercial = builder.isCommercial;
        this.isTesting = builder.isTesting;
        this.requiresNoCd = builder.requiresNoCd;
        this.description = builder.description;
        this.miniaturesUrls = builder.miniaturesUrls;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isCommercial() {
        return isCommercial;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean isTesting() {
        return isTesting;
    }

    public boolean isRequiresNoCd() {
        return requiresNoCd;
    }

    public String getDescription() {
        return description;
    }

    public List<URL> getMiniaturesUrls() {
        return miniaturesUrls;
    }

    public static class Builder {
        public String name;
        public String categoryName;
        public boolean isCommercial;
        public boolean isTesting;
        public boolean requiresNoCd;
        public String description;
        public List<URL> miniaturesUrls = new ArrayList<>();
        private Logger logger = Logger.getLogger(AppsItemDTO.class);

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCategoryName(String name) {
            this.categoryName = name;
            return this;
        }

        public Builder withTesting(boolean testing) {
            this.isTesting = testing;
            return this;
        }

        public Builder withCommercial(boolean commercial) {
            this.isCommercial = commercial;
            return this;
        }

        public Builder withRequiresNoCd(boolean noCd) {
            this.requiresNoCd = noCd;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }


        public Builder withMiniaturesUrlsString(List<String> miniaturesUrlsAsString) {
            List<URL> miniaturesUrls = new ArrayList<>();
            for(String url: miniaturesUrlsAsString) {
                try {
                    miniaturesUrls.add(new URL(url));
                } catch (MalformedURLException e) {
                    logger.warn(String.format("%s was malformed. Ignored", url), e);
                }
            }
            return withMiniaturesUrls(miniaturesUrls);
        }

        private Builder withMiniaturesUrls(List<URL> miniaturesUrls) {
            this.miniaturesUrls = miniaturesUrls;
            return this;
        }

        public AppsItemDTO build() {
            validate();
            return new AppsItemDTO(this);
        }

        private void validate() {
            if(categoryName == null) {
                throw new IllegalArgumentException("You must give a category name");
            }
        }

    }
}
