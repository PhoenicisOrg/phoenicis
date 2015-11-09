package com.playonlinux.ui.impl.javafx.mainwindow.settings;

import com.playonlinux.ui.impl.javafx.common.Themes;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftBarTitle;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftSpacer;
import com.playonlinux.ui.impl.javafx.mainwindow.MainWindow;
import com.playonlinux.ui.impl.javafx.mainwindow.MainWindowView;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;

public class ViewSettings extends MainWindowView {
    private ComboBox<Themes> themes;

    public ViewSettings(MainWindow parent) {
        super(parent);
        this.drawSideBar();
    }

    protected void drawSideBar() {
        super.drawSideBar();
        themes = new ComboBox<>();
        themes.getItems().setAll(Themes.values());
        themes.setOnAction(this::handleThemeChange);

        LeftSpacer spacer = new LeftSpacer();
        addToSideBar(new LeftBarTitle("Settings"), spacer, themes);
    }

    private void handleThemeChange(ActionEvent evt) {
        // TODO: Save selected theme in config
        switch (themes.getSelectionModel().getSelectedItem()) {
            case DEFAULT: {
                this.getScene().getStylesheets().remove(parent.getPlayOnLinuxScene().getDefaultTheme());
                this.getScene().getStylesheets().add(parent.getPlayOnLinuxScene().getDefaultTheme());
                break;
            }
            case DARK: {
                this.getScene().getStylesheets().remove(parent.getPlayOnLinuxScene().getDefaultTheme());
                this.getScene().getStylesheets().add(parent.getPlayOnLinuxScene().getDarkTheme());
                break;
            }
            default: {
                this.getScene().getStylesheets().remove(parent.getPlayOnLinuxScene().getDefaultTheme());
                this.getScene().getStylesheets().add(parent.getPlayOnLinuxScene().getDefaultTheme());
                break;
            }
        }
    }
}
