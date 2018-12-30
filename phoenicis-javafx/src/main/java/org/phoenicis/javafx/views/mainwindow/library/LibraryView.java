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

package org.phoenicis.javafx.views.mainwindow.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.phoenicis.javafx.collections.ExpandedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutCreationDTO;
import org.phoenicis.library.dto.ShortcutDTO;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class LibraryView extends MainWindowView<LibrarySidebar> {
    private final String applicationName;
    private final LibraryFilter filter;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private LibraryPanel libraryPanel;
    private CreateShortcutPanel createShortcutPanel;
    private EditShortcutPanel editShortcutPanel;
    private final Tab installedApplicationsTab;

    private CombinedListWidget<ShortcutDTO> availableShortcuts;

    private ObservableList<ShortcutCategoryDTO> categories;

    private TabPane libraryTabs;
    private Runnable onTabOpened;
    private Consumer<ShortcutDTO> onShortcutDoubleClicked;

    public LibraryView(String applicationName, String containersPath, ThemeManager themeManager,
            ObjectMapper objectMapper, JavaFxSettingsManager javaFxSettingsManager) {
        super(tr("Library"), themeManager);

        this.applicationName = applicationName;
        this.javaFxSettingsManager = javaFxSettingsManager;

        this.categories = FXCollections.observableArrayList();

        this.filter = new LibraryFilter();

        this.getStyleClass().add("mainWindowScene");

        this.availableShortcuts = createShortcutListWidget();

        this.filter.selectedShortcutCategoryProperty().addListener((Observable invalidation) -> closeDetailsView());

        LibrarySidebar sidebar = createLibrarySidebar();
        sidebar.setOnCreateShortcut(this::showShortcutCreate);

        setSidebar(sidebar);

        this.libraryTabs = new TabPane();
        this.libraryTabs.getStyleClass().add("rightPane");

        this.installedApplicationsTab = new Tab();
        this.installedApplicationsTab.setClosable(false);
        this.installedApplicationsTab.setText(tr("My applications"));
        this.installedApplicationsTab.setContent(this.availableShortcuts);

        this.libraryTabs.getTabs().add(this.installedApplicationsTab);

        setCenter(this.libraryTabs);

        this.libraryPanel = new LibraryPanel(objectMapper);
        this.createShortcutPanel = new CreateShortcutPanel(containersPath);
        this.editShortcutPanel = new EditShortcutPanel(objectMapper);
    }

    private CombinedListWidget<ShortcutDTO> createShortcutListWidget() {
        final FilteredList<ShortcutDTO> filteredShortcuts = new ExpandedList<>(
                this.categories.sorted(Comparator.comparing(ShortcutCategoryDTO::getName)),
                ShortcutCategoryDTO::getShortcuts)
                        .filtered(this.filter::filter);

        filteredShortcuts.predicateProperty().bind(
                Bindings.createObjectBinding(() -> this.filter::filter,
                        this.filter.searchTermProperty(), this.filter.selectedShortcutCategoryProperty()));

        final SortedList<ShortcutDTO> sortedShortcuts = filteredShortcuts
                .sorted(Comparator.comparing(shortcut -> shortcut.getInfo().getName()));

        final ObservableList<ListWidgetElement<ShortcutDTO>> listWidgetEntries = new MappedList<>(sortedShortcuts,
                ListWidgetElement::create);

        final CombinedListWidget<ShortcutDTO> combinedListWidget = new CombinedListWidget<>(listWidgetEntries);

        combinedListWidget.selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final ShortcutDTO selectedItem = newValue.getItem();
                final MouseEvent event = newValue.getEvent();

                if (event.getButton() == MouseButton.PRIMARY) {
                    showShortcutDetails(selectedItem);

                    if (event.getClickCount() == 2) {
                        this.onShortcutDoubleClicked.accept(selectedItem);
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    final MenuItem edit = new MenuItem("Edit");
                    edit.setOnAction(editEvent -> showShortcutEdit(selectedItem));

                    final ContextMenu contextMenu = new ContextMenu(edit);
                    // show context menu
                    contextMenu.show(this.availableShortcuts, event.getScreenX(), event.getScreenY());
                }
            }
        });

        return combinedListWidget;
    }

    private LibrarySidebar createLibrarySidebar() {
        final SortedList<ShortcutCategoryDTO> sortedCategories = this.categories
                .sorted(Comparator.comparing(ShortcutCategoryDTO::getName));

        return new LibrarySidebar(this.applicationName, this.filter, this.javaFxSettingsManager, sortedCategories,
                this.availableShortcuts);
    }

    public void setOnShortcutDoubleClicked(Consumer<ShortcutDTO> onShortcutDoubleClicked) {
        this.onShortcutDoubleClicked = onShortcutDoubleClicked;
    }

    public void setOnShortcutStop(Consumer<ShortcutDTO> onShortcutStop) {
        this.libraryPanel.setOnShortcutStop(onShortcutStop);
    }

    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.libraryPanel.setOnShortcutRun(onShortcutRun);
    }

    public void populate(List<ShortcutCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);

            closeDetailsView();
            this.installedApplicationsTab.setContent(this.availableShortcuts);
        });
    }

    private void showShortcutDetails(ShortcutDTO shortcutDTO) {
        this.libraryPanel.setOnClose(this::closeDetailsView);
        this.libraryPanel.setShortcutDTO(shortcutDTO);
        this.libraryPanel.prefWidthProperty().bind(this.getTabPane().widthProperty().divide(3));
        this.showDetailsView(this.libraryPanel);
    }

    /**
     * shows a details view which allows to create a new shortcut
     */
    private void showShortcutCreate() {
        this.createShortcutPanel.setOnClose(this::closeDetailsView);
        this.createShortcutPanel.setMaxWidth(600);
        this.createShortcutPanel.prefWidthProperty().bind(this.getTabPane().widthProperty().divide(3));
        this.createShortcutPanel.populate();

        showDetailsView(this.createShortcutPanel);
    }

    /**
     * shows a details view which allows to edit the given shortcut
     *
     * @param shortcutDTO
     */
    private void showShortcutEdit(ShortcutDTO shortcutDTO) {
        this.editShortcutPanel.setOnClose(this::closeDetailsView);
        this.editShortcutPanel.setShortcutDTO(shortcutDTO);
        this.editShortcutPanel.setMaxWidth(600);
        this.editShortcutPanel.prefWidthProperty().bind(this.getTabPane().widthProperty().divide(3));

        showDetailsView(this.editShortcutPanel);
    }

    public void createNewTab(Tab tab) {
        this.libraryTabs.getTabs().add(tab);
        this.libraryTabs.getSelectionModel().select(tab);
        if(this.onTabOpened != null) {
            this.onTabOpened.run();
        }
    }

    public void setOnTabOpened(Runnable onTabOpened) {
        this.onTabOpened = onTabOpened;
    }

    public void setOnOpenConsole(Runnable onOpenConsole) {
        this.sidebar.setOnOpenConsole(onOpenConsole);
    }

    public void setOnShortcutCreate(Consumer<ShortcutCreationDTO> onShortcutCreate) {
        this.createShortcutPanel.setOnCreateShortcut(onShortcutCreate);
    }

    public void setOnShortcutUninstall(Consumer<ShortcutDTO> onShortcutUninstall) {
        this.libraryPanel.setOnShortcutUninstall(onShortcutUninstall);
    }

    public void setOnShortcutChanged(Consumer<ShortcutDTO> onShortcutChanged) {
        this.editShortcutPanel.setOnShortcutChanged(onShortcutChanged);
    }

    public void setOnScriptRun(Consumer<File> onScriptRun) {
        this.sidebar.setOnScriptRun(onScriptRun);
    }
}
