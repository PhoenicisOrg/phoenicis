package org.phoenicis.javafx.components.library.skin;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.control.SidebarBase;
import org.phoenicis.javafx.components.common.skin.FeaturePanelSkin;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.library.control.*;
import org.phoenicis.javafx.components.library.utils.LibraryDetailsPanels;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.utils.ObjectBindings;
import org.phoenicis.javafx.utils.StringBindings;
import org.phoenicis.javafx.utils.SwitchBinding;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutDTO;

import java.util.Comparator;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link LibraryFeaturePanel} component
 */
public class LibraryFeaturePanelSkin extends FeaturePanelSkin<LibraryFeaturePanel, LibraryFeaturePanelSkin> {
    /**
     * The currently selected list widget
     */
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    /**
     * The currently shown details panel
     */
    private final ObjectProperty<LibraryDetailsPanels> selectedDetailsPanel;

    /**
     * The currently selected element inside the list widget
     */
    private final ObjectProperty<ListWidgetSelection<ShortcutDTO>> selectedListWidgetElement;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public LibraryFeaturePanelSkin(LibraryFeaturePanel control) {
        super(control);

        this.selectedListWidget = new SimpleObjectProperty<>();
        this.selectedDetailsPanel = new SimpleObjectProperty<>(LibraryDetailsPanels.Closed);

        this.selectedListWidgetElement = selectedListWidgetElement();
    }

