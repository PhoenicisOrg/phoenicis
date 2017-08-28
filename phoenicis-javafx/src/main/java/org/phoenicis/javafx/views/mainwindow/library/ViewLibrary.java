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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ExpandedList;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.SimpleFilter;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutCreationDTO;
import org.phoenicis.library.dto.ShortcutDTO;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ViewLibrary extends MainWindowView<LibrarySidebar> {
    private final SimpleFilter<ShortcutDTO> filter;
    private final ObjectMapper objectMapper;

    private LibraryPanel libraryPanel;
    private CreateShortcutPanel createShortcutPanel;
    private EditShortcutPanel editShortcutPanel;
    private final Tab installedApplicationsTab;

    private CombinedListWidget<ShortcutDTO> availableShortcuts;

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
    private Consumer<ShortcutDTO> onShortcutEdit = shortcut -> {
    };

    public ViewLibrary(String applicationName, ThemeManager themeManager, ObjectMapper objectMapper,
            JavaFxSettingsManager javaFxSettingsManager) {
        super(tr("Library"), themeManager);
        this.getStyleClass().add("mainWindowScene");
        this.objectMapper = objectMapper;

        onShortcutEdit = shortcut -> showShortcutEdit(shortcut);

        availableShortcuts = new CombinedListWidget<>(ListWidgetEntry::create, (selectedItem, event) -> {

            if (event.getButton() == MouseButton.PRIMARY) {
                // select and show details
                availableShortcuts.deselectAll();
                availableShortcuts.select(selectedItem);
                onShortcutSelected.accept(selectedItem);
                showShortcutDetails(selectedItem);

                if (event.getClickCount() == 2) {
                    onShortcutDoubleClicked.accept(selectedItem);
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // show context menu
                final ContextMenu contextMenu = new ContextMenu();
                MenuItem edit = new MenuItem("Edit");
                contextMenu.getItems().addAll(edit);
                edit.setOnAction(editEvent -> onShortcutEdit.accept(selectedItem));
                contextMenu.show(availableShortcuts, event.getScreenX(), event.getScreenY());
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

        this.filter = new SimpleFilter<ShortcutDTO>(filteredShortcuts,
                (filterText, shortcut) -> shortcut.getName().toLowerCase().contains(filterText));

        this.availableShortcuts.setOnMouseClicked(event -> {
            this.availableShortcuts.deselectAll();
            this.onShortcutSelected.accept(null);
            event.consume();
        });

        this.sidebar = new LibrarySidebar(applicationName, availableShortcuts, javaFxSettingsManager);
        this.sidebar.bindCategories(this.sortedCategories);

        // set the category selection consumers
        this.sidebar.setOnCategorySelection(category -> {
            filter.setFilters(category.getShortcuts()::contains);
            this.closeDetailsView();
        });
        this.sidebar.setOnAllCategorySelection(() -> {
            filter.clearFilters();
            this.closeDetailsView();
        });
        this.sidebar.setOnCreateShortcut(this::showShortcutCreate);

        this.setSidebar(this.sidebar);

        this.availableShortcuts.bind(sortedShortcuts);

        this.libraryTabs = new TabPane();
        this.libraryTabs.getStyleClass().add("rightPane");

        this.installedApplicationsTab = new Tab();
        this.installedApplicationsTab.setClosable(false);
        this.installedApplicationsTab.setText(tr("My applications"));
        this.installedApplicationsTab.setContent(availableShortcuts);
        this.libraryTabs.getTabs().add(this.installedApplicationsTab);

        this.setCenter(this.libraryTabs);

        this.libraryPanel = new LibraryPanel(objectMapper);
        this.createShortcutPanel = new CreateShortcutPanel();
        this.editShortcutPanel = new EditShortcutPanel(objectMapper);
    }

    public void setOnShortcutDoubleClicked(Consumer<ShortcutDTO> onShortcutDoubleClicked) {
        this.onShortcutDoubleClicked = onShortcutDoubleClicked;
    }

    public void setOnShortcutStop(Consumer<ShortcutDTO> onShortcutStop) {
        this.libraryPanel.setOnShortcutStop(onShortcutStop);
    }

    public void setOnSearch(Consumer<String> onSearch) {
        this.sidebar.setOnSearch(onSearch);
    }

    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.libraryPanel.setOnShortcutRun(onShortcutRun);
    }

    public void populate(List<ShortcutCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);
            this.filter.clearAll();
            this.sidebar.selectAllCategories();

            this.closeDetailsView();
            this.installedApplicationsTab.setContent(availableShortcuts);
        });
    }

    private void showShortcutDetails(ShortcutDTO shortcutDTO) {
        this.libraryPanel.setOnClose(this::closeDetailsView);
        this.libraryPanel.setShortcutDTO(shortcutDTO);
        this.libraryPanel.setMaxWidth(400);
        this.libraryPanel.prefWidthProperty().bind(this.getTabPane().widthProperty().divide(3));
        this.showDetailsView(libraryPanel);
    }

    /**
     * shows a details view which allows to create a new shortcut
     */
    private void showShortcutCreate() {
        this.createShortcutPanel.setOnClose(this::closeDetailsView);
        this.createShortcutPanel.setMaxWidth(600);
        this.createShortcutPanel.prefWidthProperty().bind(this.getTabPane().widthProperty().divide(3));
        this.createShortcutPanel.populate();
        this.showDetailsView(this.createShortcutPanel);
    }

    /**
     * shows a details view which allows to edit the given shortcut
     * @param shortcutDTO
     */
    private void showShortcutEdit(ShortcutDTO shortcutDTO) {
        this.editShortcutPanel.setOnClose(this::closeDetailsView);
        this.editShortcutPanel.setShortcutDTO(shortcutDTO);
        this.editShortcutPanel.setMaxWidth(600);
        this.editShortcutPanel.prefWidthProperty().bind(this.getTabPane().widthProperty().divide(3));
        this.showDetailsView(this.editShortcutPanel);
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
