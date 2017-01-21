/*
 * Copyright (C) 2015 Kaspar Tint
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

package com.playonlinux.javafx.views.mainwindow.settings;

import com.playonlinux.javafx.views.common.Themes;
import com.playonlinux.javafx.views.mainwindow.MainWindowView;
import com.playonlinux.javafx.views.mainwindow.ui.LeftBarTitle;
import com.playonlinux.javafx.views.mainwindow.ui.LeftSpacer;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.Properties;
import java.util.function.Consumer;

public class ViewSettings extends MainWindowView {
    private ComboBox<Themes> themes;
    private final Button saveButton = new Button("Save");
    private Consumer<Properties> onSave;
    private Properties settings;

    public ViewSettings() {
        super("Settings");

        this.drawSideBar();
    }

    protected void drawSideBar() {
        super.drawSideBar();
        themes = new ComboBox<>();
        themes.getItems().setAll(Themes.values());
        themes.setOnAction(this::handleThemeChange);

        saveButton.setOnMouseClicked(event -> onSave.accept(this.settings));

        LeftSpacer spacer = new LeftSpacer();
        addToSideBar(new LeftBarTitle("Settings"), spacer, themes, saveButton);

    }

    public void setOnSave(Consumer<Properties> onSave) {
        this.onSave = onSave;
    }

    private void handleThemeChange(ActionEvent evt) {
        /*
        // TODO: Save selected theme in config
        switch (themes.getSelectionModel().getSelectedItem()) {
            case DEFAULT: {
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(parent.getPlayOnLinuxScene().getTheme(Themes.DEFAULT));
                break;
            }
            case DARK: {
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(parent.getPlayOnLinuxScene().getTheme(Themes.DARK));
                break;
            }
            case HIDPI: {
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(parent.getPlayOnLinuxScene().getTheme(Themes.HIDPI));
                break;
            }
            default: {
                this.getScene().getStylesheets().clear();
                this.getScene().getStylesheets().add(parent.getPlayOnLinuxScene().getTheme(Themes.DEFAULT));
                break;
            }

        }*/
    }
}
