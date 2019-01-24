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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;
import org.phoenicis.javafx.components.library.control.ShortcutCreationDetailsPanel;
import org.phoenicis.javafx.components.library.control.ShortcutDetailsPanel;
import org.phoenicis.javafx.components.library.control.ShortcutEditingDetailsPanel;
import org.phoenicis.javafx.dialogs.ConfirmDialog;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.utils.SwitchBindingBuilder;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;
import org.phoenicis.library.ShortcutManager;
import org.phoenicis.library.ShortcutRunner;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutCreationDTO;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class LibraryView extends MainWindowView<LibrarySidebar> {
    private final String applicationName;
    private final LibraryFilter filter;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private final String containersPath;

    private final ScriptInterpreter scriptInterpreter;

    private final ObjectMapper objectMapper;

    private final ShortcutRunner shortcutRunner;

    private final ShortcutManager shortcutManager;

    private final ObservableList<ShortcutCategoryDTO> categories;

    private final ObjectProperty<LibraryDetailsPanels> selectedDetailsPanel;

    private final ObjectProperty<ListWidgetSelection<ShortcutDTO>> listWidgetSelection;

    private final ObjectProperty<ShortcutDTO> selectedShortcut;

    private final TabPane libraryTabs;

    public LibraryView(String applicationName, String containersPath, ThemeManager themeManager,
            ScriptInterpreter scriptInterpreter,
            ObjectMapper objectMapper, ShortcutRunner shortcutRunner, ShortcutManager shortcutManager,
            JavaFxSettingsManager javaFxSettingsManager) {
        super(tr("Library"), themeManager);

        this.applicationName = applicationName;
        this.javaFxSettingsManager = javaFxSettingsManager;
        this.containersPath = containersPath;
        this.scriptInterpreter = scriptInterpreter;
        this.objectMapper = objectMapper;
        this.shortcutRunner = shortcutRunner;
        this.shortcutManager = shortcutManager;

        this.categories = FXCollections.observableArrayList();
        this.selectedShortcut = new SimpleObjectProperty<>();
        this.selectedDetailsPanel = new SimpleObjectProperty<>();

        this.filter = new LibraryFilter();

        this.getStyleClass().add("mainWindowScene");

        final CombinedListWidget<ShortcutDTO> availableShortcuts = createShortcutListWidget();

        this.listWidgetSelection = availableShortcuts.selectedElementProperty();

        this.listWidgetSelection.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final ShortcutDTO selectedItem = newValue.getItem();
                final MouseEvent event = newValue.getEvent();

                this.selectedShortcut.setValue(selectedItem);

                if (event.getButton() == MouseButton.PRIMARY) {
                    this.selectedDetailsPanel.setValue(LibraryDetailsPanels.ShortcutDetails);

                    if (event.getClickCount() == 2) {
                        runShortcut(selectedItem);
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    final MenuItem edit = new MenuItem(tr("Edit"));
                    edit.setOnAction(
                            editEvent -> this.selectedDetailsPanel.setValue(LibraryDetailsPanels.ShortcutEditing));

                    final ContextMenu contextMenu = new ContextMenu(edit);
                    // show context menu
                    contextMenu.show(availableShortcuts, event.getScreenX(), event.getScreenY());
                }
            } else {
                this.selectedShortcut.setValue(null);
                this.selectedDetailsPanel.setValue(LibraryDetailsPanels.Closed);
            }
        });

        this.filter.selectedShortcutCategoryProperty().addListener((Observable invalidation) -> closeDetailsView());

        final Tab installedApplicationsTab = new Tab();
        installedApplicationsTab.setClosable(false);
        installedApplicationsTab.setText(tr("My applications"));
        installedApplicationsTab.setContent(availableShortcuts);

        this.libraryTabs = new TabPane();
        this.libraryTabs.getStyleClass().add("rightPane");
        this.libraryTabs.getTabs().add(installedApplicationsTab);

        final ShortcutDetailsPanel shortcutDetailsPanel = createShortcutDetailsPanel();
        final ShortcutCreationDetailsPanel shortcutCreationDetailsPanel = createShortcutCreationPanel();
        final ShortcutEditingDetailsPanel shortcutEditingDetailsPanel = createShortcutEditingPanel();

        this.selectedShortcut.addListener((Observable invalidation) -> {
            final ShortcutDTO shortcut = selectedShortcut.getValue();

            shortcutDetailsPanel.setShortcut(shortcut);
            shortcutEditingDetailsPanel.setShortcut(shortcut);
        });

        final LibrarySidebar sidebar = createLibrarySidebar(availableShortcuts);

        this.setSidebar(sidebar);

        this.setCenter(this.libraryTabs);

        this.content.rightProperty().bind(new SwitchBindingBuilder<LibraryDetailsPanels, Node>(selectedDetailsPanel)
                .withCase(LibraryDetailsPanels.ShortcutDetails, shortcutDetailsPanel)
                .withCase(LibraryDetailsPanels.ShortcutCreation, shortcutCreationDetailsPanel)
                .withCase(LibraryDetailsPanels.ShortcutEditing, shortcutEditingDetailsPanel)
                .withCase(LibraryDetailsPanels.Closed, new SimpleObjectProperty<>())
                .build());
    }

    private CombinedListWidget<ShortcutDTO> createShortcutListWidget() {
        final FilteredList<ShortcutDTO> filteredShortcuts = ConcatenatedList
                .create(new MappedList<>(
                        this.categories.sorted(Comparator.comparing(ShortcutCategoryDTO::getName)),
                        ShortcutCategoryDTO::getShortcuts))
                .filtered(this.filter::filter);

        filteredShortcuts.predicateProperty().bind(
                Bindings.createObjectBinding(() -> this.filter::filter,
                        this.filter.searchTermProperty(), this.filter.selectedShortcutCategoryProperty()));

        final SortedList<ShortcutDTO> sortedShortcuts = filteredShortcuts
                .sorted(Comparator.comparing(shortcut -> shortcut.getInfo().getName()));

        final ObservableList<ListWidgetElement<ShortcutDTO>> listWidgetEntries = new MappedList<>(sortedShortcuts,
                ListWidgetElement::create);

        return new CombinedListWidget<>(listWidgetEntries);
    }

    private LibrarySidebar createLibrarySidebar(CombinedListWidget<ShortcutDTO> availableShortcuts) {
        final SortedList<ShortcutCategoryDTO> sortedCategories = this.categories
                .sorted(Comparator.comparing(ShortcutCategoryDTO::getName));

        final LibrarySidebar librarySidebar = new LibrarySidebar(this.applicationName, this.filter,
                this.javaFxSettingsManager, sortedCategories, availableShortcuts);

        librarySidebar.setOnCreateShortcut(this::showShortcutCreate);

        return librarySidebar;
    }

    private ShortcutCreationDetailsPanel createShortcutCreationPanel() {
        final ShortcutCreationDetailsPanel shortcutCreationDetailsPanel = new ShortcutCreationDetailsPanel();

        shortcutCreationDetailsPanel.setOnClose(this::closeDetailsView);

        shortcutCreationDetailsPanel.setOnCreateShortcut(this::createShortcut);

        shortcutCreationDetailsPanel.setContainersPath(containersPath);

        shortcutCreationDetailsPanel.prefWidthProperty().bind(content.widthProperty().divide(3));

        return shortcutCreationDetailsPanel;
    }

    private ShortcutEditingDetailsPanel createShortcutEditingPanel() {
        final ShortcutEditingDetailsPanel shortcutEditingDetailsPanel = new ShortcutEditingDetailsPanel();

        shortcutEditingDetailsPanel.setOnClose(this::closeDetailsView);

        shortcutEditingDetailsPanel.setOnShortcutChanged(shortcutManager::updateShortcut);

        shortcutEditingDetailsPanel.setObjectMapper(objectMapper);

        shortcutEditingDetailsPanel.prefWidthProperty().bind(content.widthProperty().divide(3));

        return shortcutEditingDetailsPanel;
    }

    private ShortcutDetailsPanel createShortcutDetailsPanel() {
        final ShortcutDetailsPanel shortcutDetailsPanel = new ShortcutDetailsPanel();

        shortcutDetailsPanel.setOnClose(this::closeDetailsView);

        shortcutDetailsPanel.setOnShortcutRun(this::runShortcut);
        shortcutDetailsPanel.setOnShortcutStop(this::stopShortcut);
        shortcutDetailsPanel.setOnShortcutUninstall(this::uninstallShortcut);

        shortcutDetailsPanel.setObjectMapper(objectMapper);

        shortcutDetailsPanel.prefWidthProperty().bind(content.widthProperty().divide(3));

        return shortcutDetailsPanel;
    }

    public void populate(List<ShortcutCategoryDTO> categories) {
        Platform.runLater(() -> {
            this.categories.setAll(categories);

            closeDetailsView();
        });
    }

    /**
     * shows a details view which allows to create a new shortcut
     */
    private void showShortcutCreate() {
        closeDetailsView();

        this.selectedDetailsPanel.setValue(LibraryDetailsPanels.ShortcutCreation);
    }

    @Override
    public void closeDetailsView() {
        // deselect the currently selected shortcut
        this.listWidgetSelection.setValue(null);
        // close the details panel
        this.selectedDetailsPanel.setValue(LibraryDetailsPanels.Closed);
    }

    public void createNewTab(Tab tab) {
        this.libraryTabs.getTabs().add(tab);
        this.libraryTabs.getSelectionModel().select(tab);
    }

    /**
     * creates a new shortcut
     *
     * @param shortcutCreationDTO DTO describing the new shortcut
     */
    private void createShortcut(ShortcutCreationDTO shortcutCreationDTO) {
        // get container
        // TODO: smarter way using container manager
        final String executablePath = shortcutCreationDTO.getExecutable().getAbsolutePath();
        final String pathInContainers = executablePath.replace(containersPath, "");
        final String[] split = pathInContainers.split("/");
        final String engineContainer = split[0];
        final String engine = (Character.toUpperCase(engineContainer.charAt(0)) + engineContainer.substring(1))
                .replace("prefix", "");
        // TODO: better way to get engine ID
        final String engineId = engine.toLowerCase();
        final String container = split[1];

        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        final String scriptInclude = "include([\"engines\", \"" + engineId + "\", \"shortcuts\", \"" + engineId
                + "\"]);";

        interactiveScriptSession.eval(scriptInclude,
                ignored -> interactiveScriptSession.eval("new " + engine + "Shortcut()",
                        output -> {
                            final ScriptObjectMirror shortcutObject = (ScriptObjectMirror) output;

                            shortcutObject.callMember("name", shortcutCreationDTO.getName());
                            shortcutObject.callMember("category", shortcutCreationDTO.getCategory());
                            shortcutObject.callMember("description", shortcutCreationDTO.getDescription());
                            shortcutObject.callMember("miniature", shortcutCreationDTO.getMiniature());
                            shortcutObject.callMember("search", shortcutCreationDTO.getExecutable().getName());
                            shortcutObject.callMember("prefix", container);
                            shortcutObject.callMember("create");
                        },
                        e -> Platform.runLater(() -> {
                            final ErrorDialog errorDialog = ErrorDialog.builder()
                                    .withMessage(tr("Error while creating shortcut"))
                                    .withException(e)
                                    .withOwner(getContent().getScene().getWindow())
                                    .build();

                            errorDialog.showAndWait();
                        })),
                e -> Platform.runLater(() -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Error while creating shortcut"))
                            .withException(e)
                            .withOwner(getContent().getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                }));
    }

    private void runShortcut(ShortcutDTO shortcut) {
        shortcutRunner.run(shortcut, Collections.emptyList(), e -> {
            final ErrorDialog errorDialog = ErrorDialog.builder()
                    .withMessage(tr("Error"))
                    .withException(e)
                    .withOwner(content.getScene().getWindow())
                    .build();

            errorDialog.showAndWait();
        });
    }

    private void stopShortcut(ShortcutDTO shortcut) {
        shortcutRunner.stop(shortcut, e -> {
            final ErrorDialog errorDialog = ErrorDialog.builder()
                    .withMessage(tr("Error"))
                    .withException(e)
                    .withOwner(content.getScene().getWindow())
                    .build();

            errorDialog.showAndWait();
        });
    }

    private void uninstallShortcut(ShortcutDTO shortcut) {
        final String shortcutName = shortcut.getInfo().getName();

        final ConfirmDialog confirmMessage = ConfirmDialog.builder()
                .withTitle(tr("Uninstall {0}", shortcutName))
                .withMessage(tr("Are you sure you want to uninstall {0}?", shortcutName))
                .withOwner(content.getScene().getWindow())
                .withResizable(true)
                .withYesCallback(() -> shortcutManager.uninstallFromShortcut(shortcut, e -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Error while uninstalling {0}", shortcutName))
                            .withException(e)
                            .withOwner(content.getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                }))
                .build();

        confirmMessage.showAndCallback();
    }

    public void setOnOpenConsole(Runnable onOpenConsole) {
        this.sidebar.setOnOpenConsole(onOpenConsole);
    }

    private enum LibraryDetailsPanels {
        ShortcutDetails, ShortcutCreation, ShortcutEditing, Closed;
    }
}
