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

import com.phoenicis.settings.Setting;
import com.phoenicis.settings.Settings;
import com.playonlinux.javafx.views.common.TextWithStyle;
import com.playonlinux.javafx.views.common.Themes;
import com.playonlinux.javafx.views.mainwindow.MainWindowView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.Optional;
import java.util.function.Consumer;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class ViewSettings extends MainWindowView {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private ComboBox<Themes> themes;
    private final ObservableList<String> repositories = FXCollections.observableArrayList();
    private final Button saveButton = new Button("Save");
    private Consumer<Settings> onSave;
    private Settings settings = new Settings();

    public ViewSettings() {
        super("Settings");

        super.drawSideBar();

        final VBox informationPane = new VBox();
        informationPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);

        final Text title = new TextWithStyle(translate("Settings"), TITLE_CSS_CLASS);
        informationPane.getChildren().add(title);

        final GridPane informationContentPane = new GridPane();
        informationContentPane.getStyleClass().add("grid");

        informationContentPane.add(new TextWithStyle(translate("Theme:"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        themes = new ComboBox<>();
        themes.getItems().setAll(Themes.values());
        themes.setOnAction(this::handleThemeChange);
        informationContentPane.add(themes, 1, 0);

        TextWithStyle repositoryText = new TextWithStyle(translate("Repository:"), CAPTION_TITLE_CSS_CLASS);
        informationContentPane.add(repositoryText, 0, 1);
        GridPane.setValignment(repositoryText, VPos.TOP);
        VBox repositoryLayout = new VBox();
        repositoryLayout.setSpacing(5);
        ListView repositoryListView = new ListView(repositories);
        repositoryListView.setPrefSize(400,100);
        repositoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        repositoryListView.setEditable(true);
        repositoryListView.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<String>() {

            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));
        HBox repositoryButtonLayout = new HBox();
        repositoryButtonLayout.setSpacing(5);
        Button addButton = new Button();
        addButton.setText("Add");
        addButton.setOnAction((ActionEvent event) -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add repository");
            dialog.setHeaderText("Add repository");
            dialog.setContentText("Please add the new repository:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(repository -> repositories.add(repository));
        });
        Button removeButton = new Button();
        removeButton.setText("Remove");
        removeButton.setOnAction((ActionEvent event) -> {
            repositories.removeAll(repositoryListView.getSelectionModel().getSelectedItems());
        });
        repositoryButtonLayout.getChildren().addAll(addButton,removeButton);
        repositoryLayout.getChildren().addAll(repositoryListView,repositoryButtonLayout);
        informationContentPane.add(repositoryLayout, 1, 1);

        informationContentPane.setHgap(20);
        informationContentPane.setVgap(10);

        informationPane.getChildren().add(informationContentPane);


        saveButton.setOnMouseClicked(event -> this.save());
        informationPane.getChildren().add(saveButton);

        showRightView(informationPane);
    }

    public void setSettings(Settings settings) {
        if (settings.get(Setting.THEME).equals("darkTheme.css")) {
            themes.setValue(Themes.DARK);
        } else if (settings.get(Setting.THEME).equals("breezeDarkTheme.css")) {
            themes.setValue(Themes.BREEZE_DARK);
        } else if (settings.get(Setting.THEME).equals("hidpiTheme.css")) {
            themes.setValue(Themes.HIDPI);
        } else {
            themes.setValue(Themes.DEFAULT);
        }
        repositories.addAll(settings.get(Setting.REPOSITORY).split(";"));
    }

    public void setOnSave(Consumer<Settings> onSave) {
        this.onSave = onSave;
    }

    public void save() {
        String theme;
        switch (themes.getSelectionModel().getSelectedItem()) {
            case DEFAULT: {
                theme = "defaultTheme.css";
                break;
            }
            case DARK: {
                theme = "darkTheme.css";
                break;
            }
            case BREEZE_DARK: {
                theme = "breezeDarkTheme.css";
                break;
            }
            case HIDPI: {
                theme = "hidpiTheme.css";
                break;
            }
            default: {
                theme = "defaultTheme.css";
                break;
            }

        }
        settings.set(Setting.THEME, theme);

        StringBuilder sb = new StringBuilder();
        for (String s : repositories)
        {
            sb.append(s);
            sb.append(";");
        }
        settings.set(Setting.REPOSITORY, sb.toString());

        onSave.accept(this.settings);
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
