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

package org.phoenicis.javafx.settings;

public enum JavaFxSetting {
    THEME("application.theme"), SCALE("application.scale"), ADVANCED_MODE("application.advancedMode"), VIEW_SOURCE(
            "application.viewsource"), WINDOW_HEIGHT(
                    "application.windowHeight"), WINDOW_WIDTH("application.windowWidth"), WINDOW_MAXIMIZED(
                            "application.windowMaximized"), APPS_LIST_TYPE(
                                    "application.appsListType"), CONTAINERS_LIST_TYPE(
                                            "application.containersListType"), ENGINES_LIST_TYPE(
                                                    "application.enginesListType"), INSTALLATIONS_LIST_TYPE(
                                                            "application.installationsListType"), LIBRARY_LIST_TYPE(
                                                                    "application.libraryListType"), FUZZY_SEARCH_RATIO(
                                                                            "application.fuzzySearchRatio");

    private final String propertyName;

    JavaFxSetting(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        return this.propertyName;
    }
}
