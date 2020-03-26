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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.panelstates.None;
import org.phoenicis.javafx.components.common.panelstates.OpenDetailsPanel;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.control.SidebarBase;
import org.phoenicis.javafx.components.common.skin.FeaturePanelSkin;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.library.panelstates.ShortcutCreation;
import org.phoenicis.javafx.components.library.panelstates.ShortcutInformation;
import org.phoenicis.javafx.components.library.panelstates.ShortcutEditing;
import org.phoenicis.javafx.components.library.control.*;
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
     * The type of the currently shown list widget
     */
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public LibraryFeaturePanelSkin(LibraryFeaturePanel control) {
        super(control);

        this.selectedListWidget = new SimpleObjectProperty<>();
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
            getControl().setSelectedShortcut(null);

            // open the shortcut creation details panel
            getControl().setOpenedDetailsPanel(new ShortcutCreation());
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

        final CombinedListWidget<ShortcutDTO> combinedListWidget = new CombinedListWidget<>(listWidgetEntries,
                this.selectedListWidget);

        // bind direction: controller property -> skin property
        getControl().selectedShortcutProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                combinedListWidget.select(newValue);
            } else {
                combinedListWidget.deselect();
            }
        });

        // bind direction: skin property -> controller properties
        combinedListWidget.selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final ShortcutDTO selectedItem = newValue.getItem();
                final MouseEvent event = newValue.getEvent();

                getControl().setSelectedShortcut(selectedItem);
                getControl().setOpenedDetailsPanel(new ShortcutInformation(selectedItem));

                if (event.getClickCount() == 2) {
                    getControl().runShortcut(selectedItem);
                }
            } else {
                getControl().setSelectedShortcut(null);
                getControl().setOpenedDetailsPanel(new None());
            }
        });

        return combinedListWidget;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectExpression<DetailsPanel> createDetailsPanel() {
        return SwitchBinding
                .<OpenDetailsPanel, DetailsPanel> builder(getControl().openedDetailsPanelProperty())
                .withCase(ShortcutInformation.class, this::createShortcutInformationDetailsPanel)
                .withCase(ShortcutCreation.class, action -> createShortcutCreationDetailsPanel())
                .withCase(ShortcutEditing.class, this::createShortcutEditingDetailsPanel)
                .withCase(None.class, action -> null)
                .build();
    }

    private DetailsPanel createShortcutCreationDetailsPanel() {
        final ShortcutCreationPanel shortcutCreationPanel = new ShortcutCreationPanel();

        shortcutCreationPanel.setOnCreateShortcut(getControl()::createShortcut);

        shortcutCreationPanel.containersPathProperty().bind(getControl().containersPathProperty());

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.setTitle(tr("Create a new shortcut"));
        detailsPanel.setContent(shortcutCreationPanel);
        detailsPanel.setOnClose(getControl()::closeDetailsPanel);

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return detailsPanel;
    }

    private DetailsPanel createShortcutEditingDetailsPanel(ShortcutEditing action) {
        final ShortcutEditingPanel shortcutEditingPanel = new ShortcutEditingPanel();

        shortcutEditingPanel.onShortcutChangedProperty().bind(ObjectBindings
                .map(getControl().shortcutManagerProperty(), shortcutManager -> shortcutManager::updateShortcut));
        shortcutEditingPanel.objectMapperProperty().bind(getControl().objectMapperProperty());

        shortcutEditingPanel.setShortcut(action.getShortcut());

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty().bind(StringBindings
                .map(getControl().selectedShortcutProperty(), shortcut -> shortcut.getInfo().getName()));
        detailsPanel.setContent(shortcutEditingPanel);
        detailsPanel.setOnClose(getControl()::closeDetailsPanel);

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return detailsPanel;
    }

    private DetailsPanel createShortcutInformationDetailsPanel(ShortcutInformation action) {
        final ShortcutInformationPanel shortcutInformationPanel = new ShortcutInformationPanel();

        shortcutInformationPanel.setShortcut(action.getShortcut());
        shortcutInformationPanel.objectMapperProperty().bind(getControl().objectMapperProperty());

        shortcutInformationPanel.setOnShortcutRun(getControl()::runShortcut);
        shortcutInformationPanel.setOnShortcutStop(getControl()::stopShortcut);
        shortcutInformationPanel.setOnShortcutUninstall(getControl()::uninstallShortcut);
        shortcutInformationPanel.setOnShortcutEdit(shortcut -> getControl()
                .setOpenedDetailsPanel(new ShortcutEditing(getControl().getSelectedShortcut())));

        final DetailsPanel detailsPanel = new DetailsPanel();

        detailsPanel.titleProperty().bind(StringBindings
                .map(getControl().selectedShortcutProperty(), shortcut -> shortcut.getInfo().getName()));
        detailsPanel.setContent(shortcutInformationPanel);
        detailsPanel.setOnClose(getControl()::closeDetailsPanel);

        detailsPanel.prefWidthProperty().bind(getControl().widthProperty().divide(3));

        return detailsPanel;
    }
}