    /**
     * Creates an {@link ObjectProperty} instance for the currently selected element inside the list widgets
     *
     * @return The created {@link ObjectProperty} instance
     */
    private ObjectProperty<ListWidgetSelection<ShortcutDTO>> selectedListWidgetElement() {
        final ObjectProperty<ListWidgetSelection<ShortcutDTO>> selectedListWidgetElement = new SimpleObjectProperty<>();

        selectedListWidgetElement.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final ShortcutDTO selectedItem = newValue.getItem();
                final MouseEvent event = newValue.getEvent();

                getControl().setSelectedShortcut(selectedItem);

                if (event.getButton() == MouseButton.PRIMARY) {
                    this.selectedDetailsPanel.setValue(LibraryDetailsPanels.ShortcutDetails);

                    if (event.getClickCount() == 2) {
                        getControl().runShortcut(selectedItem);
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    final MenuItem edit = new MenuItem(tr("Edit"));
                    edit.setOnAction(
                            editEvent -> this.selectedDetailsPanel.setValue(LibraryDetailsPanels.ShortcutEditing));

                    final ContextMenu contextMenu = new ContextMenu(edit);
                    // show context menu
                    contextMenu.show(getControl(), event.getScreenX(), event.getScreenY());
                }
            } else {
                closeDetailsPanel();
            }
        });

        return selectedListWidgetElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<SidebarBase<?, ?, ?>> createSidebar() {
        final SortedList<ShortcutCategoryDTO> sortedCategories = getControl().getCategories()
                .sorted(Comparator.comparing(ShortcutCategoryDTO::getName));

        final LibrarySidebar sidebar = new LibrarySidebar(getControl().getFilter(), sortedCategories,
                this.selectedListWidget);

        sidebar.applicationNameProperty().bind(getControl().applicationNameProperty());

        sidebar.setOnCreateShortcut(() -> {
            // deselect the currently selected shortcut
            this.selectedListWidgetElement.setValue(null);
            // open the shortcut creation details panel
            this.selectedDetailsPanel.setValue(LibraryDetailsPanels.ShortcutCreation);
        });

        sidebar.setOnOpenConsole(getControl()::openConsole);

        // set the default selection
        sidebar.setSelectedListWidget(Optional
                .ofNullable(getControl().getJavaFxSettingsManager())
                .map(JavaFxSettingsManager::getLibraryListType)
                .orElse(ListWidgetType.ICONS_LIST));

        // save changes to the list widget selection to the hard drive
        sidebar.selectedListWidgetProperty().addListener((observable, oldValue, newValue) -> {
            final JavaFxSettingsManager javaFxSettingsManager = getControl().getJavaFxSettingsManager();

            if (newValue != null) {
                javaFxSettingsManager.setLibraryListType(newValue);
                javaFxSettingsManager.save();
            }
        });

        return new SimpleObjectProperty<>(sidebar);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<Node> createContent() {
        final CombinedListWidget<ShortcutDTO> combinedListWidget = createCombinedListWidget();

        final Tab installedApplicationsTab = new Tab(tr("My applications"), combinedListWidget);

        installedApplicationsTab.setClosable(false);

        final TabPane container = new TabPane();
        container.getStyleClass().add("rightPane");

        getControl().selectedTabProperty().addListener((Observable invalidation) -> {
            final Tab selectedTab = getControl().getSelectedTab();

            if (selectedTab != null) {
                container.getSelectionModel().select(selectedTab);
            } else {
                container.getSelectionModel().selectFirst();
            }
        });

        container.getSelectionModel().selectedItemProperty().addListener((Observable invalidation) -> getControl()
                .setSelectedTab(container.getSelectionModel().getSelectedItem()));

        Bindings.bindContentBidirectional(container.getTabs(), getControl().getTabs());

        container.getTabs().add(installedApplicationsTab);

        return new SimpleObjectProperty<>(container);
    }

    private CombinedListWidget<ShortcutDTO> createCombinedListWidget() {
        final FilteredList<ShortcutDTO> filteredShortcuts = ConcatenatedList
                .create(new MappedList<>(getControl().getCategories()
                        .sorted(Comparator.comparing(ShortcutCategoryDTO::getName)), ShortcutCategoryDTO::getShortcuts))
                .filtered(getControl().getFilter()::filter);

        filteredShortcuts.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl().getFilter()::filter,
                        getControl().getFilter().searchTermProperty(),
                        getControl().getFilter().selectedShortcutCategoryProperty()));

        final SortedList<ShortcutDTO> sortedShortcuts = filteredShortcuts
                .sorted(Comparator.comparing(shortcut -> shortcut.getInfo().getName()));

        final ObservableList<ListWidgetElement<ShortcutDTO>> listWidgetEntries = new MappedList<>(sortedShortcuts,
                ListWidgetElement::create);

        final CombinedListWidget<ShortcutDTO> combinedListWidget = new CombinedListWidget<>(listWidgetEntries);

        combinedListWidget.selectedListWidgetProperty().bind(this.selectedListWidget);

        this.selectedListWidgetElement.bindBidirectional(combinedListWidget.selectedElementProperty());

        return combinedListWidget;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<DetailsPanel> createDetailsPanel() {
        return SwitchBinding.<LibraryDetailsPanels, DetailsPanel> builder(this.selectedDetailsPanel)
                .withCase(LibraryDetailsPanels.ShortcutDetails, createShortcutInformationDetailsPanel())
                .withCase(LibraryDetailsPanels.ShortcutCreation, createShortcutCreationDetailsPanel())
                .withCase(LibraryDetailsPanels.ShortcutEditing, createShortcutEditingDetailsPanel())
                .withCase(LibraryDetailsPanels.Closed, new SimpleObjectProperty<>())
                .build();
    }

    private DetailsPanel createShortcutCreationDetailsPanel() {
        final ShortcutCreationPanel shortcutCreationPanel = new ShortcutCreationPanel();

        shortcutCreationPanel.setOnCreateShortcut(getControl()::createShortcut);

        shortcutCreationPanel.containersPathProperty().bind(getControl().containersPathProperty());

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.setTitle(tr("Create a new shortcut"));
        detailsPanel.setContent(shortcutCreationPanel);
        detailsPanel.setOnClose(this::closeDetailsPanel);

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return detailsPanel;
    }

    private DetailsPanel createShortcutEditingDetailsPanel() {
        final ShortcutEditingPanel shortcutEditingPanel = new ShortcutEditingPanel();

        shortcutEditingPanel.onShortcutChangedProperty().bind(ObjectBindings
                .map(getControl().shortcutManagerProperty(), shortcutManager -> shortcutManager::updateShortcut));
        shortcutEditingPanel.objectMapperProperty().bind(getControl().objectMapperProperty());

        getControl().selectedShortcutProperty().addListener((Observable invalidation) -> {
            final ShortcutDTO shortcut = getControl().getSelectedShortcut();

            shortcutEditingPanel.setShortcut(shortcut);
        });

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty().bind(StringBindings
                .map(getControl().selectedShortcutProperty(), shortcut -> shortcut.getInfo().getName()));
        detailsPanel.setContent(shortcutEditingPanel);
        detailsPanel.setOnClose(this::closeDetailsPanel);

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return detailsPanel;
    }

    private DetailsPanel createShortcutInformationDetailsPanel() {
        final ShortcutInformationPanel shortcutInformationPanel = new ShortcutInformationPanel();

        shortcutInformationPanel.shortcutProperty().bind(getControl().selectedShortcutProperty());
        shortcutInformationPanel.objectMapperProperty().bind(getControl().objectMapperProperty());

        shortcutInformationPanel.setOnShortcutRun(getControl()::runShortcut);
        shortcutInformationPanel.setOnShortcutStop(getControl()::stopShortcut);
        shortcutInformationPanel.setOnShortcutUninstall(getControl()::uninstallShortcut);

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty().bind(StringBindings
                .map(getControl().selectedShortcutProperty(), shortcut -> shortcut.getInfo().getName()));
        detailsPanel.setContent(shortcutInformationPanel);
        detailsPanel.setOnClose(this::closeDetailsPanel);

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return detailsPanel;
    }

    private void closeDetailsPanel() {
        // deselect the currently selected shortcut
        this.selectedListWidgetElement.setValue(null);
        // close the details panel
        this.selectedDetailsPanel.setValue(LibraryDetailsPanels.Closed);
    }
}
