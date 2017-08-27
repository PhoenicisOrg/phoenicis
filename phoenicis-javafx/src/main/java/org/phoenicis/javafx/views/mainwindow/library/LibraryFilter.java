package org.phoenicis.javafx.views.mainwindow.library;

import org.phoenicis.javafx.views.AbstractFilter;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutDTO;

import java.util.Optional;

/**
 * Filter class for the "Library" tab
 *
 * @author Marc Arndt
 */
public class LibraryFilter extends AbstractFilter {
    /**
     * The entered search term.
     * If no search term has been entered, this value is {@link Optional#empty()}.
     */
    private Optional<String> searchTerm;

    /**
     * The selected shortcut category.
     * If no shortcut category has been selected, this value is {@link Optional#empty()}.
     */
    private Optional<ShortcutCategoryDTO> selectedShortcutCategory;

    /**
     * Constructor.
     * Assumes an empty search term and no selected shortcut category
     */
    public LibraryFilter() {
        super();

        this.searchTerm = Optional.empty();
        this.selectedShortcutCategory = Optional.empty();
    }

    /**
     * Sets the search term to the given string.
     *
     * @param searchTerm The new search term
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = Optional.of(searchTerm);

        this.triggerFilterChanged();
    }

    /**
     * Clears the search term
     */
    public void clearSearchTerm() {
        this.searchTerm = Optional.empty();

        this.triggerFilterChanged();
    }

    /**
     * Sets the selected shortcut category
     * @param shortcutCategory The shortcut category, that has been selected
     */
    public void setSelectedShortcutCategory(ShortcutCategoryDTO shortcutCategory) {
        this.selectedShortcutCategory = Optional.ofNullable(shortcutCategory);

        this.triggerFilterChanged();
    }

    /**
     * Clears both the search term and the selected shortcut category
     */
    public void clear() {
        this.searchTerm = Optional.empty();
        this.selectedShortcutCategory = Optional.empty();
    }

    /**
     * Filters a given shortcut category
     * @param shortcutCategory The to be filtered shortcut category
     * @return True if the shortcut category should be shown, false otherwise
     */
    public boolean filter(ShortcutCategoryDTO shortcutCategory) {
        return searchTerm.map(searchTerm -> shortcutCategory.getShortcuts().stream().anyMatch(this::filter)).orElse(true);
    }

    /**
     * Filters a given shortcut
     * @param shortcut The to be filtered shortcut
     * @return True if the shortcut should be shown, false otherwise
     */
    public boolean filter(ShortcutDTO shortcut) {
        return searchTerm.map(searchTerm -> shortcut.getName().toLowerCase().contains(searchTerm.toLowerCase())).orElse(true) &&
                selectedShortcutCategory.map(selectedShortcutCategory -> selectedShortcutCategory.getShortcuts().contains(shortcut)).orElse(true);
    }
}
