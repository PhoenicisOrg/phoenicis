package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.skin.LibrarySidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.library.LibrarySidebar;
import org.phoenicis.library.dto.ShortcutCategoryDTO;

import java.util.function.Consumer;

/**
 * A toggle group component used inside the {@link LibrarySidebar}
 */
public class LibrarySidebarToggleGroup
        extends SidebarToggleGroupBase<ShortcutCategoryDTO, LibrarySidebarToggleGroup, LibrarySidebarToggleGroupSkin> {
    /**
     * A consumer, which is called when the "all" categories button has been selected
     */
    private final ObjectProperty<Runnable> onAllCategorySelection;

    /**
     * A consumer, which is called when a category has been selected
     */
    private final ObjectProperty<Consumer<ShortcutCategoryDTO>> onCategorySelection;

    /**
     * Constructor
     *
     * @param title The title of the library sidebar toggle group
     */
    public LibrarySidebarToggleGroup(String title) {
        super(title);

        this.onAllCategorySelection = new SimpleObjectProperty<>();
        this.onCategorySelection = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LibrarySidebarToggleGroupSkin createSkin() {
        return new LibrarySidebarToggleGroupSkin(this);
    }

    public Runnable getOnAllCategorySelection() {
        return onAllCategorySelection.get();
    }

    public ObjectProperty<Runnable> onAllCategorySelectionProperty() {
        return onAllCategorySelection;
    }

    public void setOnAllCategorySelection(Runnable onAllCategorySelection) {
        this.onAllCategorySelection.set(onAllCategorySelection);
    }

    public Consumer<ShortcutCategoryDTO> getOnCategorySelection() {
        return onCategorySelection.get();
    }

    public ObjectProperty<Consumer<ShortcutCategoryDTO>> onCategorySelectionProperty() {
        return onCategorySelection;
    }

    public void setOnCategorySelection(Consumer<ShortcutCategoryDTO> onCategorySelection) {
        this.onCategorySelection.set(onCategorySelection);
    }
}
