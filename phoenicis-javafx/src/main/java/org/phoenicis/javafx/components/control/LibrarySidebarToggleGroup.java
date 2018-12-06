package org.phoenicis.javafx.components.control;

import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.skin.LibrarySidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.library.LibrarySidebar;
import org.phoenicis.library.dto.ShortcutCategoryDTO;

/**
 * A toggle group component used inside the {@link LibrarySidebar}
 */
public class LibrarySidebarToggleGroup
        extends SidebarToggleGroupBase<ShortcutCategoryDTO, LibrarySidebarToggleGroup, LibrarySidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param title The title of the library sidebar toggle group
     */
    public LibrarySidebarToggleGroup(String title, ObservableList<ShortcutCategoryDTO> elements) {
        super(title, elements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LibrarySidebarToggleGroupSkin createSkin() {
        return new LibrarySidebarToggleGroupSkin(this);
    }
}
