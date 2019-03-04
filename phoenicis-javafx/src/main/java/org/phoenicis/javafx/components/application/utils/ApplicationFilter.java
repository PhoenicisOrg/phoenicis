package org.phoenicis.javafx.components.application.utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;

import java.util.Optional;

/**
 * A filter implementation for {@link ApplicationDTO} objects
 */
public interface ApplicationFilter extends ScriptFilter {
    /**
     * The entered search term
     */
    StringProperty searchTermProperty();

    /**
     * The selected filter category
     */
    ObjectProperty<CategoryDTO> filterCategoryProperty();

    /**
     * The fuzzy search ratio
     */
    DoubleProperty fuzzySearchRatioProperty();

    /**
     * Filter function for {@link ApplicationDTO} objects
     *
     * @param application The application which should checked
     * @return True if the given <code>application</code> fulfills the filterCategory conditions, false otherwise
     */
    default boolean filterApplication(ApplicationDTO application) {
        final boolean matchesFilterCategory = Optional.ofNullable(filterCategoryProperty().getValue())
                .map(category -> category.getApplications().contains(application)).orElse(true);

        final boolean matchesAtLeastOneScript = application.getScripts().stream().anyMatch(this::filterScript);

        final boolean matchesApplicationName = Optional.ofNullable(searchTermProperty().getValue())
                .map(filterText -> StringUtils.isEmpty(filterText) || FuzzySearch
                        .partialRatio(application.getName().toLowerCase(), filterText) > fuzzySearchRatioProperty()
                                .get())
                .orElse(true);

        /*
         * An application can be shown, if:
         * - it belongs to the filterCategory category, if such a category is set
         * - it contains at least one visible script
         * - its text matches the filterCategory text
         */
        return matchesFilterCategory && matchesApplicationName && matchesAtLeastOneScript;
    }
}
