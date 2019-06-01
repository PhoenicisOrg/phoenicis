package org.phoenicis.javafx.views.mainwindow.library;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutDTO;

import java.util.Optional;

/**
 * Filter class for the "Library" tab
 *
 * @author Marc Arndt
 */
public class LibraryFilter {
    /**
     * The entered search term.
     * If no search term has been entered, this value is {@link Optional#empty()}.
     */
    private final StringProperty searchTerm;

    /**
     * The selected shortcut category.
     * If no shortcut category has been selected, this value is {@link Optional#empty()}.
     */
    private final ObjectProperty<ShortcutCategoryDTO> selectedShortcutCategory;

    /**
     * Constructor.
     * Assumes an empty search term and no selected shortcut category
     */
    public LibraryFilter() {
        super();

        this.searchTerm = new SimpleStringProperty("");
        this.selectedShortcutCategory = new SimpleObjectProperty<>();
    }

    /**
     * Filters a given shortcut category
     *
     * @param shortcutCategory The to be filtered shortcut category
     * @return True if the shortcut category should be shown, false otherwise
     */
    public boolean filter(ShortcutCategoryDTO shortcutCategory) {
        return Optional.ofNullable(searchTerm.getValueSafe())
                .map(searchTerm -> shortcutCategory.getShortcuts().stream().anyMatch(this::filter))
                .orElse(true);
    }

    /**
     * Filters a given shortcut
     *
     * @param shortcut The to be filtered shortcut
     * @return True if the shortcut should be shown, false otherwise
     */
    public boolean filter(ShortcutDTO shortcut) {
        final boolean searchTermConstraint = Optional.ofNullable(searchTerm.getValueSafe())
                .map(searchTerm -> shortcut.getInfo().getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .orElse(true);

        final boolean selectedShortcutCategoryConstraint = Optional.ofNullable(selectedShortcutCategory.getValue())
                .map(selectedShortcutCategory -> selectedShortcutCategory.getShortcuts().contains(shortcut))
                .orElse(true);

        return searchTermConstraint && selectedShortcutCategoryConstraint;
    }

    public StringProperty searchTermProperty() {
        return searchTerm;
    }

    public ObjectProperty<ShortcutCategoryDTO> selectedShortcutCategoryProperty() {
        return selectedShortcutCategory;
    }
}
