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

package com.playonlinux.javafx.views.common;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class PlayOnLinuxScene extends Scene {
    private static final String DEFAULT_THEME = PlayOnLinuxScene.class.getResource("defaultTheme.css").toExternalForm();
    private static final String DARK_THEME = PlayOnLinuxScene.class.getResource("darkTheme.css").toExternalForm();
    private static final String HIDPI_THEME = PlayOnLinuxScene.class.getResource("hidpiTheme.css").toExternalForm();

    public PlayOnLinuxScene(Parent parent, int width, int height) {
        super(parent, width, height);
        this.getStylesheets().add(DEFAULT_THEME);
    }

    public PlayOnLinuxScene(Parent parent) {
        super(parent);
        this.getStylesheets().add(DEFAULT_THEME);
    }

    public PlayOnLinuxScene(Parent parent, String theme) {
        super(parent);
        this.getStylesheets().add(PlayOnLinuxScene.class.getResource(theme).toExternalForm());
    }

    public String getTheme(Themes theme) {
        switch(theme) {
            case DEFAULT: {
                return DEFAULT_THEME;
            }
            case DARK: {
                return DARK_THEME;
            }
            case HIDPI: {
                return HIDPI_THEME;
            }
            default: {
                return DEFAULT_THEME;
            }
        }
    }
}
