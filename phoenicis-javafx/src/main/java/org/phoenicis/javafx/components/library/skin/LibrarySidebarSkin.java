package org.phoenicis.javafx.components.library.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.FileChooser;
import org.phoenicis.javafx.components.common.control.SidebarGroup;
import org.phoenicis.javafx.components.common.skin.ExtendedSidebarSkinBase;
import org.phoenicis.javafx.components.library.control.LibrarySidebar;
import org.phoenicis.javafx.components.library.control.LibrarySidebarToggleGroup;
import org.phoenicis.library.dto.ShortcutCategoryDTO;

import java.io.File;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link LibrarySidebar} component
 */
public class LibrarySidebarSkin
        extends ExtendedSidebarSkinBase<ShortcutCategoryDTO, LibrarySidebar, LibrarySidebarSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public LibrarySidebarSkin(LibrarySidebar control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ScrollPane createMainContent() {
        if (getControl().getJavaFxSettingsManager().isAdvancedMode()) {
            return createScrollPane(createSidebarToggleGroup(), createSpacer(), createAdvancedToolsGroup());
        } else {
            return createScrollPane(createSidebarToggleGroup());
        }
    }

    /**
     * Creates the {@link LibrarySidebarToggleGroup} which contains all known shortcut categories
     */
    private LibrarySidebarToggleGroup createSidebarToggleGroup() {
        final FilteredList<ShortcutCategoryDTO> filteredShortcutCategories = getControl().getItems()
                .filtered(getControl().getFilter()::filter);

        filteredShortcutCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl().getFilter()::filter,
                        getControl().searchTermProperty()));

        final LibrarySidebarToggleGroup categoryView = new LibrarySidebarToggleGroup(tr("Categories"),
                filteredShortcutCategories);

        getControl().selectedShortcutCategoryProperty().bind(categoryView.selectedElementProperty());

        return categoryView;
    }

    /**
     * Creates a {@link SidebarGroup} which contains the advanced tool buttons to:
     * <ul>
     * <li>create a new shortcut</li>
     * <li>run a script</li>
     * <li>open a Phoenicis console</li>
     * </ul>
     *
     * @return The created {@link SidebarGroup} object
     */
    private SidebarGroup<Button> createAdvancedToolsGroup() {
        final Button createShortcut = new Button(tr("Create shortcut"));
        createShortcut.getStyleClass().addAll("sidebarButton", "openTerminal");
        createShortcut.setOnMouseClicked(
                event -> Optional.ofNullable(getControl().getOnCreateShortcut()).ifPresent(Runnable::run));

        final Button runScript = new Button(tr("Run a script"));
        runScript.getStyleClass().addAll("sidebarButton", "scriptButton");
        runScript.setOnMouseClicked(event -> {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(tr("Open Script..."));

            final File scriptToRun = fileChooser.showOpenDialog(getControl().getScene().getWindow());

            if (scriptToRun != null) {
                Optional.ofNullable(getControl().getOnScriptRun())
                        .ifPresent(onScriptRun -> onScriptRun.accept(scriptToRun));
            }
        });

        final Button runConsole = new Button(tr("{0} console", getControl().getApplicationName()));
        runConsole.getStyleClass().addAll("sidebarButton", "consoleButton");
        runConsole.setOnMouseClicked(
                event -> Optional.ofNullable(getControl().getOnOpenConsole()).ifPresent(Runnable::run));

        final SidebarGroup<Button> advancedToolsGroup = new SidebarGroup<>(tr("Advanced Tools"));
        advancedToolsGroup.getComponents().addAll(createShortcut, /* runScript, */runConsole);

        return advancedToolsGroup;
    }
}
