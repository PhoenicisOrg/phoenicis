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
import org.apache.commons.lang.StringUtils;
import org.graalvm.polyglot.Value;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.library.control.LibrarySidebar;
import org.phoenicis.javafx.components.library.control.ShortcutCreationPanel;
import org.phoenicis.javafx.components.library.control.ShortcutEditingPanel;
import org.phoenicis.javafx.components.library.control.ShortcutInformationPanel;
import org.phoenicis.javafx.dialogs.SimpleConfirmDialog;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.javafx.utils.StringBindings;
import org.phoenicis.javafx.utils.SwitchBinding;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;
import org.phoenicis.library.ShortcutManager;
import org.phoenicis.library.ShortcutRunner;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutCreationDTO;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.scripts.session.InteractiveScriptSession;
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

    private final ObjectProperty<ListWidgetType> selectedListWidget;

    private final ObjectProperty<Runnable> onOpenConsole;

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
        this.selectedListWidget = new SimpleObjectProperty<>();
        this.onOpenConsole = new SimpleObjectProperty<>();
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

        final DetailsPanel shortcutInformationPanel = createShortcutInformationDetailsPanel();
        final DetailsPanel shortcutCreationPanel = createShortcutCreationDetailsPanel();
        final DetailsPanel shortcutEditingPanel = createShortcutEditingDetailsPanel();

        final LibrarySidebar sidebar = createLibrarySidebar();

        this.setSidebar(sidebar);

        this.setCenter(this.libraryTabs);

        this.content.rightProperty().bind(SwitchBinding.<LibraryDetailsPanels, Node> builder(selectedDetailsPanel)
                .withCase(LibraryDetailsPanels.ShortcutDetails, shortcutInformationPanel)
                .withCase(LibraryDetailsPanels.ShortcutCreation, shortcutCreationPanel)
                .withCase(LibraryDetailsPanels.ShortcutEditing, shortcutEditingPanel)
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

        final CombinedListWidget<ShortcutDTO> combinedListWidget = new CombinedListWidget<>(listWidgetEntries);

        combinedListWidget.selectedListWidgetProperty().bind(this.selectedListWidget);

        return combinedListWidget;
    }

    private LibrarySidebar createLibrarySidebar() {
        final SortedList<ShortcutCategoryDTO> sortedCategories = this.categories
                .sorted(Comparator.comparing(ShortcutCategoryDTO::getName));

        final LibrarySidebar sidebar = new LibrarySidebar(this.filter, sortedCategories, selectedListWidget);

        sidebar.setApplicationName(this.applicationName);

        sidebar.setOnCreateShortcut(this::showShortcutCreate);
        sidebar.onOpenConsoleProperty().bind(this.onOpenConsole);

        // set the default selection
        sidebar.setSelectedListWidget(javaFxSettingsManager.getLibraryListType());

        // save changes to the list widget selection to the hard drive
        sidebar.selectedListWidgetProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                javaFxSettingsManager.setLibraryListType(newValue);
                javaFxSettingsManager.save();
            }
        });

        return sidebar;
    }

    private DetailsPanel createShortcutCreationDetailsPanel() {
        final ShortcutCreationPanel shortcutCreationPanel = new ShortcutCreationPanel();

        shortcutCreationPanel.setOnCreateShortcut(this::createShortcut);

        shortcutCreationPanel.setContainersPath(containersPath);

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.setTitle(tr("Create a new shortcut"));
        detailsPanel.setContent(shortcutCreationPanel);
        detailsPanel.setOnClose(this::closeDetailsView);

        detailsPanel.prefWidthProperty().bind(content.widthProperty().divide(3));

        return detailsPanel;
    }

    private DetailsPanel createShortcutEditingDetailsPanel() {
        final ShortcutEditingPanel shortcutEditingPanel = new ShortcutEditingPanel();

        shortcutEditingPanel.setOnShortcutChanged(shortcutManager::updateShortcut);
        shortcutEditingPanel.setObjectMapper(objectMapper);

        this.selectedShortcut.addListener((Observable invalidation) -> {
            final ShortcutDTO shortcut = selectedShortcut.getValue();

            shortcutEditingPanel.setShortcut(shortcut);
        });

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty()
                .bind(StringBindings.map(this.selectedShortcut, shortcut -> shortcut.getInfo().getName()));
        detailsPanel.setContent(shortcutEditingPanel);
        detailsPanel.setOnClose(this::closeDetailsView);

        detailsPanel.prefWidthProperty().bind(content.widthProperty().divide(3));

        return detailsPanel;
    }

    private DetailsPanel createShortcutInformationDetailsPanel() {
        final ShortcutInformationPanel shortcutInformationPanel = new ShortcutInformationPanel();

        shortcutInformationPanel.shortcutProperty().bind(this.selectedShortcut);
        shortcutInformationPanel.setObjectMapper(objectMapper);

        shortcutInformationPanel.setOnShortcutRun(this::runShortcut);
        shortcutInformationPanel.setOnShortcutStop(this::stopShortcut);
        shortcutInformationPanel.setOnShortcutUninstall(this::uninstallShortcut);

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty()
                .bind(StringBindings.map(this.selectedShortcut, shortcut -> shortcut.getInfo().getName()));
        detailsPanel.setContent(shortcutInformationPanel);
        detailsPanel.setOnClose(this::closeDetailsView);

        detailsPanel.prefWidthProperty().bind(content.widthProperty().divide(3));

        return detailsPanel;
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
        final String engine = StringUtils.capitalize(engineContainer).replace("prefix", "");
        // TODO: better way to get engine ID
        final String engineId = engine.toLowerCase();
        final String container = split[1];

        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        final String scriptInclude = "const Shortcut = include(\"engines." + engineId + "\".shortcuts." + engineId
                + "\");";

        interactiveScriptSession.eval(scriptInclude,
                ignored -> interactiveScriptSession.eval("new " + engine + "Shortcut()",
                        output -> {
                            final Value shortcutObject = (Value) output;

                            shortcutObject.invokeMember("name", shortcutCreationDTO.getName());
                            shortcutObject.invokeMember("category", shortcutCreationDTO.getCategory());
                            shortcutObject.invokeMember("description", shortcutCreationDTO.getDescription());
                            shortcutObject.invokeMember("miniature", shortcutCreationDTO.getMiniature());
                            shortcutObject.invokeMember("search", shortcutCreationDTO.getExecutable().getName());
                            shortcutObject.invokeMember("prefix", container);
                            shortcutObject.invokeMember("create");
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
        shortcutRunner.run(shortcut, Collections.emptyList(), e -> Platform.runLater(() -> {
            final ErrorDialog errorDialog = ErrorDialog.builder()
                    .withMessage(tr("Error"))
                    .withException(e)
                    .withOwner(content.getScene().getWindow())
                    .build();

            errorDialog.showAndWait();
        }));
    }

    private void stopShortcut(ShortcutDTO shortcut) {
        shortcutRunner.stop(shortcut, e -> Platform.runLater(() -> {
            final ErrorDialog errorDialog = ErrorDialog.builder()
                    .withMessage(tr("Error"))
                    .withException(e)
                    .withOwner(content.getScene().getWindow())
                    .build();

            errorDialog.showAndWait();
        }));
    }

    private void uninstallShortcut(ShortcutDTO shortcut) {
        final String shortcutName = shortcut.getInfo().getName();

        final SimpleConfirmDialog confirmMessage = SimpleConfirmDialog.builder()
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
        this.onOpenConsole.setValue(onOpenConsole);
    }

    private enum LibraryDetailsPanels {
        ShortcutDetails, ShortcutCreation, ShortcutEditing, Closed;
    }
}
