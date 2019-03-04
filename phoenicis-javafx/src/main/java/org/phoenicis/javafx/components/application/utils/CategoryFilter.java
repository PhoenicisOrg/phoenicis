package org.phoenicis.javafx.components.application.utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.repository.dto.CategoryDTO;

import java.util.Optional;

/**
 * A filter implementation for {@link CategoryDTO} objects
 */
public interface CategoryFilter extends ScriptFilter {
    /**
     * The entered search term
     */
    StringProperty searchTermProperty();

    /**
     * The fuzzy search ratio
     */
    DoubleProperty fuzzySearchRatioProperty();

    /**
     * Filter function for {@link CategoryDTO} objects
     *
     * @param category The category which should be checked
     * @return True if the given <code>category</code> fulfills the filterCategory condition, false otherwise
     */
    default boolean filterCategory(CategoryDTO category) {
        /*
         * A category can be shown, if it contains at least one visible application
         */
        return category.getApplications().stream().anyMatch(application -> {
            final boolean matchesAtLeastOneScript = application.getScripts().stream().anyMatch(this::filterScript);

            final boolean matchesApplicationName = Optional.ofNullable(searchTermProperty().getValue())
                    .map(filterText -> StringUtils.isEmpty(filterText) || FuzzySearch
                            .partialRatio(application.getName().toLowerCase(), filterText) > fuzzySearchRatioProperty()
                                    .get())
                    .orElse(true);

            /*
             * An application can be shown, if:
             * - it contains at least one visible script
             * - its text matches the filterCategory text
             */
            return matchesApplicationName && matchesAtLeastOneScript;
        });
    }
}
