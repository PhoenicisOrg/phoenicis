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

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.phoenicis.javafx.views.common.ExpandedList;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class ViewLibrary extends MainWindowView<LibrarySideBar> {
    private final Logger LOGGER = LoggerFactory.getLogger(ViewLibrary.class);

    private LibrarySideBar sideBar;

    private CombinedListWidget<ShortcutDTO> availableShortcuts;
    private final ShortcutFilter<ShortcutDTO> filter;

    private ObservableList<ShortcutCategoryDTO> categories;
    private SortedList<ShortcutCategoryDTO> sortedCategories;

    private ObservableList<ShortcutDTO> shortcuts;
    private FilteredList<ShortcutDTO> filteredShortcuts;
    private SortedList<ShortcutDTO> sortedShortcuts;

    private TabPane libraryTabs;
    private Runnable onTabOpened = () -> {
    };

    private Consumer<ShortcutDTO> onShortcutSelected = shortcut -> {
    };
    private Consumer<ShortcutDTO> onShortcutDoubleClicked = shortcut -> {
    };

    public ViewLibrary(String applicationName, ThemeManager themeManager) {
        super("Library", themeManager);
        this.getStyleClass().add("mainWindowScene");

        availableShortcuts = new CombinedListWidget<>(ListWidgetEntry::create, (selectedItem, event) -> {
            ShortcutDTO shortcutDTO = selectedItem;

            availableShortcuts.deselectAll();
            availableShortcuts.select(selectedItem);
            onShortcutSelected.accept(shortcutDTO);

            sideBar.showShortcut(shortcutDTO);

            if (event.getClickCount() == 2) {
                onShortcutDoubleClicked.accept(shortcutDTO);
            }

            event.consume();
        });

        // initialising the category lists
        this.categories = FXCollections.observableArrayList();
        this.sortedCategories = this.categories.sorted(Comparator.comparing(ShortcutCategoryDTO::getName));

        // initialising the shortcut lists
        this.shortcuts = new ExpandedList<ShortcutDTO, ShortcutCategoryDTO>(this.sortedCategories,
                ShortcutCategoryDTO::getShortcuts);
        this.filteredShortcuts = new FilteredList<ShortcutDTO>(this.shortcuts);
        this.sortedShortcuts = this.filteredShortcuts.sorted(Comparator.comparing(ShortcutDTO::getName));

        this.filter = new ShortcutFilter<ShortcutDTO>(filteredShortcuts,
                (filterText, shortcut) -> shortcut.getName().toLowerCase().contains(filterText));

        availableShortcuts.setOnMouseClicked(event -> {
            sideBar.hideShortcut();
            availableShortcuts.deselectAll();
            onShortcutSelected.accept(null);
            event.consume();
        });

        this.sideBar = new LibrarySideBar(applicationName);
        this.sideBar.bindCategories(this.sortedCategories);

        this.availableShortcuts.bind(sortedShortcuts);

        // set the category selection consumers
        this.sideBar.setOnCategorySelection(category -> {
            filter.setFilters(category.getShortcuts()::contains);
            showAvailableShortcuts();
        });
        this.sideBar.setOnAllCategorySelection(() -> {
            filter.clearFilters();
            showAvailableShortcuts();
        });

        this.drawContent();

        this.setSideBar(sideBar);
    }

    public void setOnShortcutSelected(Consumer<ShortcutDTO> onShortcutSelected) {
        this.onShortcutSelected = onShortcutSelected;
    }

    public void setOnShortcutDoubleClicked(Consumer<ShortcutDTO> onShortcutDoubleClicked) {
        this.onShortcutDoubleClicked = onShortcutDoubleClicked;
    }

    public void setOnShortcutStop(Consumer<ShortcutDTO> onShortcutStop) {
        this.sideBar.setOnShortcutStop(onShortcutStop);
    }

    public void setOnSearch(Consumer<String> onSearch) {
        this.sideBar.setOnSearch(onSearch);
    }

    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.sideBar.setOnShortcutRun(onShortcutRun);
    }

    /**
     * Show available apps panel
     */
    public void showAvailableShortcuts() {
        this.closeDetailsView();
        drawContent();
    }

    public void populate(List<ShortcutCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);
            this.filter.clearAll();
            this.sideBar.selectAllCategories();

            this.showAvailableShortcuts();
        });
    }

    private void drawContent() {
        libraryTabs = new TabPane();
        libraryTabs.getStyleClass().add("rightPane");

        final Tab installedApplication = new Tab();
        installedApplication.setClosable(false);
        installedApplication.setText(translate("My applications"));
        libraryTabs.getTabs().add(installedApplication);

        installedApplication.setContent(availableShortcuts);

        this.setCenter(libraryTabs);
    }

    public void createNewTab(Tab tab) {
        libraryTabs.getTabs().add(tab);
        libraryTabs.getSelectionModel().select(tab);
        onTabOpened.run();
    }

    public void closeTab(Tab tab) {
        libraryTabs.getTabs().remove(tab);
    }

    public void setOnTabOpened(Runnable onTabOpened) {
        this.onTabOpened = onTabOpened;
    }

    public void setOnOpenConsole(Runnable onOpenConsole) {
        this.sideBar.setOnOpenConsole(onOpenConsole);
    }

    public void setOnShortcutUninstall(Consumer<ShortcutDTO> onShortcutUninstall) {
        this.sideBar.setOnShortcutUninstall(onShortcutUninstall);
    }

    public void setOnScriptRun(Consumer<File> onScriptRun) {
        this.sideBar.setOnScriptRun(onScriptRun);
    }
}
