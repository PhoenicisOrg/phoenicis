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

package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.geometry.Insets;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.settings.Setting;
import org.phoenicis.settings.Settings;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.javafx.views.common.Theme;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.MessagePanel;
import org.phoenicis.javafx.views.mainwindow.ui.LeftGroup;
import org.phoenicis.javafx.views.mainwindow.ui.LeftToggleButton;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class ViewSettings extends MainWindowView {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";
    private final ObservableList<String> repositories = FXCollections.observableArrayList();
    private ComboBox<Theme> themes;
    private Consumer<Settings> onSave;
    private Settings settings = new Settings();
    private MessagePanel selectSettingsPanel;

    private VBox uiPanel = new VBox();
    private VBox repositoriesPanel = new VBox();
    private VBox fileAssociationsPanel = new VBox();
    private VBox networkPanel = new VBox();

    public ViewSettings(ThemeManager themeManager) {
        super("Settings", themeManager);

        final List<LeftToggleButton> leftButtonList = new ArrayList<>();
        ToggleGroup group = new ToggleGroup();

        final LeftToggleButton uiButton = new LeftToggleButton("User Interface");
        final String uiButtonIcon = "icons/mainwindow/settings/userInterface.png";
        uiButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(uiButtonIcon) + "');");
        uiButton.setToggleGroup(group);
        leftButtonList.add(uiButton);
        uiButton.setOnMouseClicked(event -> showRightView(uiPanel));

        final LeftToggleButton repositoriesButton = new LeftToggleButton("Repositories");
        final String repositoriesButtonIcon = "icons/mainwindow/settings/repository.png";
        repositoriesButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(repositoriesButtonIcon) + "');");
        repositoriesButton.setToggleGroup(group);
        leftButtonList.add(repositoriesButton);
        repositoriesButton.setOnMouseClicked(event -> showRightView(repositoriesPanel));

        final LeftToggleButton fileAssociationsButton = new LeftToggleButton("File Associations");
        final String fileAssociationsButtonIcon = "icons/mainwindow/settings/settings.png";
        fileAssociationsButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(fileAssociationsButtonIcon) + "');");
        fileAssociationsButton.setToggleGroup(group);
        leftButtonList.add(fileAssociationsButton);
        fileAssociationsButton.setOnMouseClicked(event -> showRightView(fileAssociationsPanel));

        final LeftToggleButton networkButton = new LeftToggleButton("Network");
        final String networkButtonIcon = "icons/mainwindow/settings/network.png";
        networkButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(networkButtonIcon) + "');");
        networkButton.setToggleGroup(group);
        leftButtonList.add(networkButton);
        networkButton.setOnMouseClicked(event -> showRightView(networkPanel));

        final LeftGroup leftButtons = new LeftGroup("Settings");
        leftButtons.setNodes(leftButtonList);

        addToSideBar(leftButtons);
        super.drawSideBar();

        initUiSettingsPane();
        initRepositoriesSettingsPane();
        initFileAssociationsPane();
        initNetworkPane();

        initSelectSettingsPane();
        showRightView(this.selectSettingsPanel);
    }

    private void initUiSettingsPane() {
        uiPanel = new VBox();
        uiPanel.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);

        final Text title = new TextWithStyle(translate("User Interface Settings"), TITLE_CSS_CLASS);
        uiPanel.getChildren().add(title);

        final GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid");

        gridPane.add(new TextWithStyle(translate("Theme:"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        themes = new ComboBox<>();
        themes.getItems().setAll(Theme.values());
        themes.setOnAction(event -> {
            this.handleThemeChange(event);
            this.save();}
        );
        gridPane.add(themes, 1, 0);

        gridPane.setHgap(20);
        gridPane.setVgap(10);

        uiPanel.getChildren().add(gridPane);

        final Label restartHint = new Label(translate("If you change the theme, please restart to load the icons of the new theme."));
        restartHint.setPadding(new Insets(10));
        uiPanel.getChildren().add(restartHint);
    }

    private void initRepositoriesSettingsPane() {
        repositoriesPanel = new VBox();
        repositoriesPanel.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);

        final Text title = new TextWithStyle(translate("Repositories Settings"), TITLE_CSS_CLASS);
        repositoriesPanel.getChildren().add(title);

        final GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid");

        TextWithStyle repositoryText = new TextWithStyle(translate("Repository:"), CAPTION_TITLE_CSS_CLASS);
        gridPane.add(repositoryText, 0, 0);
        GridPane.setValignment(repositoryText, VPos.TOP);
        VBox repositoryLayout = new VBox();
        repositoryLayout.setSpacing(5);
        ListView<String> repositoryListView = new ListView<>(repositories);
        repositoryListView.setPrefSize(400, 100);
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
            dialog.initOwner(getContent().getScene().getWindow());
            dialog.setTitle("Add repository");
            dialog.setHeaderText("Add repository");
            dialog.setContentText("Please add the new repository:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(repositories::add);
            this.save();
        });
        Button removeButton = new Button();
        removeButton.setText("Remove");
        removeButton.setOnAction((ActionEvent event) -> {
            repositories.removeAll(repositoryListView.getSelectionModel().getSelectedItems());
            this.save();
        });
        repositoryButtonLayout.getChildren().addAll(addButton, removeButton);
        repositoryLayout.getChildren().addAll(repositoryListView, repositoryButtonLayout);
        gridPane.add(repositoryLayout, 1, 0);

        gridPane.setHgap(20);
        gridPane.setVgap(10);

        repositoriesPanel.getChildren().add(gridPane);
    }

    private void initFileAssociationsPane() {

    }

    private void initNetworkPane() {

    }

    public void setSettings(Settings settings) {
        final Theme setTheme = Theme.fromShortName(settings.get(Setting.THEME));
        themes.setValue(setTheme);

        repositories.addAll(settings.get(Setting.REPOSITORY).split(";"));
    }

    public void setOnSave(Consumer<Settings> onSave) {
        this.onSave = onSave;
    }

    private void initSelectSettingsPane() {
        this.selectSettingsPanel = new MessagePanel(translate("Please select a settings category"));
    }

    private void save() {
        settings.set(Setting.THEME, themes.getSelectionModel().getSelectedItem().getShortName());

        StringBuilder stringBuilder = new StringBuilder();
        for (String repository : repositories) {
            stringBuilder.append(repository).append(";");
        }
        settings.set(Setting.REPOSITORY, stringBuilder.toString());

        onSave.accept(this.settings);
    }

    private void handleThemeChange(ActionEvent evt) {
        final Theme theme = themes.getSelectionModel().getSelectedItem();
        themeManager.setCurrentTheme(theme);
        final String shortName = theme.getShortName();
        final String url = String.format("/org/phoenicis/javafx/themes/%s/main.css", shortName);
        final URL style = this.getClass().getResource(url);
        getContent().getScene().getStylesheets().clear();
        getContent().getScene().getStylesheets().add(style.toExternalForm());
    }
}
