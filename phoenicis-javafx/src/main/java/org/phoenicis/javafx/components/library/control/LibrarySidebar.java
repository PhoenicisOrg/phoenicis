package org.phoenicis.javafx.components.library.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.control.ExtendedSidebarBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;
import org.phoenicis.javafx.components.library.skin.LibrarySidebarSkin;
import org.phoenicis.javafx.views.mainwindow.library.LibraryFilter;
import org.phoenicis.library.dto.ShortcutCategoryDTO;

import java.io.File;
import java.util.function.Consumer;

/**
 * A sidebar implementation for the library tab
 */
public class LibrarySidebar extends ExtendedSidebarBase<ShortcutCategoryDTO, LibrarySidebar, LibrarySidebarSkin> {
    /**
     * The name of the application, i.e. either Phoenicis PlayOnLinux or Phoenicis PlayOnMac
     */
    private final StringProperty applicationName;

    /**
     * The selected shortcut category
     */
    private final ObjectProperty<ShortcutCategoryDTO> selectedShortcutCategory;

    /**
     * The callback method used to create a new shortcut
     */
    private final ObjectProperty<Runnable> onCreateShortcut;

    /**
     * The callback method used to execute a script file.
     * This callback method is currently unused
     */
    private final ObjectProperty<Consumer<File>> onScriptRun;

    /**
     * The callback method used to open a Phoenicis console
     */
    private final ObjectProperty<Runnable> onOpenConsole;

    /**
     * The library filter utility class
     */
    private final LibraryFilter filter;

    /**
     * Constructor
     *
     * @param filter The library filter utility class
     * @param items The items shown inside a toggle button group in the sidebar
     * @param selectedListWidget The currently selected {@link ListWidgetType} by the user
     */
    public LibrarySidebar(LibraryFilter filter, ObservableList<ShortcutCategoryDTO> items,
            ObjectProperty<ListWidgetType> selectedListWidget) {
        super(items, filter.searchTermProperty(), selectedListWidget);

        this.applicationName = new SimpleStringProperty();
        this.onCreateShortcut = new SimpleObjectProperty<>();
        this.onScriptRun = new SimpleObjectProperty<>();
        this.onOpenConsole = new SimpleObjectProperty<>();
        this.filter = filter;

        this.selectedShortcutCategory = filter.selectedShortcutCategoryProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LibrarySidebarSkin createSkin() {
        return new LibrarySidebarSkin(this);
    }

    public String getApplicationName() {
        return this.applicationName.get();
    }

    public StringProperty applicationNameProperty() {
        return this.applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName.set(applicationName);
    }

    public ShortcutCategoryDTO getSelectedShortcutCategory() {
        return this.selectedShortcutCategory.get();
    }

    public ObjectProperty<ShortcutCategoryDTO> selectedShortcutCategoryProperty() {
        return this.selectedShortcutCategory;
    }

    public void setSelectedShortcutCategory(ShortcutCategoryDTO selectedShortcutCategory) {
        this.selectedShortcutCategory.set(selectedShortcutCategory);
    }

    public Runnable getOnCreateShortcut() {
        return this.onCreateShortcut.get();
    }

    public ObjectProperty<Runnable> onCreateShortcutProperty() {
        return this.onCreateShortcut;
    }

    public void setOnCreateShortcut(Runnable onCreateShortcut) {
        this.onCreateShortcut.set(onCreateShortcut);
    }

    public Consumer<File> getOnScriptRun() {
        return this.onScriptRun.get();
    }

    public ObjectProperty<Consumer<File>> onScriptRunProperty() {
        return this.onScriptRun;
    }

    public void setOnScriptRun(Consumer<File> onScriptRun) {
        this.onScriptRun.set(onScriptRun);
    }

    public Runnable getOnOpenConsole() {
        return this.onOpenConsole.get();
    }

    public ObjectProperty<Runnable> onOpenConsoleProperty() {
        return this.onOpenConsole;
    }

    public void setOnOpenConsole(Runnable onOpenConsole) {
        this.onOpenConsole.set(onOpenConsole);
    }

    public LibraryFilter getFilter() {
        return this.filter;
    }
}
