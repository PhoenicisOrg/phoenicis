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

package com.playonlinux.apps.entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.playonlinux.core.entities.Entity;
import com.playonlinux.core.entities.NameableEntity;

/**
 * Represents an application {@link Entity}
 */
public class AppEntity implements Entity, NameableEntity {
    private final String name;
    private final String categoryName;
    private final boolean isCommercial;
    private final boolean isTesting;
    private final boolean requiresNoCd;
    private final List<URL> miniaturesUrls;
    private final String description;
    private final List<ScriptEntity> scripts;

    private AppEntity(Builder builder) {
        this.name = builder.name;
        this.categoryName = builder.categoryName;
        this.isCommercial = builder.isCommercial;
        this.isTesting = builder.isTesting;
        this.requiresNoCd = builder.requiresNoCd;
        this.description = builder.description;
        this.miniaturesUrls = builder.miniaturesUrls;
        this.scripts = builder.scripts;
    }

    public List<ScriptEntity> getScripts() {
        return scripts;
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
        private String name;
        private String categoryName;
        private boolean isCommercial;
        private boolean isTesting;
        private boolean requiresNoCd;
        private String description;
        private List<URL> miniaturesUrls = new ArrayList<>();
        private static final Logger LOGGER = Logger.getLogger(AppEntity.class);
        private List<ScriptEntity> scripts;

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


        public Builder withMiniaturesUrlsString(Iterable<String> miniaturesUrlsAsString) {
            List<URL> newMiniaturesUrls = new ArrayList<>();
            for(String url: miniaturesUrlsAsString) {
                try {
                    newMiniaturesUrls.add(new URL(url));
                } catch (MalformedURLException e) {
                    LOGGER.warn(String.format("%s was malformed. Ignored", url), e);
                }
            }
            return withMiniaturesUrls(newMiniaturesUrls);
        }

        private Builder withMiniaturesUrls(List<URL> miniaturesUrls) {
            this.miniaturesUrls = miniaturesUrls;
            return this;
        }

        public Builder withScripts(List<ScriptEntity> scripts) {
            this.scripts = scripts;
            return this;
        }

        public AppEntity build() {
            validate();
            return new AppEntity(this);
        }

        private void validate() {
            if(categoryName == null) {
                throw new IllegalArgumentException("You must give a category name");
            }
        }
    }
}
